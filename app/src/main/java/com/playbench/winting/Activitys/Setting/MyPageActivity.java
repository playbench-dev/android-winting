package com.playbench.winting.Activitys.Setting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.Activitys.Login.RegionSearchActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;
import com.playbench.winting.Utils.TwoButtonDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.playbench.winting.Utils.NetworkUtils.EDIT_USER;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.LOGIN;
import static com.playbench.winting.Utils.NetworkUtils.LOGOUT;
import static com.playbench.winting.Utils.NetworkUtils.REGISTER;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.NetworkUtils.WITHDRAWAL;
import static com.playbench.winting.Utils.Util.COMPANY_NAME;
import static com.playbench.winting.Utils.Util.PHONE_NUM;
import static com.playbench.winting.Utils.Util.REGION;
import static com.playbench.winting.Utils.Util.USER_ID;
import static com.playbench.winting.Utils.Util.USER_NAME;
import static com.playbench.winting.Utils.Util.USER_NO;
import static com.playbench.winting.Utils.Util.USER_PW;
import static com.playbench.winting.Utils.Util.USER_TOKEN;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

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

    private String[]                mCodeList = new String[]{"11", "26", "27", "28", "29", "30", "31", "36", "41", "42", "43", "44", "45", "46", "47", "48", "50"};
    private String[]                mNameList = new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"};
    private String[]                mRegionList;
    private MwSharedPreferences     mPref;
    private ArrayList<String>       mRegionArrayList = new ArrayList<>();
    private String                  SELECT_REGION = "";
    private int                     REGION_CHANGE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mPref                       = new MwSharedPreferences(this);

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

        mTextId.setText(mPref.getStringValue(USER_ID) + " ");
        mTextName.setText(mPref.getStringValue(USER_NAME));
        mTextCompany.setText(mPref.getStringValue(COMPANY_NAME));
        mTextContact.setText(mPref.getStringValue(PHONE_NUM));

        SELECT_REGION = mPref.getStringValue(REGION);
        mRegionList = mPref.getStringValue(REGION).split("/");

        for (int i = 0; i < mRegionList.length; i++){
            mRegionArrayList.add(mRegionList[i]);
        }

        for (int i = 0; i < mCodeList.length; i++){
            if (mRegionArrayList.contains(mCodeList[i])){
                if (mTextRegion.getText().length() == 0){
                    mTextRegion.setText(mNameList[i]);
                }else{
                    mTextRegion.setText(mTextRegion.getText().toString() + "/" + mNameList[i]);
                }
            }
        }

        mImageBack.setOnClickListener(this);
        mTextPwChange.setOnClickListener(this);
        mTextRegion.setOnClickListener(this);
        mButtonInfoChange.setOnClickListener(this);
        mTextLogout.setOnClickListener(this);
        mTextWithdrawal.setOnClickListener(this);
    }

    void NetworkCall(String mCode){
        if (mCode.equals(LOGOUT) || mCode.equals(WITHDRAWAL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
        }else if (mCode.equals(EDIT_USER)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_NO),mPref.getStringValue(USER_PW),SELECT_REGION);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(LOGOUT)){

                }else if (mCode.equals(WITHDRAWAL)){

                }else if (mCode.equals(EDIT_USER)){
                    mPref.setValue(REGION,SELECT_REGION);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Setting_My_Page),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Setting_My_Page),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGION_CHANGE){
            if (resultCode == RESULT_OK){
                mRegionArrayList = new ArrayList<>();
                Log.i(TAG,"regionList : " + data.getStringExtra("regionList"));
                SELECT_REGION = data.getStringExtra("regionList");
                String[] mRegionList = data.getStringExtra("regionList").split("/");
                for (int i = 0; i < mRegionList.length; i++){
                    mRegionArrayList.add(mRegionList[i]);
                }
                String mRegion = "";
                for (int i = 0; i < mCodeList.length; i++){
                    if (mRegionArrayList.contains(mCodeList[i])){
                        if (mRegion.length() == 0){
                            mRegion = mNameList[i];
                        }else{
                            mRegion += "/"+mNameList[i];
                        }
                    }
                }
                mTextRegion.setText(mRegion);
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
            case R.id.txt_my_page_pw_change : {
                Intent intent = new Intent(this,PwChangeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.txt_my_page_region : {
                Intent intent = new Intent(this, RegionSearchActivity.class);
                intent.putExtra("regionList",SELECT_REGION);
                startActivityForResult(intent,REGION_CHANGE);
                break;
            }
            case R.id.btn_my_page_info_change : {
                NetworkCall(EDIT_USER);
                break;
            }
            case R.id.txt_my_page_logout : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new TwoButtonDialog(getString(R.string.Dialog_Logout_Title), getString(R.string.Dialog_Logout_Contents), new TwoButtonDialog.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(View v) {
                        NetworkCall(LOGOUT);
                    }
                }).show(fragmentManager, TAG);
                break;
            }
            case R.id.txt_my_page_withdrawal : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new TwoButtonDialog(getString(R.string.Dialog_Withdrawal_Title), getString(R.string.Dialog_Withdrawal_Contents), new TwoButtonDialog.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(View v) {
                        NetworkCall(WITHDRAWAL);
                    }
                }).show(fragmentManager, TAG);
                break;
            }
        }
    }
}