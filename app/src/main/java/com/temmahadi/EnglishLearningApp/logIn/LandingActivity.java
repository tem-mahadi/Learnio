package com.temmahadi.EnglishLearningApp.logIn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.temmahadi.EnglishLearningApp.R;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button registerButton = findViewById(R.id.btnRegisterNow);
        TextView priceText = findViewById(R.id.tvLandingPrice);
        View offerCard = findViewById(R.id.cardOffer);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, MobileNumberActivity.class);
            startActivity(intent);
        });

        startEntranceAnimation(offerCard, registerButton, priceText);
    }

    private void startEntranceAnimation(View offerCard, View registerButton, View priceText) {
        offerCard.setAlpha(0f);
        offerCard.setTranslationY(36f);
        registerButton.setAlpha(0f);
        registerButton.setTranslationY(36f);

        offerCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(550)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        registerButton.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(180)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        priceText.animate()
                .scaleX(1.06f)
                .scaleY(1.06f)
                .setDuration(700)
                .setStartDelay(380)
                .withEndAction(() -> priceText.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(450)
                        .start())
                .start();
    }
}
