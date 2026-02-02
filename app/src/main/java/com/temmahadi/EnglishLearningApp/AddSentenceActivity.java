package com.temmahadi.EnglishLearningApp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.Sentence;
import com.temmahadi.EnglishLearningApp.repository.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class AddSentenceActivity extends AppCompatActivity {

    public static final String EXTRA_SENTENCE_ID = "extra_sentence_id";
    public static final String EXTRA_SENTENCE_ENGLISH = "extra_sentence_english";
    public static final String EXTRA_SENTENCE_BANGLA = "extra_sentence_bangla";
    public static final String EXTRA_SENTENCE_TOPIC = "extra_sentence_topic";

    private TextInputEditText etEnglish, etBangla;
    private AutoCompleteTextView actvTopic;
    private TextInputLayout tilEnglish, tilBangla, tilTopic;
    private MaterialButton btnSave, btnClear;
    private TextView tvHeader, tvDescription;
    
    private List<String> existingTopics = new ArrayList<>();
    private boolean isEditMode = false;
    private int editSentenceId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence);

        if (getIntent().hasExtra(EXTRA_SENTENCE_ID)) {
            isEditMode = true;
            editSentenceId = getIntent().getIntExtra(EXTRA_SENTENCE_ID, -1);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? R.string.edit_sentence : R.string.add_sentence);
        }

        initializeViews();
        loadExistingTopics();
        setupListeners();
        
        if (isEditMode) {
            populateEditFields();
        }
    }

    private void initializeViews() {
        etEnglish = findViewById(R.id.etEnglish);
        etBangla = findViewById(R.id.etBangla);
        actvTopic = findViewById(R.id.actvTopic);
        tilEnglish = findViewById(R.id.tilEnglish);
        tilBangla = findViewById(R.id.tilBangla);
        tilTopic = findViewById(R.id.tilTopic);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        
        tvHeader = findViewById(R.id.tvHeader);
        tvDescription = findViewById(R.id.tvDescription);
        
        if (tvHeader != null && isEditMode) {
            tvHeader.setText(R.string.edit_sentence);
        }
        if (tvDescription != null && isEditMode) {
            tvDescription.setText(R.string.edit_sentence_description);
        }
        
        if (isEditMode) {
            btnSave.setText(R.string.save_changes);
        }
    }
    
    private void populateEditFields() {
        String english = getIntent().getStringExtra(EXTRA_SENTENCE_ENGLISH);
        String bangla = getIntent().getStringExtra(EXTRA_SENTENCE_BANGLA);
        String topic = getIntent().getStringExtra(EXTRA_SENTENCE_TOPIC);
        
        if (english != null) etEnglish.setText(english);
        if (bangla != null) etBangla.setText(bangla);
        if (topic != null) actvTopic.setText(topic);
    }

    private void loadExistingTopics() {
        AppDatabase db = AppDatabase.getDatabase(this);
        db.sentenceDao().getAllTopics().observe(this, topics -> {
            if (topics != null) {
                existingTopics = new ArrayList<>(topics);
                if (existingTopics.isEmpty()) {
                    existingTopics.add("Greetings");
                    existingTopics.add("Introduction");
                    existingTopics.add("Daily Life");
                    existingTopics.add("Shopping");
                    existingTopics.add("Travel");
                    existingTopics.add("Food");
                    existingTopics.add("Weather");
                    existingTopics.add("Family");
                    existingTopics.add("Work");
                    existingTopics.add("Health");
                }
                
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, 
                    android.R.layout.simple_dropdown_item_1line, 
                    existingTopics
                );
                actvTopic.setAdapter(adapter);
            }
        });
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveSentence());
        btnClear.setOnClickListener(v -> clearFields());
    }

    private void saveSentence() {
        tilEnglish.setError(null);
        tilBangla.setError(null);
        tilTopic.setError(null);

        String english = etEnglish.getText() != null ? etEnglish.getText().toString().trim() : "";
        String bangla = etBangla.getText() != null ? etBangla.getText().toString().trim() : "";
        String topic = actvTopic.getText() != null ? actvTopic.getText().toString().trim() : "";

        boolean isValid = true;

        if (TextUtils.isEmpty(english)) {
            tilEnglish.setError(getString(R.string.field_required));
            isValid = false;
        }

        if (TextUtils.isEmpty(bangla)) {
            tilBangla.setError(getString(R.string.field_required));
            isValid = false;
        }

        if (TextUtils.isEmpty(topic)) {
            tilTopic.setError(getString(R.string.field_required));
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        AppRepository.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            
            if (isEditMode && editSentenceId != -1) {
                Sentence sentence = new Sentence(english, bangla, topic);
                sentence.setId(editSentenceId);
                db.sentenceDao().update(sentence);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.sentence_updated, Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                Sentence sentence = new Sentence(english, bangla, topic);
                db.sentenceDao().insert(sentence);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.sentence_saved, Toast.LENGTH_SHORT).show();
                    clearFields();
                });
            }
        });
    }

    private void clearFields() {
        etEnglish.setText("");
        etBangla.setText("");
        actvTopic.setText("");
        tilEnglish.setError(null);
        tilBangla.setError(null);
        tilTopic.setError(null);
        etEnglish.requestFocus();
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
