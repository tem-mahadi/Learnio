package com.temmahadi.EnglishLearningApp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.SentenceProgress;
import com.temmahadi.EnglishLearningApp.model.UserProgress;
import com.temmahadi.EnglishLearningApp.repository.AppRepository;

import java.util.Calendar;

public class ProgressTracker {
    
    private static final String PREFS_NAME = "progress_prefs";
    private static final String KEY_LAST_PRACTICE_DAY = "last_practice_day";
    
    public interface ProgressUpdateListener {
        void onXPEarned(int xpAmount, int totalXP);
        void onLevelUp(int newLevel);
        void onStreakUpdate(int newStreak);
        void onDailyGoalReached();
    }
    
    private static ProgressUpdateListener listener;
    
    public static void setProgressUpdateListener(ProgressUpdateListener l) {
        listener = l;
    }
    
    // XP Values
    public static final int XP_PRACTICE = 5;
    public static final int XP_RECORDING = 10;
    public static final int XP_MASTERY = 25;
    public static final int XP_FIRST_PRACTICE = 15;
    public static final int XP_STREAK_BONUS = 5; // Per day of streak
    
    // Initialize user progress if not exists
    public static void initializeUserProgress(Context context) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            UserProgress progress = db.userProgressDao().getUserProgressSync();
            
            if (progress == null) {
                progress = new UserProgress();
                db.userProgressDao().insertOrUpdate(progress);
            }
            
            // Check if it's a new day and reset daily count
            checkAndResetDailyProgress(context, db);
        });
    }
    
    // Check and reset daily progress
    private static void checkAndResetDailyProgress(Context context, AppDatabase db) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int lastDay = prefs.getInt(KEY_LAST_PRACTICE_DAY, -1);
        int today = getDayOfYear();
        
        if (lastDay != today) {
            db.userProgressDao().resetDailyCount();
            prefs.edit().putInt(KEY_LAST_PRACTICE_DAY, today).apply();
            
            // Check streak
            UserProgress progress = db.userProgressDao().getUserProgressSync();
            if (progress != null) {
                long lastPractice = progress.getLastPracticeDate();
                long oneDayAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                long twoDaysAgo = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000);
                
                if (lastPractice < twoDaysAgo) {
                    // Streak broken
                    progress.setCurrentStreak(0);
                    db.userProgressDao().update(progress);
                }
            }
        }
    }
    
    private static int getDayOfYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_YEAR) + (cal.get(Calendar.YEAR) * 1000);
    }
    
    // Record a practice session
    public static void recordPractice(Context context, int sentenceId, boolean isFirstPractice) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            long now = System.currentTimeMillis();
            
            // Update sentence progress
            SentenceProgress sentenceProgress = db.sentenceProgressDao().getProgressForSentenceSync(sentenceId);
            if (sentenceProgress == null) {
                sentenceProgress = new SentenceProgress(sentenceId);
                db.sentenceProgressDao().insert(sentenceProgress);
                sentenceProgress = db.sentenceProgressDao().getProgressForSentenceSync(sentenceId);
            }
            db.sentenceProgressDao().incrementPracticeCount(sentenceId, now);
            
            // Auto-calculate mastery level based on practice count
            // Mastery increases at: 1, 3, 6, 10, 15 practices
            int practiceCount = sentenceProgress.getPracticeCount() + 1; // +1 because we just incremented
            int newMasteryLevel = calculateMasteryLevel(practiceCount);
            int currentMastery = sentenceProgress.getMasteryLevel();
            
            if (newMasteryLevel > currentMastery) {
                db.sentenceProgressDao().updateMasteryLevel(sentenceId, newMasteryLevel);
                
                // If mastered (level 5), increment mastered count and award XP
                if (newMasteryLevel >= 5 && currentMastery < 5) {
                    db.userProgressDao().incrementMasteredCount();
                }
            }
            
            // Calculate XP
            int xpEarned = isFirstPractice ? XP_FIRST_PRACTICE : XP_PRACTICE;
            
            // Update user progress
            UserProgress userProgress = db.userProgressDao().getUserProgressSync();
            if (userProgress != null) {
                // Add streak bonus
                int streakBonus = Math.min(userProgress.getCurrentStreak(), 10) * XP_STREAK_BONUS;
                xpEarned += streakBonus;
                
                // Update practice count and XP
                db.userProgressDao().incrementPracticeCount(xpEarned);
                db.userProgressDao().updateLastPracticeDate(now);
                
                // Update streak
                long lastPractice = userProgress.getLastPracticeDate();
                long oneDayAgo = now - (24 * 60 * 60 * 1000);
                
                if (lastPractice > oneDayAgo || userProgress.getCurrentStreak() == 0) {
                    if (getDayOfYear() != getDayFromTimestamp(lastPractice)) {
                        int newStreak = userProgress.getCurrentStreak() + 1;
                        db.userProgressDao().updateStreak(newStreak);
                        
                        if (listener != null) {
                            final int streak = newStreak;
                            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                                listener.onStreakUpdate(streak);
                            });
                        }
                    }
                }
                
                // Check level up
                userProgress = db.userProgressDao().getUserProgressSync();
                int oldLevel = userProgress.getLevel();
                int newLevel = userProgress.calculateLevel();
                
                if (newLevel > oldLevel) {
                    db.userProgressDao().updateLevel(newLevel);
                    if (listener != null) {
                        final int level = newLevel;
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                            listener.onLevelUp(level);
                        });
                    }
                }
                
                // Notify XP earned
                if (listener != null) {
                    final int xp = xpEarned;
                    final int totalXP = userProgress.getTotalXP();
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        listener.onXPEarned(xp, totalXP);
                    });
                }
                
                // Check daily goal
                userProgress = db.userProgressDao().getUserProgressSync();
                if (userProgress.getTodayPracticeCount() == userProgress.getDailyGoal()) {
                    if (listener != null) {
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                            listener.onDailyGoalReached();
                        });
                    }
                }
                
                // Check achievements
                AchievementManager.checkAndUnlockAchievements(context, userProgress);
            }
        });
    }
    
    private static int getDayFromTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.DAY_OF_YEAR) + (cal.get(Calendar.YEAR) * 1000);
    }
    
    // Calculate mastery level based on practice count
    // Level 1: 1 practice, Level 2: 3 practices, Level 3: 6 practices
    // Level 4: 10 practices, Level 5: 15 practices (mastered)
    private static int calculateMasteryLevel(int practiceCount) {
        if (practiceCount >= 15) return 5;
        if (practiceCount >= 10) return 4;
        if (practiceCount >= 6) return 3;
        if (practiceCount >= 3) return 2;
        if (practiceCount >= 1) return 1;
        return 0;
    }
    
    // Record a new recording
    public static void recordRecording(Context context) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            db.userProgressDao().incrementRecordingsCount();
            
            UserProgress progress = db.userProgressDao().getUserProgressSync();
            if (progress != null) {
                progress.setTotalXP(progress.getTotalXP() + XP_RECORDING);
                db.userProgressDao().update(progress);
                
                AchievementManager.checkAndUnlockAchievements(context, progress);
            }
        });
    }
    
    // Record mastery
    public static void recordMastery(Context context, int sentenceId) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            
            db.sentenceProgressDao().updateMasteryLevel(sentenceId, 5);
            db.userProgressDao().incrementMasteredCount();
            
            UserProgress progress = db.userProgressDao().getUserProgressSync();
            if (progress != null) {
                progress.setTotalXP(progress.getTotalXP() + XP_MASTERY);
                db.userProgressDao().update(progress);
                
                AchievementManager.checkAndUnlockAchievements(context, progress);
            }
        });
    }
    
    // Toggle favorite
    public static void toggleFavorite(Context context, int sentenceId, boolean isFavorite) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            
            SentenceProgress progress = db.sentenceProgressDao().getProgressForSentenceSync(sentenceId);
            if (progress == null) {
                progress = new SentenceProgress(sentenceId);
                progress.setFavorite(isFavorite);
                db.sentenceProgressDao().insert(progress);
            } else {
                db.sentenceProgressDao().setFavorite(sentenceId, isFavorite);
            }
            
            if (isFavorite) {
                int favoriteCount = db.sentenceProgressDao().getFavoritesSync().size();
                AchievementManager.checkFavoritesAchievement(context, favoriteCount);
            }
        });
    }
    
    // Update mastery level
    public static void updateMasteryLevel(Context context, int sentenceId, int level) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            
            SentenceProgress progress = db.sentenceProgressDao().getProgressForSentenceSync(sentenceId);
            boolean wasNotMastered = progress == null || !progress.isMastered();
            
            db.sentenceProgressDao().updateMasteryLevel(sentenceId, level);
            
            if (level >= 5 && wasNotMastered) {
                recordMastery(context, sentenceId);
            }
        });
    }
}
