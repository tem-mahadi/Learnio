package com.temmahadi.tiwilanguageapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.temmahadi.tiwilanguageapp.dao.SentenceDao;
import com.temmahadi.tiwilanguageapp.dao.StudentRecordingDao;
import com.temmahadi.tiwilanguageapp.model.Sentence;
import com.temmahadi.tiwilanguageapp.model.StudentRecording;

@Database(entities = {Sentence.class, StudentRecording.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    public abstract SentenceDao sentenceDao();
    public abstract StudentRecordingDao studentRecordingDao();
    
    private static volatile AppDatabase INSTANCE;
    
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "english_learning_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
