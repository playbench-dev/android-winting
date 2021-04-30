package com.playbench.winting.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.LOGIN;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.COMPANY_NAME;
import static com.playbench.winting.Utils.Util.COMPANY_NO;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.LOGIN_FLAG;
import static com.playbench.winting.Utils.Util.PHONE_NUM;
import static com.playbench.winting.Utils.Util.REGION;
import static com.playbench.winting.Utils.Util.USER_ID;
import static com.playbench.winting.Utils.Util.USER_NAME;
import static com.playbench.winting.Utils.Util.USER_NO;
import static com.playbench.winting.Utils.Util.USER_PW;
import static com.playbench.winting.Utils.Util.USER_TOKEN;

public class SplashActivity extends AppCompatActivity implements AsyncResponse {

    private String                  TAG = "SplashActivity";
    private boolean                 mLoginFlag = false;
    private MwSharedPreferences     mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPref = new MwSharedPreferences(this);
        mLoginFlag = mPref.getBooleanValue(LOGIN_FLAG);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        USER_TOKEN = task.getResult();
                        Log.d(TAG, "token : " + USER_TOKEN);
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLoginFlag){
                    NetworkCall(LOGIN);
                }else{
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    onBackPressed();
                }
            }
        },500);
    }

    void NetworkCall(String mCode){
        if (mCode.equals(LOGIN)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_ID),mPref.getStringValue(USER_PW),USER_TOKEN);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(LOGIN)){
                    mPref.setValue(USER_NO,JsonIsNullCheck(jsonArray.getJSONObject(0),USER_NO));
                    mPref.setValue(USER_NAME,JsonIsNullCheck(jsonArray.getJSONObject(0),USER_NAME));
                    mPref.setValue(COMPANY_NO,JsonIsNullCheck(jsonArray.getJSONObject(0),COMPANY_NO));
                    mPref.setValue(COMPANY_NAME,JsonIsNullCheck(jsonArray.getJSONObject(0),COMPANY_NAME));
                    mPref.setValue(PHONE_NUM,JsonIsNullCheck(jsonArray.getJSONObject(0),PHONE_NUM));
                    mPref.setValue(REGION,JsonIsNullCheck(jsonArray.getJSONObject(0),REGION));
                    mPref.setValue(LOGIN_FLAG,true);
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    onBackPressed();
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Login_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }
}