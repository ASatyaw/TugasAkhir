package com.example.surveimy.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.surveimy.App;
import com.example.surveimy.MainActivity;
import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivitySplashBinding;
import com.example.surveimy.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        ObjectAnimator logo = ObjectAnimator.ofFloat(binding.imglogo, View.TRANSLATION_Y, -150f, 0).setDuration(3000);
        ObjectAnimator objTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(1000);
        ObjectAnimator objVersion = ObjectAnimator.ofFloat(binding.tvVersion, View.TRANSLATION_Y, 150f, 0).setDuration(1000);
        logo.setInterpolator(new BounceInterpolator());
        logo.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {



                Intent intent;
                if(App.getInstance().isLogin()){
                    intent= new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    intent= new Intent(SplashActivity.this, LoginActivity.class);
                }
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
        logo.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(objTitle, objVersion);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();
    }

}