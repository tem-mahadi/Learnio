package com.temmahadi.EnglishLearningApp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.temmahadi.EnglishLearningApp.R;
import com.temmahadi.EnglishLearningApp.model.Achievement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<Achievement> achievements = new ArrayList<>();
    private boolean showLocked = false;
    private OnAchievementClickListener clickListener;

    public interface OnAchievementClickListener {
        void onAchievementClick(Achievement achievement);
    }

    public void setOnAchievementClickListener(OnAchievementClickListener listener) {
        this.clickListener = listener;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements != null ? achievements : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setShowLocked(boolean showLocked) {
        this.showLocked = showLocked;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        holder.bind(achievement);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    class AchievementViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivIcon;
        private final TextView tvName;
        private final TextView tvDescription;
        private final TextView tvXPReward;
        private final TextView tvUnlockedDate;
        private final View lockOverlay;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivAchievementIcon);
            tvName = itemView.findViewById(R.id.tvAchievementName);
            tvDescription = itemView.findViewById(R.id.tvAchievementDescription);
            tvXPReward = itemView.findViewById(R.id.tvXPReward);
            tvUnlockedDate = itemView.findViewById(R.id.tvUnlockedDate);
            lockOverlay = itemView.findViewById(R.id.lockOverlay);
        }

        public void bind(Achievement achievement) {
            tvName.setText(achievement.getName());
            tvDescription.setText(achievement.getDescription());
            tvXPReward.setText("+" + achievement.getXpReward() + " XP");

            // Set icon based on achievement type
            int iconRes = getIconForType(achievement.getType());
            ivIcon.setImageResource(iconRes);

            if (achievement.isUnlocked()) {
                lockOverlay.setVisibility(View.GONE);
                ivIcon.setAlpha(1.0f);
                tvName.setAlpha(1.0f);
                tvDescription.setAlpha(1.0f);
                
                if (achievement.getUnlockedDate() > 0) {
                    tvUnlockedDate.setVisibility(View.VISIBLE);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                    tvUnlockedDate.setText(sdf.format(new Date(achievement.getUnlockedDate())));
                } else {
                    tvUnlockedDate.setVisibility(View.GONE);
                }
            } else {
                if (showLocked) {
                    lockOverlay.setVisibility(View.VISIBLE);
                    ivIcon.setAlpha(0.4f);
                    tvName.setAlpha(0.6f);
                    tvDescription.setAlpha(0.6f);
                    tvUnlockedDate.setVisibility(View.GONE);
                }
            }

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onAchievementClick(achievement);
                }
            });
        }

        private int getIconForType(String type) {
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
}
