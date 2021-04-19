package com.playbench.winting.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.playbench.winting.Activitys.Login.LoginActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;

public class SplashActivity extends AppCompatActivity {

    private String                  TAG = "SplashActivity";
    private boolean                 mLoginFlag = false;
    private MwSharedPreferences     mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPref = new MwSharedPreferences(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.d(TAG, token);

                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLoginFlag){

                }else{
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    onBackPressed();
                }
            }
        },500);
    }
}