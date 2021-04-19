package com.playbench.winting.Activitys.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "RegisterActivity";
    private ImageView               mImageBack;
    private EditText                mEditId;
    private EditText                mEditPw;
    private EditText                mEditPwConfirm;
    private EditText                mEditCompany;
    private EditText                mEditName;
    private EditText                mEditContact;
    private EditText                mEditRegion;
    private TextView                mTextCheckId;
    private TextView                mTextCheckPw;
    private TextView                mTextCheckPwConfirm;
    private Button                  mButtonDone;

    private String[]                mCodeList = new String[]{"1100000000", "2600000000", "2700000000", "2800000000", "2900000000", "3000000000", "3100000000", "3600000000", "4100000000", "4200000000", "4300000000", "4400000000", "4500000000", "4600000000", "4700000000", "4800000000", "5000000000"};
    private String[]                mNameList = new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"};
    private ArrayList<String>       mRegionArrayList = new ArrayList<>();
    private MwSharedPreferences     mPref;
    private int                     COMPANY_SEARCH_CODE = 1111;
    private int                     REGION_SEARCH_CODE = 2222;
    private String                  SELECT_REGION = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPref = new MwSharedPreferences(this);

        FindViewById();
    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mEditId                     = findViewById(R.id.edt_register_id);
        mEditPw                     = findViewById(R.id.edt_register_pw);
        mEditPwConfirm              = findViewById(R.id.edt_register_pw_confirm);
        mEditCompany                = findViewById(R.id.edt_register_company);
        mEditName                   = findViewById(R.id.edt_register_name);
        mEditContact                = findViewById(R.id.edt_register_contact);
        mEditRegion                 = findViewById(R.id.edt_register_region);
        mTextCheckId                = findViewById(R.id.txt_register_id_check);
        mTextCheckPw                = findViewById(R.id.txt_register_pw_check);
        mTextCheckPwConfirm         = findViewById(R.id.txt_register_pw_confirm_check);
        mButtonDone                 = findViewById(R.id.btn_register_done);

        mImageBack.setOnClickListener(this);
        mEditCompany.setOnClickListener(this);
        mEditRegion.setOnClickListener(this);
        mButtonDone.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMPANY_SEARCH_CODE) {
            if (resultCode == RESULT_OK) {

            }
        }else if (requestCode == REGION_SEARCH_CODE){
            if (resultCode == RESULT_OK){
                mRegionArrayList = new ArrayList<>();
                SELECT_REGION = data.getStringExtra("regionList");
                String[] mRegionList = data.getStringExtra("regionList").split(",");
                for (int i = 0; i < mRegionList.length; i++){
                    mRegionArrayList.add(mRegionList[i]);
                }
                String mRegion = "";
                for (int i = 0; i < mCodeList.length; i++){
                    if (mRegionArrayList.contains(mCodeList[i])){
                        if (mRegion.length() == 0){
                            mRegion = mNameList[i];
                        }else{
                            mRegion += ","+mNameList[i];
                        }
                    }
                }
                mEditRegion.setText(mRegion);
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
            case R.id.edt_register_company : {
                Intent i = new Intent(this,CompanySearchActivity.class);
                startActivityForResult(i,COMPANY_SEARCH_CODE);
                break;
            }
            case R.id.edt_register_region : {
                Intent i = new Intent(this,RegionSearchActivity.class);
                i.putExtra("regionList",SELECT_REGION);
                startActivityForResult(i,REGION_SEARCH_CODE);
                break;
            }
            case R.id.btn_register_done : {
                //register request
                break;
            }
        }
    }
}