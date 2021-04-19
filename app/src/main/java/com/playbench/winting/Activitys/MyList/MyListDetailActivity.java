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

public class MyListDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "MyListDetailActivity";
    private ImageView               mImageBack;
    private TextView                mTextSave;
    private TextView                mTextStartDt;
    private TextView                mTextEndDt;
    private TextView                mTextPrice;
    private Button                  mButtonEstimateDetail;
    private Button                  mButtonImageSave;
    private Button                  mButtonFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_detail);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mTextSave                   = findViewById(R.id.txt_my_list_detail_save);
        mTextStartDt                = findViewById(R.id.txt_my_list_detail_start_dt);
        mTextEndDt                  = findViewById(R.id.txt_my_list_detail_end_dt);
        mTextPrice                  = findViewById(R.id.txt_my_list_detail_price);
        mButtonEstimateDetail       = findViewById(R.id.btn_my_list_detail_estimate_move);
        mButtonImageSave            = findViewById(R.id.btn_my_list_detail_img_save);
        mButtonFinish               = findViewById(R.id.btn_my_list_detail_finish);

        mImageBack.setOnClickListener(this);
        mTextSave.setOnClickListener(this);
        mTextStartDt.setOnClickListener(this);
        mTextEndDt.setOnClickListener(this);
        mButtonEstimateDetail.setOnClickListener(this);
        mButtonImageSave.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_my_list_detail_save : {

                break;
            }
            case R.id.txt_my_list_detail_start_dt : {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new DatePickerDialogActivity( new DatePickerDialogActivity.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(String data) {
                        if (mTextEndDt.getText().length() > 0){
                            if (Integer.parseInt(mTextEndDt.getText().toString().replace("-","")) < Integer.parseInt(data.replace("-",""))){
                                Toast.makeText(MyListDetailActivity.this, "종료일이 시작일보다 이전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                mTextStartDt.setText(data);
                            }
                        }else{
                            mTextStartDt.setText(data);
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
                            if (Integer.parseInt(mTextStartDt.getText().toString().replace("-","")) > Integer.parseInt(data.replace("-",""))){
                                Toast.makeText(MyListDetailActivity.this, "종료일이 시작일보다 이전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                mTextEndDt.setText(data);
                            }
                        }else{
                            mTextEndDt.setText(data);
                        }
                    }
                }).show(fragmentManager, "test");
                break;
            }
            case R.id.btn_my_list_detail_estimate_move : {
                Intent intent = new Intent(this, EstimateDetailActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
                break;
            }
            case R.id.btn_my_list_detail_img_save : {
                Intent intent = new Intent(this, ImageSaveActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_my_list_detail_finish : {

                break;
            }
        }
    }
}