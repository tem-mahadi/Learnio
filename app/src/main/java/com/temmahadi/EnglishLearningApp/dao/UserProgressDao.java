package com.temmahadi.EnglishLearningApp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.temmahadi.EnglishLearningApp.model.UserProgress;

@Dao
public interface UserProgressDao {
    
    @Query("SELECT * FROM user_progress WHERE id = 1")
    LiveData<UserProgress> getUserProgress();
    
    @Query("SELECT * FROM user_progress WHERE id = 1")
    UserProgress getUserProgressSync();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(UserProgress progress);
    
    @Update
    void update(UserProgress progress);
    
    @Query("UPDATE user_progress SET totalPracticeCount = totalPracticeCount + 1, todayPracticeCount = todayPracticeCount + 1, totalXP = totalXP + :xpEarned WHERE id = 1")
    void incrementPracticeCount(int xpEarned);
    
    @Query("UPDATE user_progress SET currentStreak = :streak, longestStreak = CASE WHEN :streak > longestStreak THEN :streak ELSE longestStreak END WHERE id = 1")
    void updateStreak(int streak);
    
    @Query("UPDATE user_progress SET lastPracticeDate = :date WHERE id = 1")
    void updateLastPracticeDate(long date);
    
    @Query("UPDATE user_progress SET todayPracticeCount = 0 WHERE id = 1")
    void resetDailyCount();
    
    @Query("UPDATE user_progress SET sentencesMastered = sentencesMastered + 1 WHERE id = 1")
    void incrementMasteredCount();
    
    @Query("UPDATE user_progress SET totalRecordingsCount = totalRecordingsCount + 1 WHERE id = 1")
    void incrementRecordingsCount();
    
    @Query("UPDATE user_progress SET dailyGoal = :goal WHERE id = 1")
    void setDailyGoal(int goal);
    
    @Query("UPDATE user_progress SET level = :level WHERE id = 1")
    void updateLevel(int level);
    
    @Query("UPDATE user_progress SET unlockedAchievements = :achievements WHERE id = 1")
    void updateAchievements(String achievements);
}
