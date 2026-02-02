package com.temmahadi.EnglishLearningApp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {
    
    // Achievement type constants
    public static final String TYPE_PRACTICE_COUNT = "PRACTICE_COUNT";
    public static final String TYPE_STREAK = "STREAK";
    public static final String TYPE_RECORDINGS = "RECORDINGS";
    public static final String TYPE_MASTERED = "MASTERED";
    public static final String TYPE_LEVEL = "LEVEL";
    public static final String TYPE_FAVORITES = "FAVORITES";
    
    @PrimaryKey
    private int id;
    
    private String name;
    private String description;
    private String iconName;
    private int requiredValue;
    private String type; // PRACTICE_COUNT, STREAK, RECORDINGS, MASTERED, LEVEL
    private boolean unlocked;
    private long unlockedDate;
    private int xpReward;
    
    public Achievement() {}
    
    public Achievement(int id, String name, String description, String iconName, 
                      int requiredValue, String type, int xpReward) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconName = iconName;
        this.requiredValue = requiredValue;
        this.type = type;
        this.xpReward = xpReward;
        this.unlocked = false;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
    
    public int getRequiredValue() { return requiredValue; }
    public void setRequiredValue(int requiredValue) { this.requiredValue = requiredValue; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    
    public long getUnlockedDate() { return unlockedDate; }
    public void setUnlockedDate(long unlockedDate) { this.unlockedDate = unlockedDate; }
    
    public int getXpReward() { return xpReward; }
    public void setXpReward(int xpReward) { this.xpReward = xpReward; }
}
