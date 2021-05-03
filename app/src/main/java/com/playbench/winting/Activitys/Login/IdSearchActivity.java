package com.playbench.winting.Activitys.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.playbench.winting.R;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.ID_SEARCH;
import static com.playbench.winting.Utils.NetworkUtils.OVERLAP;
import static com.playbench.winting.Utils.NetworkUtils.REGISTER;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;

public class IdSearchActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String                      TAG = "IdSearchActivity";
    private ImageView                   mImageBack;
    private EditText                    mEditName;
    private EditText                    mEditPhone;
    private Button                      mButtonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_search);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                      = findViewById(R.id.img_back);
        mEditName                       = findViewById(R.id.edt_id_search_name);
        mEditPhone                      = findViewById(R.id.edt_id_search_phone);
        mButtonDone                     = findViewById(R.id.btn_id_search_done);

        mImageBack.setOnClickListener(this);
        mButtonDone.setOnClickListener(this);

        mEditName.addTextChangedListener(textWatcher);
        mEditPhone.addTextChangedListener(textWatcher);

        mButtonDone.setEnabled(false);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mEditName.getText().length() > 0 && mEditPhone.getText().length() > 0){
                mButtonDone.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mButtonDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                }
                mButtonDone.setTextColor(getResources().getColor(R.color.white));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    void NetworkCall(String mCode){
        if (mCode.equals(ID_SEARCH)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mEditName.getText().toString(),mEditPhone.getText().toString());
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (mCode.equals(ID_SEARCH)){
                if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                    JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Id_Search_Title), "고객님의 아이디는\n" + jsonArray.getJSONObject(0).getString("user_id") + " 입니다.", new OneButtonDialog.ConfirmButtonListener() {
                        @Override
                        public void confirmButton(View v) {
                            onBackPressed();
                        }
                    }).show(fragmentManager,TAG);
                }else{
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Id_Search_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
                }
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_id_search_done : {
                NetworkCall(ID_SEARCH);
                break;
            }
        }
    }
}