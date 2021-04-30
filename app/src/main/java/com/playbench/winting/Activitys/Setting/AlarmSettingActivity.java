package com.playbench.winting.Activitys.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.playbench.winting.Utils.NetworkUtils.ALARM_INFO;
import static com.playbench.winting.Utils.NetworkUtils.ALARM_SETTING;
import static com.playbench.winting.Utils.NetworkUtils.EDIT_USER;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.LOGOUT;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.NetworkUtils.WITHDRAWAL;
import static com.playbench.winting.Utils.Util.REGION;
import static com.playbench.winting.Utils.Util.USER_NO;

public class AlarmSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AsyncResponse {

    private String                  TAG = "AlarmSettingActivity";
    private ImageView               mImageBack;
    private Switch                  mSwitchNew;
    private Switch                  mSwitchProgress;
    private MwSharedPreferences     mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        mPref                       = new MwSharedPreferences(this);

        FindViewById();

        NetworkCall(ALARM_INFO);
    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mSwitchNew                  = findViewById(R.id.swh_alarm_setting_new);
        mSwitchProgress             = findViewById(R.id.swh_alarm_setting_progress);

        mImageBack.setOnClickListener(this);
        mSwitchNew.setOnCheckedChangeListener(this);
        mSwitchProgress.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.swh_alarm_setting_new : {
                if (b){
                    NetworkCall(ALARM_SETTING);
                }else{
                    NetworkCall(ALARM_SETTING);
                }
                break;
            }
            case R.id.swh_alarm_setting_progress : {
                if (b){
                    NetworkCall(ALARM_SETTING);
                }else{
                    NetworkCall(ALARM_SETTING);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(ALARM_INFO)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_NO));
        }else if (mCode.equals(ALARM_SETTING)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_NO),mSwitchNew.isChecked() == true ? "1" : "2",mSwitchProgress.isChecked() == true ? "1" : "2");
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ALARM_INFO)){

                }else if (mCode.equals(ALARM_SETTING)){

                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Alarm_Setting_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }
}