package com.temmahadi.EnglishLearningApp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.temmahadi.EnglishLearningApp.model.Achievement;

import java.util.List;

@Dao
public interface AchievementDao {
    
    @Query("SELECT * FROM achievements ORDER BY id")
    LiveData<List<Achievement>> getAllAchievements();
    
    @Query("SELECT * FROM achievements WHERE unlocked = 1 ORDER BY unlockedDate DESC")
    LiveData<List<Achievement>> getUnlockedAchievements();
    
    @Query("SELECT * FROM achievements WHERE unlocked = 0 ORDER BY requiredValue")
    LiveData<List<Achievement>> getLockedAchievements();
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    Achievement getAchievementById(int id);
    
    @Query("SELECT COUNT(*) FROM achievements WHERE unlocked = 1")
    LiveData<Integer> getUnlockedCount();
    
    @Query("SELECT COUNT(*) FROM achievements")
    int getTotalCount();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Achievement achievement);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Achievement... achievements);
    
    @Update
    void update(Achievement achievement);
    
    @Query("UPDATE achievements SET unlocked = 1, unlockedDate = :date WHERE id = :id")
    void unlockAchievement(int id, long date);
    
    @Query("SELECT * FROM achievements WHERE type = :type AND unlocked = 0 ORDER BY requiredValue ASC LIMIT 1")
    Achievement getNextAchievementForType(String type);
}
