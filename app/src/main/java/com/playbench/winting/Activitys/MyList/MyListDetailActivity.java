package com.playbench.winting.Activitys.MyList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.DatePickerDialogActivity;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;
import com.playbench.winting.Utils.TwoButtonDialog;
import com.playbench.winting.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.playbench.winting.Utils.NetworkUtils.ANTECEDENTS_DETAIL;
import static com.playbench.winting.Utils.NetworkUtils.ANTECEDENTS_DONE;
import static com.playbench.winting.Utils.NetworkUtils.ANTECEDENTS_INSERT;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_DETAIL;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.GetFormatDEC;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_ID;

public class MyListDetailActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String                  TAG = "MyListDetailActivity";
    private ImageView               mImageBack;
    private TextView                mTextStartDt;
    private TextView                mTextEndDt;
    private TextView                mTextPrice;
    private Button                  mButtonEstimateDetail;
    private Button                  mButtonImageSave;
    private Button                  mButtonFinish;
    private MwSharedPreferences     mPref;

    private SimpleDateFormat        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_detail);

        mPref                       = new MwSharedPreferences(this);

        FindViewById();

        NetworkCall(ANTECEDENTS_DETAIL);
    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mTextStartDt                = findViewById(R.id.txt_my_list_detail_start_dt);
        mTextEndDt                  = findViewById(R.id.txt_my_list_detail_end_dt);
        mTextPrice                  = findViewById(R.id.txt_my_list_detail_price);
        mButtonEstimateDetail       = findViewById(R.id.btn_my_list_detail_estimate_move);
        mButtonImageSave            = findViewById(R.id.btn_my_list_detail_img_save);
        mButtonFinish               = findViewById(R.id.btn_my_list_detail_finish);

        mImageBack.setOnClickListener(this);
        mTextStartDt.setOnClickListener(this);
        mTextEndDt.setOnClickListener(this);
        mButtonEstimateDetail.setOnClickListener(this);
        mButtonImageSave.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);

    }

    void NetworkCall(String mCode){
        if (mCode.equals(ANTECEDENTS_DETAIL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(getIntent().getStringExtra("estimateNo"));
        }else if (mCode.equals(ANTECEDENTS_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_ID),getIntent().getStringExtra("estimateNo")
                    ,mTextStartDt.getText().toString() + " " + simpleDateFormat.format(new Date(System.currentTimeMillis())),mTextEndDt.getText().toString() + " " + simpleDateFormat.format(new Date(System.currentTimeMillis())));
        }else if (mCode.equals(ANTECEDENTS_DONE)){

        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)) {
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ANTECEDENTS_DETAIL)) {
                    mTextStartDt.setText(jsonArray.getJSONObject(0).getString("start_date").substring(0,10));
                    mTextEndDt.setText(jsonArray.getJSONObject(0).getString("end_date").substring(0,10));
                    mTextPrice.setText(GetFormatDEC(JsonIsNullCheck(jsonArray.getJSONObject(0),"price")));
                }else if (mCode.equals(ANTECEDENTS_INSERT)){

                }else if (mCode.equals(ANTECEDENTS_DONE)){

                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.My_List_Detail_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
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
            case R.id.txt_my_list_detail_start_dt : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new DatePickerDialogActivity( new DatePickerDialogActivity.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(String data) {
                        if (mTextEndDt.getText().length() > 0){
                            if (mTextEndDt.getText().toString().equals("0000-00-00")){
                                mTextStartDt.setText(data);
                                NetworkCall(ANTECEDENTS_INSERT);
                            }else{
                                if (Integer.parseInt(mTextEndDt.getText().toString().replace("-","")) < Integer.parseInt(data.replace("-",""))){
                                    Toast.makeText(MyListDetailActivity.this, "종료일이 시작일보다 이전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }else{
                                    mTextStartDt.setText(data);
                                    NetworkCall(ANTECEDENTS_INSERT);
                                }
                            }
                        }else{
                            mTextStartDt.setText(data);
                            NetworkCall(ANTECEDENTS_INSERT);
                        }
                    }
                }).show(fragmentManager, "test");
                break;
            }
            case R.id.txt_my_list_detail_end_dt : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new DatePickerDialogActivity( new DatePickerDialogActivity.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(String data) {
                        if (mTextStartDt.getText().length() > 0){
                            if (mTextStartDt.getText().toString().equals("0000-00-00")){
                                mTextEndDt.setText(data);
                                NetworkCall(ANTECEDENTS_INSERT);
                            }else{
                                if (Integer.parseInt(mTextStartDt.getText().toString().replace("-","")) > Integer.parseInt(data.replace("-",""))){
                                    Toast.makeText(MyListDetailActivity.this, "종료일이 시작일보다 이전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }else{
                                    mTextEndDt.setText(data);
                                    NetworkCall(ANTECEDENTS_INSERT);
                                }
                            }
                        }else{
                            mTextEndDt.setText(data);
                            NetworkCall(ANTECEDENTS_INSERT);
                        }
                    }
                }).show(fragmentManager, TAG);
                break;
            }
            case R.id.btn_my_list_detail_estimate_move : {
                Intent intent = new Intent(this, EstimateDetailActivity.class);
                intent.putExtra("flag",1);
                intent.putExtra("orderNo",getIntent().getStringExtra("orderNo"));
                startActivity(intent);
                break;
            }
            case R.id.btn_my_list_detail_img_save : {
                Intent intent = new Intent(this, ImageSaveActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_my_list_detail_finish : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new TwoButtonDialog(getString(R.string.My_List_Detail_Title), getString(R.string.My_List_Detail_Finish_Contents), new TwoButtonDialog.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(View v) {

                    }
                }).show(fragmentManager,TAG);
                break;
            }
        }
    }
}