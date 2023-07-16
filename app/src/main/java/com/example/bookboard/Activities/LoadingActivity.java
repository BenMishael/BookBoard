package com.example.bookboard.Activities;

import static android.content.ContentValues.TAG;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bookboard.R;

public class LoadingActivity extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;
    private LottieAnimationView lottieAnimationLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        findViews();

        lottieAnimationView.playAnimation();
        lottieAnimationLogo.playAnimation();

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG, "onAnimationEnd: "+R.raw.lottie_loadingcircle_end);
                lottieAnimationLogo.setAnimation(R.raw.lottie_loadingcircle_end);
                lottieAnimationLogo.playAnimation();
                while(lottieAnimationLogo.isAnimating());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    private void findViews() {
        lottieAnimationLogo = findViewById(R.id.animation_view_logo);
        lottieAnimationView = findViewById(R.id.animation_view);
    }

}
