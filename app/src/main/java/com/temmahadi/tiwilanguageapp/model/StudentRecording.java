package com.temmahadi.tiwilanguageapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.Ignore;

@Entity(tableName = "student_recordings",
        foreignKeys = @ForeignKey(entity = Sentence.class,
                parentColumns = "id",
                childColumns = "sentenceId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("sentenceId")})
public class StudentRecording {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int sentenceId;
    private String studentName;
    private String recordingPath;
    private long recordingDate;
    
    public StudentRecording() {}
    
    @Ignore
    public StudentRecording(int sentenceId, String studentName, String recordingPath, long recordingDate) {
        this.sentenceId = sentenceId;
        this.studentName = studentName;
        this.recordingPath = recordingPath;
        this.recordingDate = recordingDate;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getSentenceId() {
        return sentenceId;
    }
    
    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getRecordingPath() {
        return recordingPath;
    }
    
    public void setRecordingPath(String recordingPath) {
        this.recordingPath = recordingPath;
    }
    
    public long getRecordingDate() {
        return recordingDate;
    }
    
    public void setRecordingDate(long recordingDate) {
        this.recordingDate = recordingDate;
    }
}
