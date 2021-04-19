package com.playbench.winting.Activitys.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.R;

public class PwChangeActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "PwChangeActivity";
    private ImageView               mImageBack;
    private EditText                mEditCurrent;
    private TextView                mTextCurrentHint;
    private EditText                mEditNew;
    private TextView                mTextNewHint;
    private EditText                mEditNewConfirm;
    private TextView                mTextNewConfirmHint;
    private Button                  mButtonFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_change);

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

        mImageBack.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_pw_change_finish : {

                break;
            }
        }
    }
}