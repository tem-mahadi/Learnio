package com.temmahadi.tiwilanguageapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.temmahadi.tiwilanguageapp.model.Sentence;
import com.temmahadi.tiwilanguageapp.model.StudentRecording;
import com.temmahadi.tiwilanguageapp.repository.AppRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Sentence>> allSentences;
    private LiveData<List<String>> allTopics;
    
    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allSentences = repository.getAllSentences();
        allTopics = repository.getAllTopics();
    }
    
    public LiveData<List<Sentence>> getAllSentences() {
        return allSentences;
    }
    
    public LiveData<List<String>> getAllTopics() {
        return allTopics;
    }
    
    public LiveData<List<Sentence>> getSentencesByTopic(String topic) {
        return repository.getSentencesByTopic(topic);
    }
    
    public LiveData<Sentence> getSentenceById(int id) {
        return repository.getSentenceById(id);
    }
    
    public void insert(Sentence sentence) {
        repository.insert(sentence);
    }
    
    public void insertAll(Sentence... sentences) {
        repository.insertAll(sentences);
    }
    
    public void update(Sentence sentence) {
        repository.update(sentence);
    }
    
    public void delete(Sentence sentence) {
        repository.delete(sentence);
    }
    
    public LiveData<List<StudentRecording>> getRecordingsForSentence(int sentenceId) {
        return repository.getRecordingsForSentence(sentenceId);
    }
    
    public LiveData<List<StudentRecording>> getRecordingsByStudent(String studentName) {
        return repository.getRecordingsByStudent(studentName);
    }
    
    public void insert(StudentRecording recording) {
        repository.insert(recording);
    }
    
    public void update(StudentRecording recording) {
        repository.update(recording);
    }
    
    public void delete(StudentRecording recording) {
        repository.delete(recording);
    }
}
