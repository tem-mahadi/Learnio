package com.temmahadi.tiwilanguageapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.temmahadi.tiwilanguageapp.adapter.SentenceAdapter;
import com.temmahadi.tiwilanguageapp.dao.SentenceDao;
import com.temmahadi.tiwilanguageapp.database.AppDatabase;
import com.temmahadi.tiwilanguageapp.model.Sentence;
import com.temmahadi.tiwilanguageapp.model.StudentRecording;
import com.temmahadi.tiwilanguageapp.repository.AppRepository;
import com.temmahadi.tiwilanguageapp.utils.AudioManager;
import com.temmahadi.tiwilanguageapp.utils.JsonLoader;
import com.temmahadi.tiwilanguageapp.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SentenceAdapter.OnSentenceClickListener {
    
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    
    private MainViewModel viewModel;
    private SentenceAdapter adapter;
    private AudioManager audioManager;
    private boolean isTeacherMode = true;
    private boolean isRecording = false;
    private Sentence currentRecordingSentence;
    private int currentRecordingPosition = -1;
    private int currentPlayingTeacherPosition = -1;
    private int currentPlayingStudentPosition = -1;
    private boolean isStoppingRecording = false;
    
    private Button btnTeacherMode;
    private Button btnStudentMode;
    private TextView modeIndicator;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initializeViews();
        checkPermissions();
        initializeViewModel();
        setupRecyclerView();
        setupModeButtons();
        loadInitialData();
    }
    
    private void initializeViews() {
        btnTeacherMode = findViewById(R.id.btn_teacher_mode);
        btnStudentMode = findViewById(R.id.btn_student_mode);
        modeIndicator = findViewById(R.id.mode_indicator);
        recyclerView = findViewById(R.id.recycler_sentences);
        audioManager = new AudioManager();
        
        // Set up playback completion listener
        audioManager.setOnPlaybackCompleteListener(() -> {
            runOnUiThread(() -> stopCurrentPlayback());
        });
    }
    
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
    
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        viewModel.getAllSentences().observe(this, sentences -> {
            if (sentences != null) {
                adapter.setSentences(sentences);
                if (sentences.isEmpty()) {
                    loadSentencesFromJson();
                }
            }
        });
    }
    
    private void setupRecyclerView() {
        adapter = new SentenceAdapter(isTeacherMode);
        adapter.setOnSentenceClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupModeButtons() {
        updateModeUI();
        
        btnTeacherMode.setOnClickListener(v -> {
            isTeacherMode = true;
            adapter = new SentenceAdapter(isTeacherMode);
            adapter.setOnSentenceClickListener(this);
            recyclerView.setAdapter(adapter);
            updateModeUI();
            // Refresh data
            viewModel.getAllSentences().observe(this, sentences -> {
                if (sentences != null) {
                    adapter.setSentences(sentences);
                }
            });
        });
        
        btnStudentMode.setOnClickListener(v -> {
            isTeacherMode = false;
            adapter = new SentenceAdapter(isTeacherMode);
            adapter.setOnSentenceClickListener(this);
            recyclerView.setAdapter(adapter);
            updateModeUI();
            // Refresh data
            viewModel.getAllSentences().observe(this, sentences -> {
                if (sentences != null) {
                    adapter.setSentences(sentences);
                }
            });
        });
    }
    
    private void updateModeUI() {
        if (isTeacherMode) {
            btnTeacherMode.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_500));
            btnStudentMode.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
            modeIndicator.setText("Current Mode: Teacher");
        } else {
            btnTeacherMode.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
            btnStudentMode.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_500));
            modeIndicator.setText("Current Mode: Student");
        }
    }
    
    private void loadInitialData() {
        // Check if database is empty and load from JSON if needed
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            SentenceDao dao = db.sentenceDao();
            int count = dao.getCount();
            
            if (count == 0) {
                runOnUiThread(this::loadSentencesFromJson);
            }
        });
    }
    
    private void loadSentencesFromJson() {
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            List<Sentence> sentences = JsonLoader.loadSentencesFromAssets(this);
            if (!sentences.isEmpty()) {
                viewModel.insertAll(sentences.toArray(new Sentence[0]));
                runOnUiThread(() -> Toast.makeText(this, 
                    "Loaded " + sentences.size() + " sentences", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onRecordClick(Sentence sentence) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission required to record audio", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Find the position of the sentence
        int position = findSentencePosition(sentence);
        if (position == -1) return;
        
        if (!isRecording) {
            // Stop any current playback before recording
            stopCurrentPlayback();
            startRecording(sentence, position);
        } else {
            stopRecording();
        }
    }
    
    private int findSentencePosition(Sentence sentence) {
        if (viewModel.getAllSentences().getValue() != null) {
            List<Sentence> sentences = viewModel.getAllSentences().getValue();
            for (int i = 0; i < sentences.size(); i++) {
                if (sentences.get(i).getId() == sentence.getId()) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private void startRecording(Sentence sentence, int position) {
        currentRecordingSentence = sentence;
        currentRecordingPosition = position;
        String fileName = (isTeacherMode ? "teacher_" : "student_") + sentence.getId() + "_" + System.currentTimeMillis();
        
        if (audioManager.startRecording(this, fileName)) {
            isRecording = true;
            adapter.setRecordingState(position, true);
            Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show();
            currentRecordingPosition = -1;
        }
    }
    
    private void stopRecording() {
        isStoppingRecording = true;
        String recordingPath = audioManager.stopRecording();
        isRecording = false;
        
        if (recordingPath != null && currentRecordingSentence != null) {
            if (isTeacherMode) {
                // Save teacher recording
                currentRecordingSentence.setTeacherRecordingPath(recordingPath);
                viewModel.update(currentRecordingSentence);
                Toast.makeText(this, "Teacher recording saved", Toast.LENGTH_SHORT).show();
            } else {
                // Save student recording
                StudentRecording studentRecording = new StudentRecording(
                    currentRecordingSentence.getId(),
                    "Student", // You might want to add a way to get student name
                    recordingPath,
                    System.currentTimeMillis()
                );
                viewModel.insert(studentRecording);
                Toast.makeText(this, "Student recording saved", Toast.LENGTH_SHORT).show();
            }
            
            adapter.setRecordingState(currentRecordingPosition, false);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Failed to save recording", Toast.LENGTH_SHORT).show();
            if (currentRecordingPosition != -1) {
                adapter.setRecordingState(currentRecordingPosition, false);
            }
        }
        
        currentRecordingSentence = null;
        currentRecordingPosition = -1;
        
        // Reset the flag after a short delay to ensure UI operations complete
        new android.os.Handler().postDelayed(() -> {
            isStoppingRecording = false;
        }, 500);
    }

    @Override
    public void onPlayTeacherClick(Sentence sentence) {
        // Prevent accidental triggering during recording stop operations
        if (isStoppingRecording) {
            return;
        }
        
        int position = findSentencePosition(sentence);
        if (position == -1) return;
        
        // If currently playing this teacher recording, stop it
        if (currentPlayingTeacherPosition == position) {
            stopCurrentPlayback();
            return;
        }
        
        if (sentence.getTeacherRecordingPath() != null) {
            // Stop any current recording before playing
            if (isRecording) {
                stopRecording();
                return; // Exit to prevent immediate playback after recording stop
            }
            
            // Stop any other playback
            stopCurrentPlayback();
            
            if (audioManager.startPlayback(sentence.getTeacherRecordingPath())) {
                currentPlayingTeacherPosition = position;
                adapter.setPlayingTeacherState(position, true);
                Toast.makeText(this, "Playing teacher recording", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to play teacher recording", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No teacher recording available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPlayStudentClick(Sentence sentence) {
        // Prevent accidental triggering during recording stop operations
        if (isStoppingRecording) {
            return;
        }
        
        int position = findSentencePosition(sentence);
        if (position == -1) return;
        
        // If currently playing this student recording, stop it
        if (currentPlayingStudentPosition == position) {
            stopCurrentPlayback();
            return;
        }
        
        // Stop any current recording before playing
        if (isRecording) {
            stopRecording();
            return; // Exit to prevent immediate playback after recording stop
        }
        
        // Stop any other playback
        stopCurrentPlayback();
        
        // Get the latest student recording for this sentence using background thread
        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            List<StudentRecording> recordings = db.studentRecordingDao()
                .getRecordingsForSentenceSync(sentence.getId());
            
            runOnUiThread(() -> {
                if (recordings != null && !recordings.isEmpty()) {
                    StudentRecording latestRecording = recordings.get(0); // First is latest due to ORDER BY recordingDate DESC
                    
                    if (audioManager.startPlayback(latestRecording.getRecordingPath())) {
                        currentPlayingStudentPosition = position;
                        adapter.setPlayingStudentState(position, true);
                        Toast.makeText(this, "Playing student recording", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to play student recording", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No student recording available", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void stopCurrentPlayback() {
        audioManager.stopPlayback();
        
        if (currentPlayingTeacherPosition != -1) {
            adapter.setPlayingTeacherState(currentPlayingTeacherPosition, false);
            currentPlayingTeacherPosition = -1;
        }
        
        if (currentPlayingStudentPosition != -1) {
            adapter.setPlayingStudentState(currentPlayingStudentPosition, false);
            currentPlayingStudentPosition = -1;
        }
    }

    @Override
    public void onSentenceClick(Sentence sentence) {
        // Optional: Handle sentence click for additional details
        Toast.makeText(this, "Clicked: " + sentence.getEnglish(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRecording) {
            stopRecording();
        }
        stopCurrentPlayback();
        if (audioManager != null) {
            audioManager.release();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Audio recording permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Audio recording permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}