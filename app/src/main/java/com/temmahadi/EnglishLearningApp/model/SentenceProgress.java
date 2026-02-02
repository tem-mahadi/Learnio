package com.temmahadi.EnglishLearningApp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "sentence_progress",
        foreignKeys = @ForeignKey(entity = Sentence.class,
                parentColumns = "id",
                childColumns = "sentenceId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("sentenceId")})
public class SentenceProgress {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int sentenceId;
    private int practiceCount;
    private int difficultyRating; // 1=Easy, 2=Medium, 3=Hard
    private int masteryLevel; // 0-5 stars
    private boolean isFavorite;
    private boolean isMastered;
    private long lastPracticeDate;
    private long firstPracticeDate;
    private int correctAttempts; // For quiz mode tracking
    private int totalAttempts;
    private String personalNotes;
    
    public SentenceProgress() {
        this.difficultyRating = 2; // Default to medium
        this.masteryLevel = 0;
        this.isFavorite = false;
        this.isMastered = false;
    }
    
    public SentenceProgress(int sentenceId) {
        this.sentenceId = sentenceId;
        this.difficultyRating = 2;
        this.masteryLevel = 0;
        this.isFavorite = false;
        this.isMastered = false;
        this.firstPracticeDate = System.currentTimeMillis();
    }
    
    // Calculate mastery percentage
    public int getMasteryPercentage() {
        return (masteryLevel * 100) / 5;
    }
    
    // Calculate accuracy percentage
    public int getAccuracyPercentage() {
        if (totalAttempts == 0) return 0;
        return (correctAttempts * 100) / totalAttempts;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getSentenceId() { return sentenceId; }
    public void setSentenceId(int sentenceId) { this.sentenceId = sentenceId; }
    
    public int getPracticeCount() { return practiceCount; }
    public void setPracticeCount(int practiceCount) { this.practiceCount = practiceCount; }
    
    public int getDifficultyRating() { return difficultyRating; }
    public void setDifficultyRating(int difficultyRating) { this.difficultyRating = difficultyRating; }
    
    public int getMasteryLevel() { return masteryLevel; }
    public void setMasteryLevel(int masteryLevel) { this.masteryLevel = masteryLevel; }
    
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    
    public boolean isMastered() { return isMastered; }
    public void setMastered(boolean mastered) { isMastered = mastered; }
    
    public long getLastPracticeDate() { return lastPracticeDate; }
    public void setLastPracticeDate(long lastPracticeDate) { this.lastPracticeDate = lastPracticeDate; }
    
    public long getFirstPracticeDate() { return firstPracticeDate; }
    public void setFirstPracticeDate(long firstPracticeDate) { this.firstPracticeDate = firstPracticeDate; }
    
    public int getCorrectAttempts() { return correctAttempts; }
    public void setCorrectAttempts(int correctAttempts) { this.correctAttempts = correctAttempts; }
    
    public int getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }
    
    public String getPersonalNotes() { return personalNotes; }
    public void setPersonalNotes(String personalNotes) { this.personalNotes = personalNotes; }
}
