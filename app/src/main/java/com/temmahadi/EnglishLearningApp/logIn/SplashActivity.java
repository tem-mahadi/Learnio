package com.temmahadi.EnglishLearningApp.logIn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.temmahadi.EnglishLearningApp.MainActivity;
import com.temmahadi.EnglishLearningApp.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay to show splash screen, then check subscription status
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkSubscriptionAndNavigate();
        }, SPLASH_DELAY);
    }

    private void checkSubscriptionAndNavigate() {
        try {
            if (SubscriptionManager.isSubscribed(this)) {
                // User is already subscribed, go directly to main activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // User is not subscribed, go to mobile number activity for login
                Intent intent = new Intent(SplashActivity.this, MobileNumberActivity.class);
                startActivity(intent);
            }
            finish(); // Close splash activity
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: go to MainActivity if there's any error
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}