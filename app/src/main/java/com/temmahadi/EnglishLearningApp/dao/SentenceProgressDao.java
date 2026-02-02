package com.temmahadi.EnglishLearningApp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.temmahadi.EnglishLearningApp.model.SentenceProgress;

import java.util.List;

@Dao
public interface SentenceProgressDao {
    
    @Query("SELECT * FROM sentence_progress WHERE sentenceId = :sentenceId")
    LiveData<SentenceProgress> getProgressForSentence(int sentenceId);
    
    @Query("SELECT * FROM sentence_progress WHERE sentenceId = :sentenceId")
    SentenceProgress getProgressForSentenceSync(int sentenceId);
    
    @Query("SELECT * FROM sentence_progress WHERE isFavorite = 1 ORDER BY lastPracticeDate DESC")
    LiveData<List<SentenceProgress>> getFavorites();
    
    @Query("SELECT * FROM sentence_progress WHERE isFavorite = 1")
    List<SentenceProgress> getFavoritesSync();
    
    @Query("SELECT * FROM sentence_progress WHERE isMastered = 1 ORDER BY lastPracticeDate DESC")
    LiveData<List<SentenceProgress>> getMastered();
    
    @Query("SELECT * FROM sentence_progress ORDER BY practiceCount DESC")
    LiveData<List<SentenceProgress>> getAllByPracticeCount();
    
    @Query("SELECT * FROM sentence_progress")
    List<SentenceProgress> getAllProgressSync();
    
    @Query("SELECT * FROM sentence_progress ORDER BY lastPracticeDate DESC LIMIT :limit")
    LiveData<List<SentenceProgress>> getRecentlyPracticed(int limit);
    
    @Query("SELECT COUNT(*) FROM sentence_progress WHERE isMastered = 1")
    LiveData<Integer> getMasteredCount();
    
    @Query("SELECT COUNT(*) FROM sentence_progress WHERE isFavorite = 1")
    LiveData<Integer> getFavoriteCount();
    
    @Query("SELECT SUM(practiceCount) FROM sentence_progress")
    LiveData<Integer> getTotalPracticeCount();
    
    @Query("SELECT AVG(masteryLevel) FROM sentence_progress WHERE practiceCount > 0")
    LiveData<Float> getAverageMasteryLevel();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SentenceProgress progress);
    
    @Update
    void update(SentenceProgress progress);
    
    @Query("UPDATE sentence_progress SET isFavorite = :isFavorite WHERE sentenceId = :sentenceId")
    void setFavorite(int sentenceId, boolean isFavorite);
    
    @Query("UPDATE sentence_progress SET practiceCount = practiceCount + 1, lastPracticeDate = :date WHERE sentenceId = :sentenceId")
    void incrementPracticeCount(int sentenceId, long date);
    
    @Query("UPDATE sentence_progress SET masteryLevel = :level, isMastered = CASE WHEN :level >= 5 THEN 1 ELSE isMastered END WHERE sentenceId = :sentenceId")
    void updateMasteryLevel(int sentenceId, int level);
    
    @Query("UPDATE sentence_progress SET difficultyRating = :rating WHERE sentenceId = :sentenceId")
    void setDifficultyRating(int sentenceId, int rating);
    
    @Query("UPDATE sentence_progress SET personalNotes = :notes WHERE sentenceId = :sentenceId")
    void setPersonalNotes(int sentenceId, String notes);
    
    @Query("SELECT sentenceId FROM sentence_progress WHERE isFavorite = 1")
    List<Integer> getFavoriteSentenceIds();
    
    @Query("SELECT sentenceId FROM sentence_progress WHERE isMastered = 1")
    List<Integer> getMasteredSentenceIds();
    
    @Query("SELECT * FROM sentence_progress WHERE practiceCount = 0 OR practiceCount IS NULL")
    List<SentenceProgress> getUnpracticedSentences();
}
