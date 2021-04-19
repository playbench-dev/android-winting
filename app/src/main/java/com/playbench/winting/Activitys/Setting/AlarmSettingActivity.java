package com.playbench.winting.Activitys.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.playbench.winting.R;

public class AlarmSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private String                  TAG = "AlarmSettingActivity";
    private ImageView               mImageBack;
    private Switch                  mSwitchNew;
    private Switch                  mSwitchProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        FindViewById();

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

                }else{

                }
                break;
            }
            case R.id.swh_alarm_setting_progress : {
                if (b){

                }else{

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
}