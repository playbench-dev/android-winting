package com.playbench.winting.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.playbench.winting.Activitys.Login.AgreeActivity;
import com.playbench.winting.Activitys.Setting.AlarmSettingActivity;
import com.playbench.winting.Activitys.Setting.LicenseActivity;
import com.playbench.winting.Activitys.Setting.MyPageActivity;
import com.playbench.winting.R;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private String              TAG = "SettingFragment";
    private LinearLayout        mLinearMyPage;
    private LinearLayout        mLinearAlarm;
    private LinearLayout        mLinearAgree;
    private LinearLayout        mLinearLicense;

    public SettingFragment (){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v                  = inflater.inflate(R.layout.fragment_setting, container, false);
        mLinearMyPage           = v.findViewById(R.id.lin_setting_my_page);
        mLinearAlarm            = v.findViewById(R.id.lin_setting_alarm);
        mLinearAgree            = v.findViewById(R.id.lin_setting_agree);
        mLinearLicense          = v.findViewById(R.id.lin_setting_license);

        mLinearMyPage.setOnClickListener(this);
        mLinearAlarm.setOnClickListener(this);
        mLinearAgree.setOnClickListener(this);
        mLinearLicense.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lin_setting_my_page : {
                Intent intent = new Intent(getActivity(), MyPageActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_setting_alarm : {
                Intent intent = new Intent(getActivity(), AlarmSettingActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_setting_agree : {
                Intent intent = new Intent(getActivity(), AgreeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_setting_license : {
                Intent intent = new Intent(getActivity(), LicenseActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
