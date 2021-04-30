package com.playbench.winting.Activitys.Estimate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.playbench.winting.Activitys.MainActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;
import com.playbench.winting.Utils.TwoButtonDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.FILM_DELETE;
import static com.playbench.winting.Utils.NetworkUtils.FILM_EDIT;
import static com.playbench.winting.Utils.NetworkUtils.FILM_INSERT;
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

public class FilmInsertActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String                  TAG = "FilmInsertActivity";
    private ImageView               mImageBack;
    private TextView                mTextTitle;
    private ImageView               mImageDelete;
    private RadioButton             mRdoFilm;
    private RadioButton             mRdoSheet;
    private EditText                mEditBrand;
    private EditText                mEditName;
    private LinearLayout            mLinearSpecVisible;
    private EditText                mEditVLT;
    private EditText                mEditUVR;
    private EditText                mEditIRR;
    private EditText                mEditTSER;
    private EditText                mEditGuarantee;
    private Button                  mButtonSave;
    private MwSharedPreferences     mPref;

    private Intent                  beforeIntent;

    //info
    private String                  mFilmNo = "";
    private String                  mFilmType = "1";    //1 - film 2 - sheet
    private String                  mFilmBrand = "";
    private String                  mFilmProduct = "";
    private String                  mFilmVLT = "";
    private String                  mFilmUVR = "";
    private String                  mFilmIRR = "";
    private String                  mFilmTSER = "";
    private String                  mFilmAsDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_insert);

        mPref                       = new MwSharedPreferences(this);
        beforeIntent                = getIntent();

        FindViewById();

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mTextTitle                  = findViewById(R.id.txt_film_insert_title);
        mImageDelete                = findViewById(R.id.img_film_insert_delete);
        mRdoFilm                    = findViewById(R.id.rdo_film_insert_select_film);
        mRdoSheet                   = findViewById(R.id.rdo_film_insert_select_sheet);
        mEditBrand                  = findViewById(R.id.edt_film_insert_brand);
        mEditName                   = findViewById(R.id.edt_film_insert_name);
        mLinearSpecVisible          = findViewById(R.id.lin_film_insert_spec_parent);
        mEditVLT                    = findViewById(R.id.edt_film_insert_vlt);
        mEditUVR                    = findViewById(R.id.edt_film_insert_uvr);
        mEditIRR                    = findViewById(R.id.edt_film_insert_irr);
        mEditTSER                   = findViewById(R.id.edt_film_insert_tser);
        mEditGuarantee              = findViewById(R.id.edt_film_insert_guarantee);
        mButtonSave                 = findViewById(R.id.btn_film_insert_save);

        mImageBack.setOnClickListener(this);
        mRdoFilm.setOnClickListener(this);
        mRdoSheet.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
        mImageDelete.setOnClickListener(this);

        if (beforeIntent.hasExtra("filmNo")){
            mTextTitle.setText(getString(R.string.Film_Edit_Title));
            mButtonSave.setText(getString(R.string.Film_Edit_Button_Text));
            mImageDelete.setVisibility(View.VISIBLE);
            if (beforeIntent.hasExtra("filmType")){
                if (beforeIntent.getStringExtra("filmType").equals("1")){
                    mRdoFilm.performClick();
                }else{
                    mRdoSheet.performClick();
                }
            }
            if (beforeIntent.hasExtra("filmBrand")){
                mEditBrand.setText(beforeIntent.getStringExtra("filmBrand"));
            }
            if (beforeIntent.hasExtra("filmName")){
                mEditName.setText(beforeIntent.getStringExtra("filmName"));
            }
            if (beforeIntent.hasExtra("filmVLT")){
                mEditVLT.setText(beforeIntent.getStringExtra("filmVLT"));
            }
            if (beforeIntent.hasExtra("filmUVR")){
                mEditUVR.setText(beforeIntent.getStringExtra("filmUVR"));
            }
            if (beforeIntent.hasExtra("filmIRR")){
                mEditIRR.setText(beforeIntent.getStringExtra("filmIRR"));
            }
            if (beforeIntent.hasExtra("filmTSER")){
                mEditTSER.setText(beforeIntent.getStringExtra("filmTSER"));
            }
            if (beforeIntent.hasExtra("filmAsDate")){
                mEditGuarantee.setText(beforeIntent.getStringExtra("filmAsDate"));
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(FILM_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_NO),mFilmType,mFilmBrand,mFilmProduct,mFilmVLT,mFilmUVR,mFilmIRR,mFilmTSER,mFilmAsDate);
        }else if (mCode.equals(FILM_EDIT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("filmNo"),mFilmType,mFilmBrand,mFilmProduct,mFilmVLT,mFilmUVR,mFilmIRR,mFilmTSER,mFilmAsDate);
        }else if (mCode.equals(FILM_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("filmNo"));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {

        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(FILM_INSERT)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Film_Insert_Title), jsonObject.getString(ERROR_NM), new OneButtonDialog.ConfirmButtonListener() {
                        @Override
                        public void confirmButton(View v) {
                            setResult(RESULT_OK);
                            onBackPressed();
                        }
                    }).show(fragmentManager,TAG);
                }else if (mCode.equals(FILM_EDIT)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Film_Edit_Title), jsonObject.getString(ERROR_NM), new OneButtonDialog.ConfirmButtonListener() {
                        @Override
                        public void confirmButton(View v) {
                            setResult(RESULT_OK);
                            onBackPressed();
                        }
                    }).show(fragmentManager,TAG);
                }else if (mCode.equals(FILM_DELETE)){
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Film_Insert_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
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
            case R.id.img_film_insert_delete : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new TwoButtonDialog(getString(R.string.Film_Edit_Title), getString(R.string.Film_Edit_Delete), new TwoButtonDialog.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(View v) {
                        NetworkCall(FILM_DELETE);
                    }
                }).show(fragmentManager,TAG);
                break;
            }
            case R.id.rdo_film_insert_select_film : {
                mLinearSpecVisible.setVisibility(View.VISIBLE);
                mRdoFilm.setTextColor(getResources().getColor(R.color.white));
                mRdoSheet.setTextColor(getResources().getColor(R.color.colorTextColor));
                break;
            }
            case R.id.rdo_film_insert_select_sheet : {
                mLinearSpecVisible.setVisibility(View.GONE);
                mRdoFilm.setTextColor(getResources().getColor(R.color.colorTextColor));
                mRdoSheet.setTextColor(getResources().getColor(R.color.white));
                break;
            }
            case R.id.btn_film_insert_save : {
                mFilmType = (mRdoFilm.isChecked() == true) ? "1" : "2";
                mFilmBrand = mEditBrand.getText().toString();
                mFilmProduct = mEditName.getText().toString();
                mFilmVLT = mEditVLT.getText().toString();
                mFilmUVR = mEditUVR.getText().toString();
                mFilmIRR = mEditIRR.getText().toString();
                mFilmTSER = mEditTSER.getText().toString();
                mFilmAsDate = mEditGuarantee.getText().toString();
                if (beforeIntent.hasExtra("filmNo")){
                    NetworkCall(FILM_EDIT);
                }else{
                    NetworkCall(FILM_INSERT);
                }

                break;
            }
        }
    }


}