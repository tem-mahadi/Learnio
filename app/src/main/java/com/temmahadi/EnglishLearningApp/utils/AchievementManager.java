package com.temmahadi.EnglishLearningApp.utils;

import android.content.Context;

import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.Achievement;
import com.temmahadi.EnglishLearningApp.model.UserProgress;
import com.temmahadi.EnglishLearningApp.repository.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    
    public interface AchievementUnlockListener {
        void onAchievementUnlocked(Achievement achievement);
    }
    
    private static AchievementUnlockListener listener;
    
    public static void setAchievementUnlockListener(AchievementUnlockListener l) {
        listener = l;
    }
    
    // Achievement Types
    public static final String TYPE_PRACTICE_COUNT = "PRACTICE_COUNT";
    public static final String TYPE_STREAK = "STREAK";
    public static final String TYPE_RECORDINGS = "RECORDINGS";
    public static final String TYPE_MASTERED = "MASTERED";
    public static final String TYPE_LEVEL = "LEVEL";
    public static final String TYPE_FAVORITES = "FAVORITES";
    
    // Initialize all achievements
    public static void initializeAchievements(Context context) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            int count = db.achievementDao().getTotalCount();
            
            if (count == 0) {
                List<Achievement> achievements = createDefaultAchievements();
                db.achievementDao().insertAll(achievements.toArray(new Achievement[0]));
            }
        });
    }
    
    private static List<Achievement> createDefaultAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        
        // Practice Count Achievements
        achievements.add(new Achievement(1, "First Steps", "Practice your first sentence", "ic_achievement_first", 1, TYPE_PRACTICE_COUNT, 10));
        achievements.add(new Achievement(2, "Getting Started", "Practice 10 sentences", "ic_achievement_practice", 10, TYPE_PRACTICE_COUNT, 25));
        achievements.add(new Achievement(3, "Practice Makes Perfect", "Practice 50 sentences", "ic_achievement_practice", 50, TYPE_PRACTICE_COUNT, 50));
        achievements.add(new Achievement(4, "Dedicated Learner", "Practice 100 sentences", "ic_achievement_practice", 100, TYPE_PRACTICE_COUNT, 100));
        achievements.add(new Achievement(5, "Language Champion", "Practice 500 sentences", "ic_achievement_champion", 500, TYPE_PRACTICE_COUNT, 250));
        achievements.add(new Achievement(6, "Master Practitioner", "Practice 1000 sentences", "ic_achievement_master", 1000, TYPE_PRACTICE_COUNT, 500));
        
        // Streak Achievements
        achievements.add(new Achievement(7, "On Fire!", "Maintain a 3-day streak", "ic_achievement_fire", 3, TYPE_STREAK, 30));
        achievements.add(new Achievement(8, "Week Warrior", "Maintain a 7-day streak", "ic_achievement_fire", 7, TYPE_STREAK, 75));
        achievements.add(new Achievement(9, "Fortnight Fighter", "Maintain a 14-day streak", "ic_achievement_fire", 14, TYPE_STREAK, 150));
        achievements.add(new Achievement(10, "Monthly Master", "Maintain a 30-day streak", "ic_achievement_fire", 30, TYPE_STREAK, 300));
        achievements.add(new Achievement(11, "Unstoppable", "Maintain a 100-day streak", "ic_achievement_legendary", 100, TYPE_STREAK, 1000));
        
        // Recordings Achievements
        achievements.add(new Achievement(12, "First Recording", "Make your first recording", "ic_achievement_mic", 1, TYPE_RECORDINGS, 10));
        achievements.add(new Achievement(13, "Voice Activated", "Make 10 recordings", "ic_achievement_mic", 10, TYPE_RECORDINGS, 25));
        achievements.add(new Achievement(14, "Recording Star", "Make 50 recordings", "ic_achievement_mic", 50, TYPE_RECORDINGS, 75));
        achievements.add(new Achievement(15, "Audio Master", "Make 100 recordings", "ic_achievement_mic", 100, TYPE_RECORDINGS, 150));
        
        // Mastery Achievements
        achievements.add(new Achievement(16, "First Mastery", "Master your first sentence", "ic_achievement_star", 1, TYPE_MASTERED, 20));
        achievements.add(new Achievement(17, "Rising Star", "Master 5 sentences", "ic_achievement_star", 5, TYPE_MASTERED, 50));
        achievements.add(new Achievement(18, "Shining Bright", "Master 10 sentences", "ic_achievement_star", 10, TYPE_MASTERED, 100));
        achievements.add(new Achievement(19, "Star Collector", "Master 25 sentences", "ic_achievement_star", 25, TYPE_MASTERED, 250));
        achievements.add(new Achievement(20, "Master of All", "Master all sentences", "ic_achievement_gold", 40, TYPE_MASTERED, 500));
        
        // Level Achievements
        achievements.add(new Achievement(21, "Level Up!", "Reach Level 2", "ic_achievement_level", 2, TYPE_LEVEL, 15));
        achievements.add(new Achievement(22, "Rising Learner", "Reach Level 5", "ic_achievement_level", 5, TYPE_LEVEL, 50));
        achievements.add(new Achievement(23, "Experienced", "Reach Level 10", "ic_achievement_level", 10, TYPE_LEVEL, 100));
        achievements.add(new Achievement(24, "Expert", "Reach Level 20", "ic_achievement_level", 20, TYPE_LEVEL, 200));
        achievements.add(new Achievement(25, "Legendary", "Reach Level 50", "ic_achievement_legendary", 50, TYPE_LEVEL, 1000));
        
        // Favorites Achievements
        achievements.add(new Achievement(26, "Bookmark Lover", "Add 5 sentences to favorites", "ic_achievement_heart", 5, TYPE_FAVORITES, 15));
        achievements.add(new Achievement(27, "Collection Master", "Add 15 sentences to favorites", "ic_achievement_heart", 15, TYPE_FAVORITES, 40));
        
        return achievements;
    }
    
    // Check and unlock achievements based on current progress
    public static void checkAndUnlockAchievements(Context context, UserProgress progress) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            
            // Check Practice Count achievements
            checkAchievementType(db, TYPE_PRACTICE_COUNT, progress.getTotalPracticeCount());
            
            // Check Streak achievements
            checkAchievementType(db, TYPE_STREAK, progress.getCurrentStreak());
            
            // Check Recordings achievements
            checkAchievementType(db, TYPE_RECORDINGS, progress.getTotalRecordingsCount());
            
            // Check Mastered achievements
            checkAchievementType(db, TYPE_MASTERED, progress.getSentencesMastered());
            
            // Check Level achievements
            checkAchievementType(db, TYPE_LEVEL, progress.getLevel());
        });
    }
    
    private static void checkAchievementType(AppDatabase db, String type, int currentValue) {
        Achievement nextAchievement = db.achievementDao().getNextAchievementForType(type);
        
        while (nextAchievement != null && currentValue >= nextAchievement.getRequiredValue()) {
            // Unlock this achievement
            db.achievementDao().unlockAchievement(nextAchievement.getId(), System.currentTimeMillis());
            
            // Award XP
            UserProgress progress = db.userProgressDao().getUserProgressSync();
            if (progress != null) {
                progress.setTotalXP(progress.getTotalXP() + nextAchievement.getXpReward());
                int newLevel = progress.calculateLevel();
                progress.setLevel(newLevel);
                db.userProgressDao().update(progress);
            }
            
            // Notify listener
            if (listener != null) {
                final Achievement unlockedAchievement = nextAchievement;
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    listener.onAchievementUnlocked(unlockedAchievement);
                });
            }
            
            // Check for next achievement
            nextAchievement = db.achievementDao().getNextAchievementForType(type);
        }
    }
    
    // Check favorites achievement
    public static void checkFavoritesAchievement(Context context, int favoriteCount) {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            checkAchievementType(db, TYPE_FAVORITES, favoriteCount);
        });
    }
}
