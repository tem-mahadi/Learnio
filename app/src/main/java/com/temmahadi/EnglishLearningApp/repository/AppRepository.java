package com.temmahadi.EnglishLearningApp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.temmahadi.EnglishLearningApp.dao.SentenceDao;
import com.temmahadi.EnglishLearningApp.dao.StudentRecordingDao;
import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.Sentence;
import com.temmahadi.EnglishLearningApp.model.StudentRecording;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private SentenceDao sentenceDao;
    private StudentRecordingDao studentRecordingDao;
    private LiveData<List<Sentence>> allSentences;
    private LiveData<List<String>> allTopics;
    
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        sentenceDao = db.sentenceDao();
        studentRecordingDao = db.studentRecordingDao();
        allSentences = sentenceDao.getAllSentences();
        allTopics = sentenceDao.getAllTopics();
    }
    
    // Sentence operations
    public LiveData<List<Sentence>> getAllSentences() {
        return allSentences;
    }
    
    public LiveData<List<String>> getAllTopics() {
        return allTopics;
    }
    
    public LiveData<List<Sentence>> getSentencesByTopic(String topic) {
        return sentenceDao.getSentencesByTopic(topic);
    }
    
    public LiveData<Sentence> getSentenceById(int id) {
        return sentenceDao.getSentenceById(id);
    }
    
    public void insert(Sentence sentence) {
        databaseWriteExecutor.execute(() -> {
            sentenceDao.insert(sentence);
        });
    }
    
    public void insertAll(Sentence... sentences) {
        databaseWriteExecutor.execute(() -> {
            sentenceDao.insertAll(sentences);
        });
    }
    
    public void update(Sentence sentence) {
        databaseWriteExecutor.execute(() -> {
            sentenceDao.update(sentence);
        });
    }
    
    public void delete(Sentence sentence) {
        databaseWriteExecutor.execute(() -> {
            sentenceDao.delete(sentence);
        });
    }
    
    // Student recording operations
    public LiveData<List<StudentRecording>> getRecordingsForSentence(int sentenceId) {
        return studentRecordingDao.getRecordingsForSentence(sentenceId);
    }
    
    public LiveData<List<StudentRecording>> getRecordingsByStudent(String studentName) {
        return studentRecordingDao.getRecordingsByStudent(studentName);
    }
    
    public void insert(StudentRecording recording) {
        databaseWriteExecutor.execute(() -> {
            studentRecordingDao.insert(recording);
        });
    }
    
    public void update(StudentRecording recording) {
        databaseWriteExecutor.execute(() -> {
            studentRecordingDao.update(recording);
        });
    }
    
    public void delete(StudentRecording recording) {
        databaseWriteExecutor.execute(() -> {
            studentRecordingDao.delete(recording);
        });
    }
    
    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
}
