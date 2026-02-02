package com.temmahadi.EnglishLearningApp;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.temmahadi.EnglishLearningApp.database.AppDatabase;
import com.temmahadi.EnglishLearningApp.model.Sentence;
import com.temmahadi.EnglishLearningApp.utils.ProgressTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private static final int QUESTIONS_PER_QUIZ = 10;
    
    private TextView tvQuestionNumber, tvQuestion, tvFeedback, tvScore;
    private ProgressBar progressQuiz;
    private LinearLayout optionsContainer;
    private MaterialButton btnNext;
    private CardView cardFeedback;
    
    private List<Sentence> allSentences = new ArrayList<>();
    private List<QuizQuestion> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private boolean answered = false;
    
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.quiz_mode);
        }

        initializeViews();
        loadSentences();
    }

    private void initializeViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvScore = findViewById(R.id.tvScore);
        progressQuiz = findViewById(R.id.progressQuiz);
        optionsContainer = findViewById(R.id.optionsContainer);
        btnNext = findViewById(R.id.btnNext);
        cardFeedback = findViewById(R.id.cardFeedback);

        btnNext.setOnClickListener(v -> nextQuestion());
        cardFeedback.setVisibility(View.GONE);
    }

    private void loadSentences() {
        AppDatabase db = AppDatabase.getDatabase(this);
        db.sentenceDao().getAllSentences().observe(this, sentences -> {
            if (sentences != null && sentences.size() >= 4) {
                allSentences = new ArrayList<>(sentences);
                generateQuiz();
            } else {
                showError("Not enough sentences for quiz");
            }
        });
    }

    private void generateQuiz() {
        questions.clear();
        Random random = new Random();
        
        // Shuffle sentences and pick questions
        List<Sentence> shuffled = new ArrayList<>(allSentences);
        Collections.shuffle(shuffled);
        
        int questionCount = Math.min(QUESTIONS_PER_QUIZ, shuffled.size());
        
        for (int i = 0; i < questionCount; i++) {
            Sentence correctSentence = shuffled.get(i);
            
            // Generate wrong options
            List<String> options = new ArrayList<>();
            options.add(correctSentence.getBangla());
            
            // Get 3 wrong answers
            List<Sentence> wrongOptions = new ArrayList<>(allSentences);
            wrongOptions.remove(correctSentence);
            Collections.shuffle(wrongOptions);
            
            for (int j = 0; j < Math.min(3, wrongOptions.size()); j++) {
                options.add(wrongOptions.get(j).getBangla());
            }
            
            Collections.shuffle(options);
            
            int correctIndex = options.indexOf(correctSentence.getBangla());
            
            QuizQuestion question = new QuizQuestion(
                correctSentence.getId(),
                correctSentence.getEnglish(),
                options,
                correctIndex,
                correctSentence.getBangla()
            );
            
            questions.add(question);
        }
        
        progressQuiz.setMax(questions.size());
        showQuestion();
    }

    private void showQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        answered = false;
        cardFeedback.setVisibility(View.GONE);
        btnNext.setEnabled(false);
        btnNext.setText(R.string.next);

        QuizQuestion question = questions.get(currentQuestionIndex);
        
        tvQuestionNumber.setText(getString(R.string.question) + " " + 
            (currentQuestionIndex + 1) + "/" + questions.size());
        tvQuestion.setText(question.questionText);
        tvScore.setText(getString(R.string.your_score) + ": " + correctAnswers + "/" + questions.size());
        
        ObjectAnimator.ofInt(progressQuiz, "progress", currentQuestionIndex + 1)
            .setDuration(300)
            .start();

        // Create option buttons
        optionsContainer.removeAllViews();
        
        for (int i = 0; i < question.options.size(); i++) {
            Button optionBtn = createOptionButton(question.options.get(i), i);
            optionsContainer.addView(optionBtn);
        }
    }

    private Button createOptionButton(String text, int index) {
        MaterialButton btn = new MaterialButton(this);
        btn.setText(text);
        btn.setTextSize(16);
        btn.setAllCaps(false);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);
        btn.setLayoutParams(params);
        
        // Use theme-aware colors for dark mode support
        btn.setBackgroundColor(getResources().getColor(R.color.card_background));
        btn.setTextColor(getResources().getColor(R.color.text_primary));
        btn.setStrokeColorResource(R.color.primary);
        btn.setStrokeWidth(2);
        btn.setCornerRadius(12);
        btn.setPadding(24, 24, 24, 24);
        
        btn.setOnClickListener(v -> checkAnswer(index, btn));
        
        return btn;
    }

    private void checkAnswer(int selectedIndex, Button selectedBtn) {
        if (answered) return;
        answered = true;

        QuizQuestion question = questions.get(currentQuestionIndex);
        boolean isCorrect = selectedIndex == question.correctIndex;
        
        // Disable all buttons
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            MaterialButton btn = (MaterialButton) optionsContainer.getChildAt(i);
            btn.setEnabled(false);
            
            if (i == question.correctIndex) {
                btn.setBackgroundColor(getResources().getColor(R.color.correct_green));
                btn.setTextColor(getResources().getColor(R.color.white));
            } else if (i == selectedIndex && !isCorrect) {
                btn.setBackgroundColor(getResources().getColor(R.color.error_red));
                btn.setTextColor(getResources().getColor(R.color.white));
            }
        }

        // Show feedback
        cardFeedback.setVisibility(View.VISIBLE);
        if (isCorrect) {
            correctAnswers++;
            tvFeedback.setText("✓ " + getString(R.string.correct));
            tvFeedback.setTextColor(getResources().getColor(R.color.correct_green));
            cardFeedback.setCardBackgroundColor(getResources().getColor(R.color.correct_bg));
            
            // Track progress with actual sentence ID
            ProgressTracker.recordPractice(this, question.sentenceId, false);
        } else {
            tvFeedback.setText("✗ " + getString(R.string.incorrect) + "\n" + 
                getString(R.string.correct) + ": " + question.correctAnswer);
            tvFeedback.setTextColor(getResources().getColor(R.color.error_red));
            cardFeedback.setCardBackgroundColor(getResources().getColor(R.color.error_bg));
        }

        tvScore.setText(getString(R.string.your_score) + ": " + correctAnswers + "/" + questions.size());
        
        btnNext.setEnabled(true);
        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setText(R.string.finish);
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion();
        } else {
            showResults();
        }
    }

    private void showResults() {
        int percentage = (int) ((correctAnswers * 100.0) / questions.size());
        String grade;
        String emoji;
        
        if (percentage >= 90) {
            grade = "Excellent!";
            emoji = "🏆";
        } else if (percentage >= 70) {
            grade = "Great Job!";
            emoji = "⭐";
        } else if (percentage >= 50) {
            grade = "Good Effort!";
            emoji = "👍";
        } else {
            grade = "Keep Practicing!";
            emoji = "💪";
        }

        new MaterialAlertDialogBuilder(this)
            .setTitle(emoji + " " + getString(R.string.quiz_complete))
            .setMessage(grade + "\n\n" +
                getString(R.string.your_score) + ": " + correctAnswers + "/" + questions.size() + 
                " (" + percentage + "%)\n\n" +
                "XP Earned: +" + (correctAnswers * 5))
            .setPositiveButton(R.string.try_again, (d, w) -> {
                currentQuestionIndex = 0;
                correctAnswers = 0;
                generateQuiz();
            })
            .setNegativeButton(R.string.finish, (d, w) -> finish())
            .setCancelable(false)
            .show();
    }

    private void showError(String message) {
        new MaterialAlertDialogBuilder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton(R.string.ok, (d, w) -> finish())
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

    // Inner class for quiz questions
    private static class QuizQuestion {
        int sentenceId;
        String questionText;
        List<String> options;
        int correctIndex;
        String correctAnswer;

        QuizQuestion(int sentenceId, String questionText, List<String> options, int correctIndex, String correctAnswer) {
            this.sentenceId = sentenceId;
            this.questionText = questionText;
            this.options = options;
            this.correctIndex = correctIndex;
            this.correctAnswer = correctAnswer;
        }
    }
}
