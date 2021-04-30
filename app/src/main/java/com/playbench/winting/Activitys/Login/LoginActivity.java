package com.playbench.winting.Activitys.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.playbench.winting.Activitys.MainActivity;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String TAG                  = "LoginActivity";
    private EditText                    mEditId;
    private EditText                    mEditPw;
    private Button                      mButtonLogin;
    private TextView                    mTextIdSearch;
    private TextView                    mTextPwSearch;
    private TextView                    mTextSignUp;

    //memo 약관동의 팝업
    private View                        bottomSheetDialog_Agree_View;
    private BottomSheetDialog           mAgreeBottomSheetDialog;
    private LinearLayout                mLinAgreeAll;
    private CheckBox                    mChbAgreeAll;
    private CheckBox                    mChbAgreeService;
    private CheckBox                    mChbAgreePersonal;
    private CheckBox                    mChbAgreeSensitivity;
    private Button                      mBtnAgreeDone;
    private TextView                    mTextDetailService;
    private TextView                    mTextDetailPersonal;
    private TextView                    mTextDetailSensitivity;
    private MwSharedPreferences         mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPref                           = new MwSharedPreferences(this);

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

                        Log.d(TAG, USER_TOKEN);

                    }
                });

        FindViewById();

    }

    void FindViewById(){
        mEditId                         = findViewById(R.id.edt_login_id);
        mEditPw                         = findViewById(R.id.edt_login_pw);
        mButtonLogin                    = findViewById(R.id.btn_login_move);
        mTextIdSearch                   = findViewById(R.id.txt_login_id_search);
        mTextPwSearch                   = findViewById(R.id.txt_login_pw_search);
        mTextSignUp                     = findViewById(R.id.txt_login_sign_up);

        mButtonLogin.setOnClickListener(this);
        mTextIdSearch.setOnClickListener(this);
        mTextPwSearch.setOnClickListener(this);
        mTextSignUp.setOnClickListener(this);
    }

    void AgreePopUp(){
        bottomSheetDialog_Agree_View    = getLayoutInflater().inflate(R.layout.agree_dialog,null);
        mAgreeBottomSheetDialog         = new BottomSheetDialog(this,R.style.Theme_Design_BottomSheetDialog);

        mLinAgreeAll                    = bottomSheetDialog_Agree_View.findViewById(R.id.lin_agree_check_all);
        mChbAgreeAll                    = bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_all);
        mChbAgreeService                = bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_service);
        mChbAgreePersonal               = bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_personal);
        mChbAgreeSensitivity            = bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_sensitivity);
        mBtnAgreeDone                   = bottomSheetDialog_Agree_View.findViewById(R.id.btn_agree_done);
        mTextDetailService              = bottomSheetDialog_Agree_View.findViewById(R.id.txt_agree_move_service);
        mTextDetailPersonal             = bottomSheetDialog_Agree_View.findViewById(R.id.txt_agree_move_personal);
        mTextDetailSensitivity          = bottomSheetDialog_Agree_View.findViewById(R.id.txt_agree_move_sensitivity);

        mAgreeBottomSheetDialog.setContentView(bottomSheetDialog_Agree_View);
        mAgreeBottomSheetDialog.setCancelable(false);

        mChbAgreeAll.setOnClickListener(this);
        mChbAgreeService.setOnClickListener(this);
        mChbAgreePersonal.setOnClickListener(this);
        mChbAgreeSensitivity.setOnClickListener(this);
        mBtnAgreeDone.setOnClickListener(this);
        mTextDetailService.setOnClickListener(this);
        mTextDetailPersonal.setOnClickListener(this);
        mTextDetailSensitivity.setOnClickListener(this);

        mAgreeBottomSheetDialog.show();
    }

    void NetworkCall(String mCode){
        if (mCode.equals(LOGIN)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mEditId.getText().toString(),mEditPw.getText().toString(),USER_TOKEN);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(LOGIN)){
                    mPref.setValue(USER_ID,mEditId.getText().toString());
                    mPref.setValue(USER_PW,mEditPw.getText().toString());
                    mPref.setValue(USER_NO,JsonIsNullCheck(jsonArray.getJSONObject(0),USER_NO));
                    mPref.setValue(USER_NAME,JsonIsNullCheck(jsonArray.getJSONObject(0),USER_NAME));
                    mPref.setValue(COMPANY_NO,JsonIsNullCheck(jsonArray.getJSONObject(0),COMPANY_NO));
                    mPref.setValue(COMPANY_NAME,JsonIsNullCheck(jsonArray.getJSONObject(0),COMPANY_NAME));
                    mPref.setValue(PHONE_NUM,JsonIsNullCheck(jsonArray.getJSONObject(0),PHONE_NUM));
                    mPref.setValue(REGION,JsonIsNullCheck(jsonArray.getJSONObject(0),REGION));
                    mPref.setValue(LOGIN_FLAG,true);
                    Intent i = new Intent(this, MainActivity.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_move : {
                if (mEditId.getText().length() > 0 && mEditPw.getText().length() > 0){
                    NetworkCall(LOGIN);
                }
                break;
            }
            case R.id.txt_login_id_search : {
                Intent i = new Intent(this,IdSearchActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_login_pw_search : {
                Intent i = new Intent(this,PwSearchActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_login_sign_up : {
                AgreePopUp();
                break;
            }
            case R.id.chb_agree_all : {
                if (!mChbAgreeAll.isChecked()){
                    mChbAgreeService.setChecked(false);
                    mChbAgreePersonal.setChecked(false);
                    mChbAgreeSensitivity.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisable));
                    }
                    mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorDisableText));
                }else{
                    mChbAgreeService.setChecked(true);
                    mChbAgreePersonal.setChecked(true);
                    mChbAgreeSensitivity.setChecked(true);
                    mBtnAgreeDone.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    }
                    mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            }
            case R.id.chb_agree_service : {
                if (!mChbAgreeService.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisable));
                    }
                    mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorDisableText));
                }else{
                    if (mChbAgreePersonal.isChecked() && mChbAgreeSensitivity.isChecked()){
                        mChbAgreeAll.setChecked(true);
                        mBtnAgreeDone.setEnabled(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        }
                        mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                }
                break;
            }
            case R.id.chb_agree_personal : {
                if (!mChbAgreePersonal.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisable));
                    }
                    mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorDisableText));
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreeSensitivity.isChecked()){
                        mChbAgreeAll.setChecked(true);
                        mBtnAgreeDone.setEnabled(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        }
                        mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                }
                break;
            }
            case R.id.chb_agree_sensitivity : {
                if (!mChbAgreeSensitivity.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisable));
                    }
                    mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorDisableText));
                }else{
                    if (mChbAgreePersonal.isChecked() && mChbAgreeService.isChecked()){
                        mChbAgreeAll.setChecked(true);
                        mBtnAgreeDone.setEnabled(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        }
                        mBtnAgreeDone.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                }
                break;
            }
            case R.id.txt_agree_move_service : {
                Intent i = new Intent(this,AgreeActivity.class);
                i.putExtra("agreeFirst",1);
                startActivity(i);
                break;
            }
            case R.id.txt_agree_move_personal : {
                Intent i = new Intent(this,AgreeActivity.class);
                i.putExtra("agreeFirst",2);
                startActivity(i);
                break;
            }
            case R.id.txt_agree_move_sensitivity : {
                Intent i = new Intent(this,AgreeActivity.class);
                i.putExtra("agreeFirst",3);
                startActivity(i);
                break;
            }
            case R.id.btn_agree_done : {
                mAgreeBottomSheetDialog.dismiss();
                Intent i = new Intent(this,RegisterActivity.class);
                startActivity(i);
                break;
            }
        }
    }
}