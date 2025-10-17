package com.temmahadi.tiwilanguageapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.temmahadi.tiwilanguageapp.model.Sentence;

import java.util.List;

@Dao
public interface SentenceDao {
    
    @Query("SELECT * FROM sentences ORDER BY topic, id")
    LiveData<List<Sentence>> getAllSentences();
    
    @Query("SELECT * FROM sentences WHERE topic = :topic ORDER BY id")
    LiveData<List<Sentence>> getSentencesByTopic(String topic);
    
    @Query("SELECT DISTINCT topic FROM sentences ORDER BY topic")
    LiveData<List<String>> getAllTopics();
    
    @Query("SELECT * FROM sentences WHERE id = :id")
    LiveData<Sentence> getSentenceById(int id);
    
    @Insert
    void insert(Sentence sentence);
    
    @Insert
    void insertAll(Sentence... sentences);
    
    @Update
    void update(Sentence sentence);
    
    @Delete
    void delete(Sentence sentence);
    
    @Query("DELETE FROM sentences")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM sentences")
    int getCount();
}
