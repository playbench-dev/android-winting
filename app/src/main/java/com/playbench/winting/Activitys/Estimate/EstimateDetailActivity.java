package com.playbench.winting.Activitys.Estimate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.Adapters.EstimateViewPagerAdpater;
import com.playbench.winting.R;

public class EstimateDetailActivity extends AppCompatActivity implements View.OnClickListener , ViewPager.OnPageChangeListener {

    private String                  TAG = "EstimateDetailActivity";
    private ImageView               mImageBack;
    private ViewPager               mViewPager;
    private TextView                mTextPagerCnt;
    private TextView                mTextRegion;
    private TextView                mTextForm;
    private TextView                mTextDueDate;
    private TextView                mTextPurpose;
    private TextView                mTextDescription;
    private TextView                mTextInsulation;
    private TextView                mTextSheetPaper;
    private LinearLayout            mLinearInsulationParent;
    private LinearLayout            mLinearSheetPaperParent;
    private Button                  mButtonMove;
    private EstimateViewPagerAdpater mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_detail);

        FindViewById();

        mTextPagerCnt.setText("1/10");
                mTextRegion.setText("미사강변한강로 295 605호");
        mTextForm.setText("오피스텔");
                mTextDueDate.setText("2021-10-10");
        mTextPurpose.setText("- 결로\n- 보온\n - 빛차단");
                mTextDescription.setText("손이 안들어감 좁아서");

        for (int i = 0; i < 3; i++){
            AfterSizeList(1,12000,23000,10,23);
        }

        for (int i = 0; i < 3; i++){
            AfterSizeList(2,12000,23000,10,23);
        }

        mAdapter = new EstimateViewPagerAdpater(this);

        for (int i = 0; i < 10; i++){
            mAdapter.addItem("");
        }

        mViewPager.setAdapter(mAdapter);
    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mViewPager                  = findViewById(R.id.view_pager_estimate_detail);
        mTextPagerCnt               = findViewById(R.id.txt_estimate_detail_pager_cnt);
        mTextRegion                 = findViewById(R.id.txt_estimate_detail_region);
        mTextForm                   = findViewById(R.id.txt_estimate_detail_form);
        mTextDueDate                = findViewById(R.id.txt_estimate_detail_due_date);
        mTextPurpose                = findViewById(R.id.txt_estimate_detail_purpose);
        mTextDescription            = findViewById(R.id.txt_estimate_detail_description);
        mTextInsulation             = findViewById(R.id.txt_estimate_detail_insulation);
        mTextSheetPaper             = findViewById(R.id.txt_estimate_detail_sheet);
        mLinearInsulationParent     = findViewById(R.id.lin_estimate_detail_insulation_parent);
        mLinearSheetPaperParent     = findViewById(R.id.lin_estimate_detail_sheet_parent);
        mButtonMove                 = findViewById(R.id.btn_estimate_detail_next_move);

        mImageBack.setOnClickListener(this);
        mButtonMove.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);

        if (getIntent().hasExtra("flag")){
            mButtonMove.setVisibility(View.GONE);
        }

    }

    void AfterSizeList(int flag, int vertical, int horizontal, int paperCnt, int squareMeter){   //flag 1 - 단열 2 - 시트지
        View listView               = new View(this);
        listView                    = getLayoutInflater().inflate(R.layout.view_estimate_detail_after_size_list_item,null);
        TextView mTextVertical      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_vertical);
        TextView mTextHorizontal    = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_horizontal);
        TextView mTextPaperCnt      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_paper_cnt);
        TextView mTextSquareMeter   = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_square_meter);

        mTextVertical.setText("" + vertical);
        mTextHorizontal.setText("" + horizontal);
        mTextPaperCnt.setText("" + paperCnt);
        mTextSquareMeter.setText("" + squareMeter);

        if (flag == 1){
            mLinearInsulationParent.addView(listView);
        }else{
            mLinearSheetPaperParent.addView(listView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTextPagerCnt.setText(""+(++position)+"/10");
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_estimate_detail_next_move : {
                Intent intent = new Intent(this,EstimateSubmitActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}