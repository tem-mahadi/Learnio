package com.temmahadi.EnglishLearningApp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "sentences")
public class Sentence {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String english;
    private String bangla;
    private String topic;
    private String teacherRecordingPath;
    
    public Sentence() {}
    
    @Ignore
    public Sentence(String english, String bangla, String topic) {
        this.english = english;
        this.bangla = bangla;
        this.topic = topic;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getEnglish() {
        return english;
    }
    
    public void setEnglish(String english) {
        this.english = english;
    }
    
    public String getBangla() {
        return bangla;
    }
    
    public void setBangla(String bangla) {
        this.bangla = bangla;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public String getTeacherRecordingPath() {
        return teacherRecordingPath;
    }
    
    public void setTeacherRecordingPath(String teacherRecordingPath) {
        this.teacherRecordingPath = teacherRecordingPath;
    }
}
