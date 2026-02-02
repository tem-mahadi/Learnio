package com.temmahadi.EnglishLearningApp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.temmahadi.EnglishLearningApp.adapter.SentenceAdapter;
import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.Sentence;
import com.temmahadi.EnglishLearningApp.model.SentenceProgress;
import com.temmahadi.EnglishLearningApp.repository.AppRepository;
import com.temmahadi.EnglishLearningApp.utils.ProgressTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity implements SentenceAdapter.OnSentenceClickListener {

    private RecyclerView recyclerView;
    private View tvEmpty;
    private SentenceAdapter adapter;
    private List<Sentence> allSentences = new ArrayList<>();
    private Map<Integer, SentenceProgress> progressMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.favorites);
        }

        initializeViews();
        loadFavorites();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerFavorites);
        tvEmpty = findViewById(R.id.tvEmpty);
        
        adapter = new SentenceAdapter(false); // Student mode (no recording)
        adapter.setOnSentenceClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadFavorites() {
        AppDatabase db = AppDatabase.getDatabase(this);
        
        // Get all sentences first
        db.sentenceDao().getAllSentences().observe(this, sentences -> {
            if (sentences != null) {
                allSentences = sentences;
                loadFavoriteProgress();
            }
        });
    }

    private void loadFavoriteProgress() {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            List<SentenceProgress> favorites = db.sentenceProgressDao().getFavoritesSync();
            
            runOnUiThread(() -> {
                if (favorites != null && !favorites.isEmpty()) {
                    // Build progress map and filter sentences
                    progressMap.clear();
                    List<Integer> favoriteIds = new ArrayList<>();
                    
                    for (SentenceProgress progress : favorites) {
                        progressMap.put(progress.getSentenceId(), progress);
                        favoriteIds.add(progress.getSentenceId());
                    }
                    
                    // Filter sentences that are favorites
                    List<Sentence> favoriteSentences = new ArrayList<>();
                    for (Sentence sentence : allSentences) {
                        if (favoriteIds.contains(sentence.getId())) {
                            favoriteSentences.add(sentence);
                        }
                    }
                    
                    adapter.setSentences(favoriteSentences);
                    adapter.setProgressMap(progressMap);
                    
                    tvEmpty.setVisibility(favoriteSentences.isEmpty() ? View.VISIBLE : View.GONE);
                    recyclerView.setVisibility(favoriteSentences.isEmpty() ? View.GONE : View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteProgress();
    }

    @Override
    public void onRecordClick(Sentence sentence) {
        // Not available in favorites view
    }

    @Override
    public void onPlayTeacherClick(Sentence sentence) {
        // Could implement playback here if needed
    }

    @Override
    public void onPlayStudentClick(Sentence sentence) {
        // Could implement playback here if needed
    }

    @Override
    public void onSentenceClick(Sentence sentence) {
        // Could open sentence detail here if needed
    }

    @Override
    public void onFavoriteClick(Sentence sentence, boolean isFavorite) {
        ProgressTracker.toggleFavorite(this, sentence.getId(), isFavorite);
        // Refresh the list after a short delay to allow DB update
        new android.os.Handler().postDelayed(this::loadFavoriteProgress, 300);
    }

    @Override
    public void onMasteryRatingClick(Sentence sentence, int rating) {
        ProgressTracker.updateMasteryLevel(this, sentence.getId(), rating);
        if (rating == 0) {
            android.widget.Toast.makeText(this, "Mastery reset", android.widget.Toast.LENGTH_SHORT).show();
        } else {
            android.widget.Toast.makeText(this, getString(R.string.mastery_updated, rating), android.widget.Toast.LENGTH_SHORT).show();
        }
        // Refresh the list
        new android.os.Handler().postDelayed(this::loadFavoriteProgress, 300);
    }
    
    @Override
    public void onEditClick(Sentence sentence) {
        // Launch AddSentenceActivity in edit mode
        android.content.Intent intent = new android.content.Intent(this, AddSentenceActivity.class);
        intent.putExtra(AddSentenceActivity.EXTRA_SENTENCE_ID, sentence.getId());
        intent.putExtra(AddSentenceActivity.EXTRA_SENTENCE_ENGLISH, sentence.getEnglish());
        intent.putExtra(AddSentenceActivity.EXTRA_SENTENCE_BANGLA, sentence.getBangla());
        intent.putExtra(AddSentenceActivity.EXTRA_SENTENCE_TOPIC, sentence.getTopic());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
