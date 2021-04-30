package com.playbench.winting.Activitys.Login;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.playbench.winting.Utils.NetworkUtils.COMPANY_SEARCH;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.LOGIN;
import static com.playbench.winting.Utils.NetworkUtils.OVERLAP;
import static com.playbench.winting.Utils.NetworkUtils.REGISTER;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.USER_TOKEN;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse , TextWatcher{

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

    private String[]                mCodeList = new String[]{"11", "26", "27", "28", "29", "30", "31", "36", "41", "42", "43", "44", "45", "46", "47", "48", "50"};
    private String[]                mNameList = new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"};
    private ArrayList<String>       mRegionArrayList = new ArrayList<>();
    private MwSharedPreferences     mPref;
    private int                     COMPANY_SEARCH_CODE = 1111;
    private int                     REGION_SEARCH_CODE = 2222;
    private String                  SELECT_REGION = "";
    private String                  COMPANY_NO = "";
    private boolean                 mPwCheck = false;
    private boolean                 mIdCheck = false;

    //비밀번호 패턴 영숫자 6자 이상
    private String                  mPattern = "^[a-zA-Z0-9].{5,20}$";

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

        mEditId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    NetworkCall(OVERLAP);
                }
            }
        });

        mEditPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTextCheckPw.setVisibility(View.VISIBLE);
                ButtonChange();
                if (charSequence.length() == 0){
                    mPwCheck = false;
                    mTextCheckPw.setText(getString(R.string.Register_Pw_Check_False));
                    mTextCheckPw.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                }else{
                    if (Pattern.matches(mPattern,mEditPw.getText().toString())){
                        mTextCheckPw.setText(getString(R.string.Register_Pw_Check_True));
                        mTextCheckPw.setTextColor(getResources().getColor(R.color.colorPrimary));
                        if (mEditPw.getText().toString().equals(mEditPwConfirm.getText().toString())){
                            mPwCheck = true;
                            mTextCheckPwConfirm.setText(getString(R.string.Register_Pw_Confirm_Check_True));
                            mTextCheckPwConfirm.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }else{
                            mPwCheck = false;
                            mTextCheckPwConfirm.setText(getString(R.string.Register_Pw_Confirm_Check_False));
                            mTextCheckPwConfirm.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                        }
                    }else{
                        mTextCheckPw.setText(getString(R.string.Register_Pw_Check_False));
                        mTextCheckPw.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditPwConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTextCheckPwConfirm.setVisibility(View.VISIBLE);
                ButtonChange();
                if (charSequence.length() == 0){
                    mPwCheck = false;
                    mTextCheckPwConfirm.setText(getString(R.string.Register_Pw_Confirm_Check_False));
                    mTextCheckPwConfirm.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                }else{
                    if (Pattern.matches(mPattern,mEditPwConfirm.getText().toString())){
                        mTextCheckPwConfirm.setText(getString(R.string.Register_Pw_Confirm_Check_True));
                        mTextCheckPwConfirm.setTextColor(getResources().getColor(R.color.colorPrimary));
                        if (mEditPwConfirm.getText().toString().equals(mEditPw.getText().toString())){
                            mPwCheck = true;
                        }else{
                            mPwCheck = false;
                        }
                    }else{
                        mTextCheckPwConfirm.setText(getString(R.string.Register_Pw_Confirm_Check_False));
                        mTextCheckPwConfirm.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditId.addTextChangedListener(this);
        mEditCompany.addTextChangedListener(this);
        mEditName.addTextChangedListener(this);
        mEditContact.addTextChangedListener(this);
        mEditRegion.addTextChangedListener(this);
    }

    void RegisterCheck(){
        if (!mIdCheck){
            Toast.makeText(this, "아이디를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else if (!mPwCheck){
            Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else if (COMPANY_NO.length() == 0){
            Toast.makeText(this, "업체를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else if (mEditName.getText().length() == 0){
            Toast.makeText(this, "이름을 작성해주세요.", Toast.LENGTH_SHORT).show();
        }else if (mEditContact.getText().length() == 0){
            Toast.makeText(this, "연락처를 작성해주세요.", Toast.LENGTH_SHORT).show();
        }else if (SELECT_REGION.length() == 0){
            Toast.makeText(this, "활동지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            NetworkCall(REGISTER);
        }
    }

    void ButtonChange(){
        if (mEditId.getText().length() > 0 && mEditPw.getText().length() > 0 && mEditPwConfirm.getText().length() > 0 && COMPANY_NO.length() > 0 && mEditName.getText().length() > 0 && mEditContact.getText().length() > 0 && SELECT_REGION.length() > 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mButtonDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
            }
            mButtonDone.setTextColor(getResources().getColor(R.color.white));
            mButtonDone.setEnabled(true);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mButtonDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisable));
            }
            mButtonDone.setTextColor(getResources().getColor(R.color.colorDisableText));
            mButtonDone.setEnabled(false);
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(REGISTER)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mEditId.getText().toString(),mEditPw.getText().toString(),
                    COMPANY_NO,mEditName.getText().toString(),mEditContact.getText().toString(),SELECT_REGION,USER_TOKEN);
        }else if (mCode.equals(OVERLAP)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mEditId.getText().toString());
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (mCode.equals(REGISTER)){
                if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                    JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Register_Success_Title), getString(R.string.Register_Success_Contents), new OneButtonDialog.ConfirmButtonListener() {
                        @Override
                        public void confirmButton(View v) {
                            onBackPressed();
                        }
                    }).show(fragmentManager,TAG);

                }else{
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Register_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
                }
            }else if (mCode.equals(OVERLAP)){
                mTextCheckId.setVisibility(View.VISIBLE);
                if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                    mIdCheck = false;
                    mTextCheckId.setText(getString(R.string.Register_Id_Check_False));
                    mTextCheckId.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                }else{
                    mIdCheck = true;
                    mTextCheckId.setText(getString(R.string.Register_Id_Check_True));
                    mTextCheckId.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

        }catch (JSONException e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMPANY_SEARCH_CODE) {
            if (resultCode == RESULT_OK) {
                COMPANY_NO = data.getStringExtra("companyNo");
                mEditCompany.setText(data.getStringExtra("companyName"));
            }
        }else if (requestCode == REGION_SEARCH_CODE){
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
                RegisterCheck();
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        ButtonChange();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}