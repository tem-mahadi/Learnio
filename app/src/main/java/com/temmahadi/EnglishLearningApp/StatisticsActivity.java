package com.temmahadi.EnglishLearningApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.temmahadi.EnglishLearningApp.adapter.AchievementAdapter;
import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.Achievement;
import com.temmahadi.EnglishLearningApp.model.UserProgress;

import java.util.Calendar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvLevel, tvLevelTitle, tvXP, tvXPToNext;
    private ProgressBar progressXP;
    private TextView tvCurrentStreak, tvLongestStreak;
    private TextView tvDailyProgress;
    private ProgressBar progressDaily;
    private TextView tvTotalPractice, tvTotalRecordings, tvMastered, tvAchievements;
    private RecyclerView rvRecentAchievements;
    private MaterialCardView cardAchievements, cardNoAchievements;
    private LinearLayout weekCalendar;
    private TextView tvViewAll;

    private AppDatabase database;
    private AchievementAdapter achievementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.statistics);
        }

        database = AppDatabase.getDatabase(this);
        initializeViews();
        setupRecyclerView();
        loadStatistics();
        setupClickListeners();
    }

    private void initializeViews() {
        tvLevel = findViewById(R.id.tvLevel);
        tvLevelTitle = findViewById(R.id.tvLevelTitle);
        tvXP = findViewById(R.id.tvXP);
        tvXPToNext = findViewById(R.id.tvXPToNext);
        progressXP = findViewById(R.id.progressXP);
        
        tvCurrentStreak = findViewById(R.id.tvCurrentStreak);
        tvLongestStreak = findViewById(R.id.tvLongestStreak);
        
        tvDailyProgress = findViewById(R.id.tvDailyProgress);
        progressDaily = findViewById(R.id.progressDaily);
        
        tvTotalPractice = findViewById(R.id.tvTotalPractice);
        tvTotalRecordings = findViewById(R.id.tvTotalRecordings);
        tvMastered = findViewById(R.id.tvMastered);
        tvAchievements = findViewById(R.id.tvAchievements);
        
        rvRecentAchievements = findViewById(R.id.rvRecentAchievements);
        cardAchievements = findViewById(R.id.cardAchievements);
        cardNoAchievements = findViewById(R.id.cardNoAchievements);
        weekCalendar = findViewById(R.id.weekCalendar);
        tvViewAll = findViewById(R.id.tvViewAll);
    }

    private void setupRecyclerView() {
        achievementAdapter = new AchievementAdapter();
        rvRecentAchievements.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        rvRecentAchievements.setAdapter(achievementAdapter);
    }

    private void loadStatistics() {
        // Load user progress
        LiveData<UserProgress> progressLiveData = database.userProgressDao().getUserProgress();
        progressLiveData.observe(this, this::updateUserProgressUI);

        // Load achievements count
        database.achievementDao().getUnlockedAchievements().observe(this, achievements -> {
            if (achievements != null) {
                tvAchievements.setText(achievements.size() + "/27");
                
                if (achievements.isEmpty()) {
                    cardNoAchievements.setVisibility(View.VISIBLE);
                    rvRecentAchievements.setVisibility(View.GONE);
                } else {
                    cardNoAchievements.setVisibility(View.GONE);
                    rvRecentAchievements.setVisibility(View.VISIBLE);
                    
                    // Show last 5 achievements
                    List<Achievement> recent = achievements.subList(
                        0, Math.min(5, achievements.size())
                    );
                    achievementAdapter.setAchievements(recent);
                }
            }
        });
    }

    private void updateUserProgressUI(UserProgress progress) {
        if (progress == null) {
            progress = new UserProgress();
        }

        // Level and XP
        int level = progress.getLevel();
        tvLevel.setText(String.valueOf(level));
        tvLevelTitle.setText(getLevelTitle(level));
        tvXP.setText(progress.getTotalXP() + " XP");
        
        // Calculate XP progress to next level
        int xpForCurrentLevel = progress.getXPForLevel(level);
        int xpForNextLevel = progress.getXPForLevel(level + 1);
        int xpProgress = progress.getTotalXP() - xpForCurrentLevel;
        int xpNeeded = xpForNextLevel - xpForCurrentLevel;
        int progressPercent = (int) ((xpProgress * 100.0) / xpNeeded);
        
        progressXP.setProgress(progressPercent);
        tvXPToNext.setText((xpNeeded - xpProgress) + " XP to next level");

        // Streak
        tvCurrentStreak.setText(String.valueOf(progress.getCurrentStreak()));
        tvLongestStreak.setText(progress.getLongestStreak() + " days");
        
        // Build week calendar
        buildWeekCalendar(progress.getCurrentStreak());

        // Daily goal
        int todayCount = progress.getTodayPracticeCount();
        int dailyGoal = progress.getDailyGoal();
        tvDailyProgress.setText(todayCount + "/" + dailyGoal);
        int dailyProgressPercent = Math.min(100, (int) ((todayCount * 100.0) / dailyGoal));
        progressDaily.setProgress(dailyProgressPercent);

        // Statistics
        tvTotalPractice.setText(String.valueOf(progress.getTotalPracticeCount()));
        tvTotalRecordings.setText(String.valueOf(progress.getTotalRecordingsCount()));
        tvMastered.setText(String.valueOf(progress.getSentencesMastered()));
    }

    private String getLevelTitle(int level) {
        if (level < 5) return getString(R.string.level_beginner);
        if (level < 10) return getString(R.string.level_elementary);
        if (level < 20) return getString(R.string.level_intermediate);
        if (level < 35) return getString(R.string.level_advanced);
        if (level < 50) return getString(R.string.level_expert);
        return getString(R.string.level_master);
    }

    private void buildWeekCalendar(int currentStreak) {
        weekCalendar.removeAllViews();
        
        String[] dayNames = {"S", "M", "T", "W", "T", "F", "S"};
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK) - 1;
        
        for (int i = 0; i < 7; i++) {
            View dayView = getLayoutInflater().inflate(R.layout.item_calendar_day, weekCalendar, false);
            TextView tvDay = dayView.findViewById(R.id.tvDay);
            View indicator = dayView.findViewById(R.id.streakIndicator);
            
            tvDay.setText(dayNames[i]);
            
            // Mark completed days based on streak
            int daysFromToday = (7 + i - today) % 7;
            if (daysFromToday == 0) {
                // Today
                indicator.setBackgroundResource(R.drawable.bg_streak_today);
            } else if (daysFromToday <= currentStreak && daysFromToday > 0) {
                // Not yet (future days)
                indicator.setBackgroundResource(R.drawable.bg_streak_empty);
            } else if (7 - daysFromToday < currentStreak) {
                // Past completed days
                indicator.setBackgroundResource(R.drawable.bg_streak_completed);
            } else {
                indicator.setBackgroundResource(R.drawable.bg_streak_empty);
            }
            
            weekCalendar.addView(dayView);
        }
    }

    private void setupClickListeners() {
        cardAchievements.setOnClickListener(v -> {
            startActivity(new Intent(this, AchievementsActivity.class));
        });

        tvViewAll.setOnClickListener(v -> {
            startActivity(new Intent(this, AchievementsActivity.class));
        });
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
