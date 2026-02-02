package com.temmahadi.EnglishLearningApp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.temmahadi.EnglishLearningApp.adapter.AchievementAdapter;
import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.Achievement;
import com.temmahadi.EnglishLearningApp.utils.AchievementManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AchievementsActivity extends AppCompatActivity {

    private RecyclerView rvAchievements;
    private TabLayout tabLayout;
    private TextView tvUnlockedCount;
    private AchievementAdapter adapter;
    private AppDatabase database;
    
    private List<Achievement> allAchievements = new ArrayList<>();
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.achievements);
        }

        database = AppDatabase.getDatabase(this);
        initializeViews();
        setupRecyclerView();
        setupTabs();
        loadAchievements();
    }

    private void initializeViews() {
        rvAchievements = findViewById(R.id.rvAchievements);
        tabLayout = findViewById(R.id.tabLayout);
        tvUnlockedCount = findViewById(R.id.tvUnlockedCount);
    }

    private void setupRecyclerView() {
        adapter = new AchievementAdapter();
        adapter.setShowLocked(true);
        adapter.setOnAchievementClickListener(this::showAchievementDetails);
        
        rvAchievements.setLayoutManager(new GridLayoutManager(this, 2));
        rvAchievements.setAdapter(adapter);
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentFilter = "all";
                        break;
                    case 1:
                        currentFilter = "unlocked";
                        break;
                    case 2:
                        currentFilter = "locked";
                        break;
                }
                filterAchievements();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadAchievements() {
        database.achievementDao().getAllAchievements().observe(this, achievements -> {
            if (achievements == null || achievements.isEmpty()) {
                // Initialize achievements
                AchievementManager.initializeAchievements(this);
                return;
            }
            
            allAchievements = achievements;
            int unlockedCount = 0;
            for (Achievement a : achievements) {
                if (a.isUnlocked()) unlockedCount++;
            }
            tvUnlockedCount.setText(unlockedCount + "/" + achievements.size() + " " + getString(R.string.unlocked));
            
            filterAchievements();
        });
    }

    private void filterAchievements() {
        List<Achievement> filtered = new ArrayList<>();
        
        for (Achievement a : allAchievements) {
            switch (currentFilter) {
                case "all":
                    filtered.add(a);
                    break;
                case "unlocked":
                    if (a.isUnlocked()) filtered.add(a);
                    break;
                case "locked":
                    if (!a.isUnlocked()) filtered.add(a);
                    break;
            }
        }
        
        adapter.setAchievements(filtered);
    }

    private void showAchievementDetails(Achievement achievement) {
        String status;
        if (achievement.isUnlocked()) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            status = getString(R.string.unlocked_on) + " " + sdf.format(new Date(achievement.getUnlockedDate()));
        } else {
            status = getString(R.string.not_yet_unlocked);
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(achievement.getName())
                .setMessage(achievement.getDescription() + "\n\n" +
                        getString(R.string.reward) + ": +" + achievement.getXpReward() + " XP\n" +
                        getString(R.string.status) + ": " + status)
                .setPositiveButton(R.string.ok, null)
                .show();
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
