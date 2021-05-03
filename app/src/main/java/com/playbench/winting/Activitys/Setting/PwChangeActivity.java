package com.playbench.winting.Activitys.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.Activitys.Login.LoginActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.playbench.winting.Utils.NetworkUtils.EDIT_USER;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.LOGIN;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.NetworkUtils.WITHDRAWAL;
import static com.playbench.winting.Utils.Util.LOGIN_FLAG;
import static com.playbench.winting.Utils.Util.REGION;
import static com.playbench.winting.Utils.Util.USER_ID;
import static com.playbench.winting.Utils.Util.USER_NO;
import static com.playbench.winting.Utils.Util.USER_PW;

public class PwChangeActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String                  TAG = "PwChangeActivity";
    private ImageView               mImageBack;
    private EditText                mEditCurrent;
    private TextView                mTextCurrentHint;
    private EditText                mEditNew;
    private TextView                mTextNewHint;
    private EditText                mEditNewConfirm;
    private TextView                mTextNewConfirmHint;
    private Button                  mButtonFinish;

    private boolean                 mCurrentPwCheck = false;
    private boolean                 mChangePwCheck = false;

    private MwSharedPreferences     mPref;
    //비밀번호 패턴 영숫자 6자 이상
    private String                  mPattern = "^[a-zA-Z0-9].{5,20}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_change);

        mPref                       = new MwSharedPreferences(this);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mEditCurrent                = findViewById(R.id.edt_pw_change_current);
        mEditNew                    = findViewById(R.id.edt_pw_change_new);
        mEditNewConfirm             = findViewById(R.id.edt_pw_change_new_confirm);
        mTextCurrentHint            = findViewById(R.id.txt_pw_change_current_hint);
        mTextNewHint                = findViewById(R.id.txt_pw_change_new_hint);
        mTextNewConfirmHint         = findViewById(R.id.txt_pw_change_new_confirm_hint);
        mButtonFinish               = findViewById(R.id.btn_pw_change_finish);

        mTextCurrentHint.setVisibility(View.GONE);
        mTextNewHint.setVisibility(View.GONE);
        mTextNewConfirmHint.setVisibility(View.GONE);

        mButtonFinish.setEnabled(false);

        mImageBack.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);

        mEditCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mPref.getStringValue(USER_PW).equals(mEditCurrent.getText().toString())){
                    mCurrentPwCheck = true;
                    mTextCurrentHint.setVisibility(View.GONE);
                }else{
                    mCurrentPwCheck = false;
                    mTextCurrentHint.setVisibility(View.VISIBLE);
                    mTextCurrentHint.setText(getString(R.string.Pw_Change_Current_Pw_Hint_Text));
                }
                if (mEditCurrent.getText().length() > 0 && mEditNew.getText().length() > 0 && mEditNewConfirm.getText().length() > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mButtonFinish.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    }
                    mButtonFinish.setTextColor(getResources().getColor(R.color.white));
                    mButtonFinish.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditNew.getText().length() > 0){
                    if (Pattern.matches(mPattern,mEditNew.getText().toString())){
                        mTextNewHint.setVisibility(View.VISIBLE);
                        mTextNewHint.setText(getString(R.string.Pw_Change_New_Pw_Hint_Complete_Text));
                        mTextNewHint.setTextColor(getResources().getColor(R.color.colorPrimary));
                        if (mEditNew.getText().toString().equals(mEditNewConfirm.getText().toString())){
                            mChangePwCheck = true;

                        }
                    }else{
                        mChangePwCheck = false;
                        mTextNewHint.setVisibility(View.VISIBLE);
                        mTextNewHint.setText(getString(R.string.Pw_Change_New_Pw_Hint_Error_Text));
                        mTextNewHint.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                    }
                }
                if (mEditCurrent.getText().length() > 0 && mEditNew.getText().length() > 0 && mEditNewConfirm.getText().length() > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mButtonFinish.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    }
                    mButtonFinish.setTextColor(getResources().getColor(R.color.white));
                    mButtonFinish.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditNewConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditNewConfirm.getText().toString().equals(mEditNew.getText().toString())){
                    mChangePwCheck = true;
                    mTextNewConfirmHint.setVisibility(View.VISIBLE);
                    mTextNewConfirmHint.setText(getString(R.string.Pw_Change_New_Confirm_Pw_Hint_Complete_Text));
                    mTextNewConfirmHint.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else{
                    mChangePwCheck = false;
                    mTextNewConfirmHint.setVisibility(View.VISIBLE);
                    mTextNewConfirmHint.setText(getString(R.string.Pw_Change_New_Confirm_Pw_Hint_Error_Text));
                    mTextNewConfirmHint.setTextColor(getResources().getColor(R.color.colorOrangeRed));
                }
                if (mEditCurrent.getText().length() > 0 && mEditNew.getText().length() > 0 && mEditNewConfirm.getText().length() > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mButtonFinish.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    }
                    mButtonFinish.setTextColor(getResources().getColor(R.color.white));
                    mButtonFinish.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_pw_change_finish : {
                if (mCurrentPwCheck && mChangePwCheck){
                    NetworkCall(EDIT_USER);
                }
                break;
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(EDIT_USER)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_NO),mEditNew.getText().toString(),mPref.getStringValue(REGION));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(EDIT_USER)){
                    mPref.setValue(USER_PW,mEditNew.getText().toString());
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Pw_Change_Title), getString(R.string.Pw_Change_Popup_Text), new OneButtonDialog.ConfirmButtonListener() {
                        @Override
                        public void confirmButton(View v) {
                            onBackPressed();
                        }
                    }).show(fragmentManager,TAG);
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Pw_Change_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }
}