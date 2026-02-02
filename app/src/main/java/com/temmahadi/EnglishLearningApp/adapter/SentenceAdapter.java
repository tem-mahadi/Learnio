package com.temmahadi.EnglishLearningApp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.temmahadi.EnglishLearningApp.R;
import com.temmahadi.EnglishLearningApp.model.Sentence;
import com.temmahadi.EnglishLearningApp.model.SentenceProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.SentenceViewHolder> {
    
    private List<Sentence> sentences = new ArrayList<>();
    private Map<Integer, SentenceProgress> progressMap = new HashMap<>();
    private OnSentenceClickListener listener;
    private boolean isTeacherMode;
    private int currentRecordingPosition = -1;
    private int currentPlayingTeacherPosition = -1;
    private int currentPlayingStudentPosition = -1;
    
    public interface OnSentenceClickListener {
        void onRecordClick(Sentence sentence);
        void onPlayTeacherClick(Sentence sentence);
        void onPlayStudentClick(Sentence sentence);
        void onSentenceClick(Sentence sentence);
        void onFavoriteClick(Sentence sentence, boolean isFavorite);
        void onMasteryRatingClick(Sentence sentence, int rating);
        void onEditClick(Sentence sentence);
    }
    
    public SentenceAdapter(boolean isTeacherMode) {
        this.isTeacherMode = isTeacherMode;
    }
    
    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sentence, parent, false);
        return new SentenceViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        Sentence sentence = sentences.get(position);
        holder.bind(sentence, position);
    }
    
    @Override
    public int getItemCount() {
        return sentences.size();
    }
    
    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
        notifyDataSetChanged();
    }
    
    public void setProgressMap(Map<Integer, SentenceProgress> progressMap) {
        this.progressMap = progressMap != null ? progressMap : new HashMap<>();
        notifyDataSetChanged();
    }
    
    public void updateProgress(int sentenceId, SentenceProgress progress) {
        progressMap.put(sentenceId, progress);
        // Find position and update
        for (int i = 0; i < sentences.size(); i++) {
            if (sentences.get(i).getId() == sentenceId) {
                notifyItemChanged(i);
                break;
            }
        }
    }
    
    public void setOnSentenceClickListener(OnSentenceClickListener listener) {
        this.listener = listener;
    }
    
    public void setRecordingState(int position, boolean isRecording) {
        currentRecordingPosition = isRecording ? position : -1;
        notifyItemChanged(position);
    }
    
    public void setPlayingTeacherState(int position, boolean isPlaying) {
        int previousPosition = currentPlayingTeacherPosition;
        currentPlayingTeacherPosition = isPlaying ? position : -1;
        
        if (previousPosition != -1 && previousPosition != position) {
            notifyItemChanged(previousPosition);
        }
        notifyItemChanged(position);
    }
    
    public void setPlayingStudentState(int position, boolean isPlaying) {
        int previousPosition = currentPlayingStudentPosition;
        currentPlayingStudentPosition = isPlaying ? position : -1;
        
        if (previousPosition != -1 && previousPosition != position) {
            notifyItemChanged(previousPosition);
        }
        notifyItemChanged(position);
    }
    
    public void stopAllPlayback() {
        int teacherPos = currentPlayingTeacherPosition;
        int studentPos = currentPlayingStudentPosition;
        
        currentPlayingTeacherPosition = -1;
        currentPlayingStudentPosition = -1;
        
        if (teacherPos != -1) {
            notifyItemChanged(teacherPos);
        }
        if (studentPos != -1) {
            notifyItemChanged(studentPos);
        }
    }
    
    class SentenceViewHolder extends RecyclerView.ViewHolder {
        private TextView englishText;
        private TextView banglaText;
        private TextView topicText;
        private TextView tvPracticeCount;
        private Button recordButton;
        private Button playTeacherButton;
        private Button playStudentButton;
        private ImageButton btnFavorite;
        private ImageButton btnEdit;
        private ImageView[] stars = new ImageView[5];
        private LinearLayout layoutMastery;
        
        public SentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            englishText = itemView.findViewById(R.id.text_english);
            banglaText = itemView.findViewById(R.id.text_bangla);
            topicText = itemView.findViewById(R.id.text_topic);
            tvPracticeCount = itemView.findViewById(R.id.tvPracticeCount);
            recordButton = itemView.findViewById(R.id.btn_record);
            playTeacherButton = itemView.findViewById(R.id.btn_play_teacher);
            playStudentButton = itemView.findViewById(R.id.btn_play_student);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            layoutMastery = itemView.findViewById(R.id.layoutMastery);
            
            // Initialize stars
            stars[0] = itemView.findViewById(R.id.star1);
            stars[1] = itemView.findViewById(R.id.star2);
            stars[2] = itemView.findViewById(R.id.star3);
            stars[3] = itemView.findViewById(R.id.star4);
            stars[4] = itemView.findViewById(R.id.star5);
            
            // Set button visibility based on mode
            if (isTeacherMode) {
                playStudentButton.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
            } else {
                playStudentButton.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
            }
            
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onSentenceClick(sentences.get(getAdapterPosition()));
                }
            });
            
            recordButton.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onRecordClick(sentences.get(getAdapterPosition()));
                }
            });
            
            playTeacherButton.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onPlayTeacherClick(sentences.get(getAdapterPosition()));
                }
            });
            
            playStudentButton.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onPlayStudentClick(sentences.get(getAdapterPosition()));
                }
            });
            
            btnFavorite.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Sentence sentence = sentences.get(getAdapterPosition());
                    SentenceProgress progress = progressMap.get(sentence.getId());
                    boolean currentlyFavorite = progress != null && progress.isFavorite();
                    listener.onFavoriteClick(sentence, !currentlyFavorite);
                }
            });
            
            btnEdit.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onEditClick(sentences.get(getAdapterPosition()));
                }
            });
            
            // Set click listeners for stars (manual mastery rating)
            // Clicking a star sets rating to that level
            // Clicking the same star (current level) resets to 0
            for (int i = 0; i < 5; i++) {
                final int starIndex = i;
                final int rating = i + 1;
                stars[i].setOnClickListener(v -> {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        Sentence sentence = sentences.get(getAdapterPosition());
                        SentenceProgress progress = progressMap.get(sentence.getId());
                        int currentMastery = progress != null ? progress.getMasteryLevel() : 0;
                        
                        // If clicking the same star as current level, reset to 0
                        // Otherwise set to the clicked star's rating
                        int newRating = (currentMastery == rating) ? 0 : rating;
                        listener.onMasteryRatingClick(sentence, newRating);
                    }
                });
            }
        }
        
        public void bind(Sentence sentence, int position) {
            englishText.setText(sentence.getEnglish());
            banglaText.setText(sentence.getBangla());
            topicText.setText(sentence.getTopic());
            
            // Get progress for this sentence
            SentenceProgress progress = progressMap.get(sentence.getId());
            
            // Update favorite icon
            if (progress != null && progress.isFavorite()) {
                btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                btnFavorite.setImageResource(R.drawable.ic_favorite_outline);
            }
            
            // Update mastery stars
            int masteryLevel = progress != null ? progress.getMasteryLevel() : 0;
            for (int i = 0; i < 5; i++) {
                if (i < masteryLevel) {
                    stars[i].setImageResource(R.drawable.ic_star_filled);
                } else {
                    stars[i].setImageResource(R.drawable.ic_star_empty);
                }
            }
            
            // Update practice count
            if (progress != null && progress.getPracticeCount() > 0) {
                tvPracticeCount.setVisibility(View.VISIBLE);
                tvPracticeCount.setText("Practiced " + progress.getPracticeCount() + " times");
            } else {
                tvPracticeCount.setVisibility(View.GONE);
            }
            
            // Update record button text and state
            boolean isCurrentlyRecording = currentRecordingPosition == position;
            if (isCurrentlyRecording) {
                recordButton.setText("Stop Recording");
                recordButton.setBackgroundTintList(itemView.getContext().getColorStateList(android.R.color.holo_red_dark));
            } else {
                recordButton.setText(isTeacherMode ? "Record Teacher" : "Record Student");
                recordButton.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.purple_700));
            }
            
            // Update play teacher button text and state
            boolean isCurrentlyPlayingTeacher = currentPlayingTeacherPosition == position;
            if (isCurrentlyPlayingTeacher) {
                playTeacherButton.setText("Stop Teacher");
                playTeacherButton.setBackgroundTintList(itemView.getContext().getColorStateList(android.R.color.holo_red_dark));
            } else {
                playTeacherButton.setText("Play Teacher");
                playTeacherButton.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.teal_700));
            }
            
            // Update play student button text and state
            boolean isCurrentlyPlayingStudent = currentPlayingStudentPosition == position;
            if (isCurrentlyPlayingStudent) {
                playStudentButton.setText("Stop Student");
                playStudentButton.setBackgroundTintList(itemView.getContext().getColorStateList(android.R.color.holo_red_dark));
            } else {
                playStudentButton.setText("Play Student");
                playStudentButton.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.teal_200));
            }
            
            // Enable/disable buttons based on recording availability
            playTeacherButton.setEnabled(sentence.getTeacherRecordingPath() != null);
            if (!playTeacherButton.isEnabled()) {
                playTeacherButton.setAlpha(0.5f);
            } else {
                playTeacherButton.setAlpha(1.0f);
            }
        }
    }
}
