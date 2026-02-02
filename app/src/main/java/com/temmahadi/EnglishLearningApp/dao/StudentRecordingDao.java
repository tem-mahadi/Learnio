package com.temmahadi.EnglishLearningApp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.temmahadi.EnglishLearningApp.model.StudentRecording;

import java.util.List;

@Dao
public interface StudentRecordingDao {
    
    @Query("SELECT * FROM student_recordings WHERE sentenceId = :sentenceId ORDER BY recordingDate DESC")
    LiveData<List<StudentRecording>> getRecordingsForSentence(int sentenceId);
    
    @Query("SELECT * FROM student_recordings WHERE sentenceId = :sentenceId ORDER BY recordingDate DESC")
    List<StudentRecording> getRecordingsForSentenceSync(int sentenceId);
    
    @Query("SELECT * FROM student_recordings WHERE studentName = :studentName ORDER BY recordingDate DESC")
    LiveData<List<StudentRecording>> getRecordingsByStudent(String studentName);
    
    @Query("SELECT * FROM student_recordings WHERE id = :id")
    LiveData<StudentRecording> getRecordingById(int id);
    
    @Insert
    void insert(StudentRecording recording);
    
    @Update
    void update(StudentRecording recording);
    
    @Delete
    void delete(StudentRecording recording);
    
    @Query("DELETE FROM student_recordings WHERE sentenceId = :sentenceId")
    void deleteRecordingsForSentence(int sentenceId);
}
