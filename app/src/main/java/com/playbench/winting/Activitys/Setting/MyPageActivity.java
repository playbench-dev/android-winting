package com.playbench.winting.Activitys.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.Activitys.Login.RegionSearchActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.TwoButtonDialog;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "MyPageActivity";
    private ImageView               mImageBack;
    private TextView                mTextId;
    private TextView                mTextName;
    private TextView                mTextCompany;
    private TextView                mTextContact;
    private TextView                mTextPwChange;
    private TextView                mTextRegion;
    private Button                  mButtonInfoChange;
    private TextView                mTextLogout;
    private TextView                mTextWithdrawal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mTextId                     = findViewById(R.id.txt_my_page_id);
        mTextName                   = findViewById(R.id.txt_my_page_name);
        mTextCompany                = findViewById(R.id.txt_my_page_company);
        mTextContact                = findViewById(R.id.txt_my_page_contact);
        mTextPwChange               = findViewById(R.id.txt_my_page_pw_change);
        mTextRegion                 = findViewById(R.id.txt_my_page_region);
        mButtonInfoChange           = findViewById(R.id.btn_my_page_info_change);
        mTextLogout                 = findViewById(R.id.txt_my_page_logout);
        mTextWithdrawal             = findViewById(R.id.txt_my_page_withdrawal);

        mImageBack.setOnClickListener(this);
        mTextPwChange.setOnClickListener(this);
        mTextRegion.setOnClickListener(this);
        mButtonInfoChange.setOnClickListener(this);
        mTextLogout.setOnClickListener(this);
        mTextWithdrawal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_my_page_pw_change : {
                Intent intent = new Intent(this,PwChangeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.txt_my_page_region : {
                Intent intent = new Intent(this, RegionSearchActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_my_page_info_change : {

                break;
            }
            case R.id.txt_my_page_logout : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new TwoButtonDialog(getString(R.string.Dialog_Logout_Title), getString(R.string.Dialog_Logout_Contents), new TwoButtonDialog.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(View v) {

                    }
                }).show(fragmentManager, TAG);
                break;
            }
            case R.id.txt_my_page_withdrawal : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new TwoButtonDialog(getString(R.string.Dialog_Withdrawal_Title), getString(R.string.Dialog_Withdrawal_Contents), new TwoButtonDialog.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(View v) {

                    }
                }).show(fragmentManager, TAG);
                break;
            }
        }
    }
}