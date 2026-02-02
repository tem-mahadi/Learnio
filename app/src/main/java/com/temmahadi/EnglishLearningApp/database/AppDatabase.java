package com.temmahadi.EnglishLearningApp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.temmahadi.EnglishLearningApp.dao.AchievementDao;
import com.temmahadi.EnglishLearningApp.dao.SentenceDao;
import com.temmahadi.EnglishLearningApp.dao.SentenceProgressDao;
import com.temmahadi.EnglishLearningApp.dao.StudentRecordingDao;
import com.temmahadi.EnglishLearningApp.dao.UserProgressDao;
import com.temmahadi.EnglishLearningApp.model.Achievement;
import com.temmahadi.EnglishLearningApp.model.Sentence;
import com.temmahadi.EnglishLearningApp.model.SentenceProgress;
import com.temmahadi.EnglishLearningApp.model.StudentRecording;
import com.temmahadi.EnglishLearningApp.model.UserProgress;

@Database(entities = {
        Sentence.class, 
        StudentRecording.class, 
        UserProgress.class, 
        SentenceProgress.class,
        Achievement.class
}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    public abstract SentenceDao sentenceDao();
    public abstract StudentRecordingDao studentRecordingDao();
    public abstract UserProgressDao userProgressDao();
    public abstract SentenceProgressDao sentenceProgressDao();
    public abstract AchievementDao achievementDao();
    
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
