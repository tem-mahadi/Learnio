package com.temmahadi.EnglishLearningApp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.temmahadi.EnglishLearningApp.model.Sentence;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {
    
    public static class JsonSentence {
        private String English;
        private String Bangla;
        private String Topic;
        
        public String getEnglish() {
            return English;
        }
        
        public String getBangla() {
            return Bangla;
        }
        
        public String getTopic() {
            return Topic;
        }
    }
    
    public static List<Sentence> loadSentencesFromAssets(Context context) {
        List<Sentence> sentences = new ArrayList<>();
        
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("bangla_english_sentences.json");
            
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            
            String json = new String(buffer, StandardCharsets.UTF_8);
            
            Gson gson = new Gson();
            Type listType = new TypeToken<List<JsonSentence>>(){}.getType();
            List<JsonSentence> jsonSentences = gson.fromJson(json, listType);
            
            for (JsonSentence jsonSentence : jsonSentences) {
                Sentence sentence = new Sentence(
                    jsonSentence.getEnglish(),
                    jsonSentence.getBangla(),
                    jsonSentence.getTopic()
                );
                sentences.add(sentence);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return sentences;
    }
}
