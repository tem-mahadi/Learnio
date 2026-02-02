package com.temmahadi.EnglishLearningApp.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.temmahadi.EnglishLearningApp.R;
import com.temmahadi.EnglishLearningApp.model.Achievement;

public class NotificationHelper {

    public static void showAchievementUnlocked(@NonNull Activity activity, @NonNull Achievement achievement) {
        View rootView = activity.findViewById(android.R.id.content);
        
        Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);
        
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(activity.getResources().getColor(R.color.primary));
        layout.setPadding(0, 0, 0, 0);
        
        // Hide default text
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        
        // Inflate custom view
        View customView = LayoutInflater.from(activity).inflate(R.layout.snackbar_achievement, null);
        
        ImageView ivIcon = customView.findViewById(R.id.ivAchievementIcon);
        TextView tvTitle = customView.findViewById(R.id.tvAchievementTitle);
        TextView tvName = customView.findViewById(R.id.tvAchievementName);
        TextView tvXP = customView.findViewById(R.id.tvXPReward);
        
        ivIcon.setImageResource(getIconForType(achievement.getType()));
        tvTitle.setText(activity.getString(R.string.achievement_unlocked));
        tvName.setText(achievement.getName());
        tvXP.setText("+" + achievement.getXpReward() + " XP");
        
        layout.addView(customView, 0);
        snackbar.show();
    }
    
    public static void showXPEarned(@NonNull Activity activity, int xpAmount) {
        Toast.makeText(activity, 
            String.format(activity.getString(R.string.xp_earned), xpAmount), 
            Toast.LENGTH_SHORT).show();
    }
    
    public static void showLevelUp(@NonNull Activity activity, int newLevel) {
        View rootView = activity.findViewById(android.R.id.content);
        
        Snackbar snackbar = Snackbar.make(rootView, 
            "🎉 " + activity.getString(R.string.level_up) + " Level " + newLevel + "!", 
            Snackbar.LENGTH_LONG);
        
        snackbar.setBackgroundTint(activity.getResources().getColor(R.color.correct_green));
        snackbar.setTextColor(activity.getResources().getColor(R.color.white));
        snackbar.show();
    }
    
    public static void showStreakUpdate(@NonNull Activity activity, int streakDays) {
        View rootView = activity.findViewById(android.R.id.content);
        
        Snackbar snackbar = Snackbar.make(rootView, 
            "🔥 " + streakDays + " day streak! Keep it up!", 
            Snackbar.LENGTH_SHORT);
        
        snackbar.setBackgroundTint(0xFFFF6B35);
        snackbar.setTextColor(activity.getResources().getColor(R.color.white));
        snackbar.show();
    }
    
    public static void showDailyGoalReached(@NonNull Activity activity) {
        View rootView = activity.findViewById(android.R.id.content);
        
        Snackbar snackbar = Snackbar.make(rootView, 
            activity.getString(R.string.daily_goal_reached), 
            Snackbar.LENGTH_LONG);
        
        snackbar.setBackgroundTint(activity.getResources().getColor(R.color.correct_green));
        snackbar.setTextColor(activity.getResources().getColor(R.color.white));
        snackbar.show();
    }
    
    private static int getIconForType(String type) {
        if (type == null) return R.drawable.ic_trophy;
        
        switch (type) {
            case Achievement.TYPE_PRACTICE_COUNT:
                return R.drawable.ic_practice;
            case Achievement.TYPE_STREAK:
                return R.drawable.ic_streak;
            case Achievement.TYPE_RECORDINGS:
                return R.drawable.ic_mic;
            case Achievement.TYPE_MASTERED:
                return R.drawable.ic_star_filled;
            case Achievement.TYPE_LEVEL:
                return R.drawable.ic_level_badge;
            case Achievement.TYPE_FAVORITES:
                return R.drawable.ic_favorite_filled;
            default:
                return R.drawable.ic_trophy;
        }
    }
}
