package com.temmahadi.EnglishLearningApp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey
    private int id = 1; // Single record for user progress
    
    private int totalPracticeCount;
    private int currentStreak;
    private int longestStreak;
    private long lastPracticeDate;
    private int sentencesMastered;
    private int totalRecordingsCount;
    private int dailyGoal;
    private int todayPracticeCount;
    private int totalXP;
    private int level;
    private String unlockedAchievements; // JSON string of achievement IDs
    
    public UserProgress() {
        this.dailyGoal = 5; // Default daily goal
        this.level = 1;
        this.totalXP = 0;
        this.unlockedAchievements = "[]";
    }
    
    // Calculate level from XP
    public int calculateLevel() {
        // Level formula: Level = sqrt(XP / 100) + 1
        return (int) Math.floor(Math.sqrt(totalXP / 100.0)) + 1;
    }
    
    // Calculate XP needed for next level
    public int getXPForNextLevel() {
        int nextLevel = level + 1;
        return (nextLevel - 1) * (nextLevel - 1) * 100;
    }
    
    // Calculate XP required for a specific level
    public int getXPForLevel(int targetLevel) {
        if (targetLevel <= 1) return 0;
        return (targetLevel - 1) * (targetLevel - 1) * 100;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getTotalPracticeCount() { return totalPracticeCount; }
    public void setTotalPracticeCount(int totalPracticeCount) { this.totalPracticeCount = totalPracticeCount; }
    
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    
    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }
    
    public long getLastPracticeDate() { return lastPracticeDate; }
    public void setLastPracticeDate(long lastPracticeDate) { this.lastPracticeDate = lastPracticeDate; }
    
    public int getSentencesMastered() { return sentencesMastered; }
    public void setSentencesMastered(int sentencesMastered) { this.sentencesMastered = sentencesMastered; }
    
    public int getTotalRecordingsCount() { return totalRecordingsCount; }
    public void setTotalRecordingsCount(int totalRecordingsCount) { this.totalRecordingsCount = totalRecordingsCount; }
    
    public int getDailyGoal() { return dailyGoal; }
    public void setDailyGoal(int dailyGoal) { this.dailyGoal = dailyGoal; }
    
    public int getTodayPracticeCount() { return todayPracticeCount; }
    public void setTodayPracticeCount(int todayPracticeCount) { this.todayPracticeCount = todayPracticeCount; }
    
    public int getTotalXP() { return totalXP; }
    public void setTotalXP(int totalXP) { this.totalXP = totalXP; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public String getUnlockedAchievements() { return unlockedAchievements; }
    public void setUnlockedAchievements(String unlockedAchievements) { this.unlockedAchievements = unlockedAchievements; }
}
