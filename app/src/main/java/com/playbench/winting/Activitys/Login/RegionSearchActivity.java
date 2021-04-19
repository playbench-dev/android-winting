package com.playbench.winting.Activitys.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.R;

import java.util.ArrayList;
import java.util.Arrays;

public class RegionSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "RegionSearchActivity";
    private ImageView               mImageClose;
    private LinearLayout            mLinearListParent;
    private Button                  mButtonDone;

    private String[]                mCodeList = new String[]{"1100000000", "2600000000", "2700000000", "2800000000", "2900000000", "3000000000", "3100000000", "3600000000", "4100000000", "4200000000", "4300000000", "4400000000", "4500000000", "4600000000", "4700000000", "4800000000", "5000000000"};
    private String[]                mNameList = new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"};
    private String                  mRegionString = "";
    private Intent                  mBeforeIntent;
    private String[]                mRegionList;
    private ArrayList<String>       mRegionSelect = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_search);

        mBeforeIntent               = getIntent();
        if (mBeforeIntent.hasExtra("regionList")){
            if (mBeforeIntent.getStringExtra("regionList").length() > 0)
            mRegionList = mBeforeIntent.getStringExtra("regionList").split(",");
        }

        FindViewById();

        for (int i = 0; i < mCodeList.length; i++){
            if (mRegionList != null){
                if ( Arrays.asList(mRegionList).contains(mCodeList[i])){
                    mRegionSelect.add(mCodeList[i]);
                    RegionList(mCodeList[i],mNameList[i],true);
                }else{
                    RegionList(mCodeList[i],mNameList[i],false);
                }
            }else{
                RegionList(mCodeList[i],mNameList[i],false);
            }
        }
    }

    void FindViewById(){
        mImageClose                 = findViewById(R.id.img_back);
        mLinearListParent           = findViewById(R.id.lin_region_search_list_parent);
        mButtonDone                 = findViewById(R.id.btn_region_search_done);

        mImageClose.setOnClickListener(this);
        mButtonDone.setOnClickListener(this);
    }

    void RegionList(String mCode, String mName, boolean check){
        View listView               = new View(this);
        listView                    = getLayoutInflater().inflate(R.layout.view_region_search_list_item,null);
        TextView mTextTitle         = listView.findViewById(R.id.txt_region_search_list_item_title);
        ImageView mImageCheck       = listView.findViewById(R.id.img_region_search_list_item_check);

        mTextTitle.setText(mName);

        mLinearListParent.addView(listView);

        if (check){
            mTextTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            mImageCheck.setImageResource(R.drawable.ic_check);
        }else{
            mTextTitle.setTextColor(getResources().getColor(R.color.colorDisableText));
            mImageCheck.setImageResource(R.drawable.ic_check_off);
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRegionSelect.contains(mCode)){
                    mRegionSelect.remove(mCode);
                    mTextTitle.setTextColor(getResources().getColor(R.color.colorDisableText));
                    mImageCheck.setImageResource(R.drawable.ic_check_off);
                }else{
                    mRegionSelect.add(mCode);
                    mTextTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mImageCheck.setImageResource(R.drawable.ic_check);
                }
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
            case R.id.btn_region_search_done : {
                if (mRegionSelect.size() > 0){
                    for (int i = 0; i < mRegionSelect.size(); i++){
                        if (i == 0){
                            mRegionString += mRegionSelect.get(i);
                        }else{
                            mRegionString += ","+mRegionSelect.get(i);
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("regionList",mRegionString);
                    setResult(RESULT_OK,intent);
                    onBackPressed();
                }

                break;
            }
        }
    }
}