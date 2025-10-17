package com.temmahadi.tiwilanguageapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.temmahadi.tiwilanguageapp.R;
import com.temmahadi.tiwilanguageapp.model.Sentence;

import java.util.ArrayList;
import java.util.List;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.SentenceViewHolder> {
    
    private List<Sentence> sentences = new ArrayList<>();
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
        private Button recordButton;
        private Button playTeacherButton;
        private Button playStudentButton;
        
        public SentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            englishText = itemView.findViewById(R.id.text_english);
            banglaText = itemView.findViewById(R.id.text_bangla);
            topicText = itemView.findViewById(R.id.text_topic);
            recordButton = itemView.findViewById(R.id.btn_record);
            playTeacherButton = itemView.findViewById(R.id.btn_play_teacher);
            playStudentButton = itemView.findViewById(R.id.btn_play_student);
            
            // Set button visibility based on mode
            if (isTeacherMode) {
                playStudentButton.setVisibility(View.GONE);
            } else {
                playStudentButton.setVisibility(View.VISIBLE);
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
        }
        
        public void bind(Sentence sentence, int position) {
            englishText.setText(sentence.getEnglish());
            banglaText.setText(sentence.getBangla());
            topicText.setText(sentence.getTopic());
            
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
