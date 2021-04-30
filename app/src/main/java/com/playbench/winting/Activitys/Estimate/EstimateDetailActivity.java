package com.playbench.winting.Activitys.Estimate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.Adapters.EstimateViewPagerAdpater;
import com.playbench.winting.R;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_DETAIL;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_LIST;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.DueDate;
import static com.playbench.winting.Utils.Util.FormType;
import static com.playbench.winting.Utils.Util.JsonIntIsNullCheck;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_NO;

public class EstimateDetailActivity extends AppCompatActivity implements View.OnClickListener , ViewPager.OnPageChangeListener, AsyncResponse {

    private String                  TAG = "EstimateDetailActivity";
    private ImageView               mImageBack;
    private ViewPager               mViewPager;
    private TextView                mTextPagerCnt;
    private TextView                mTextPagerTotalCnt;
    private TextView                mTextRegion;
    private TextView                mTextForm;
    private TextView                mTextDueDate;
    private TextView                mTextPurpose;
    private TextView                mTextDescription;
    private TextView                mTextInsulation;
    private TextView                mTextSheetPaper;
    private LinearLayout            mLinearInsulationParent;
    private LinearLayout            mLinearSheetPaperParent;
    private LinearLayout            mLinearInsulationVisible;
    private LinearLayout            mLinearSheetPaperVisible;
    private Button                  mButtonMove;
    private EstimateViewPagerAdpater mAdapter;

    //info
    private String                  mOrdeNo = "";
    private String                  mOrdeCode = "";
    private String                  mVisitImage = "";
    private String                  mAddress = "";
    private String                  mForm = "";
    private String                  mDueDate = "";
    private String                  mPurpose = "";
    private String                  mDescription = "";
    private String                  mAfterSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_detail);

        FindViewById();

        mAdapter = new EstimateViewPagerAdpater(this);

        NetworkCall(ORDER_DETAIL);
    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mViewPager                  = findViewById(R.id.view_pager_estimate_detail);
        mTextPagerCnt               = findViewById(R.id.txt_estimate_detail_pager_cnt);
        mTextPagerTotalCnt          = findViewById(R.id.txt_estimate_detail_pager_total_cnt);
        mTextRegion                 = findViewById(R.id.txt_estimate_detail_region);
        mTextForm                   = findViewById(R.id.txt_estimate_detail_form);
        mTextDueDate                = findViewById(R.id.txt_estimate_detail_due_date);
        mTextPurpose                = findViewById(R.id.txt_estimate_detail_purpose);
        mTextDescription            = findViewById(R.id.txt_estimate_detail_description);
        mTextInsulation             = findViewById(R.id.txt_estimate_detail_insulation);
        mTextSheetPaper             = findViewById(R.id.txt_estimate_detail_sheet);
        mLinearInsulationParent     = findViewById(R.id.lin_estimate_detail_insulation_parent);
        mLinearSheetPaperParent     = findViewById(R.id.lin_estimate_detail_sheet_parent);
        mLinearInsulationVisible    = findViewById(R.id.lin_estimate_detail_insulation_visible);
        mLinearSheetPaperVisible    = findViewById(R.id.lin_estimate_detail_sheet_visible);
        mButtonMove                 = findViewById(R.id.btn_estimate_detail_next_move);

        mImageBack.setOnClickListener(this);
        mButtonMove.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);

        if (getIntent().hasExtra("flag")){
            mButtonMove.setVisibility(View.GONE);
        }

    }

    //헤베(제곱미터) = 가로(mm)*세로(mm)/1,000,000
    void AfterSizeList(int flag, int vertical, int horizontal, int paperCnt){   //flag 1 - 단열 2 - 시트지
        View listView               = new View(this);
        listView                    = getLayoutInflater().inflate(R.layout.view_estimate_detail_after_size_list_item,null);
        TextView mTextVertical      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_vertical);
        TextView mTextHorizontal    = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_horizontal);
        TextView mTextPaperCnt      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_paper_cnt);
        TextView mTextSquareMeter   = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_square_meter);

        mTextVertical.setText("" + vertical);
        mTextHorizontal.setText("" + horizontal);
        mTextPaperCnt.setText("" + paperCnt);
        double squareMeter = ((vertical * 10) * (horizontal * 10) / 1000000);
        mTextSquareMeter.setText("" + Math.round(squareMeter));

        if (flag == 1){
            mLinearInsulationParent.addView(listView);
        }else{
            mLinearSheetPaperParent.addView(listView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTextPagerCnt.setText(""+(++position));
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    void NetworkCall(String mCode){
        if (mCode.equals(ORDER_DETAIL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(getIntent().getStringExtra("orderNo"));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ORDER_DETAIL)){
                    JSONObject object = jsonArray.getJSONObject(0);

                    JSONArray jsonArrayVisit = null;
                    JSONArray jsonArrayPurpose = null;
                    JSONArray jsonArrayAfterSize = null;

                    mOrdeNo = object.getString("order_no");
                    mOrdeCode = object.getString("order_code");

                    if (object.has("visit_image")){
                        jsonArrayVisit = object.getJSONArray("visit_image");
                        for (int i = 0; i < jsonArrayVisit.length(); i++){
                            mAdapter.addItem(jsonArrayVisit.getString(i));
                        }
                        mViewPager.setAdapter(mAdapter);
                        mTextPagerTotalCnt.setText(" / "+mViewPager.getChildCount());
                    }else{
                        mTextPagerCnt.setText("0");
                        mTextPagerTotalCnt.setText(" / 0");
                    }
                    if (object.has("purpose")){
                        jsonArrayPurpose = object.getJSONArray("purpose");
                        mTextPurpose.setText(JsonIsNullCheck(jsonArrayPurpose.getJSONObject(0),"text"));
                    }
                    if (object.has("after_size")){
                        jsonArrayAfterSize = object.getJSONArray("after_size");
                        mAfterSize = jsonArrayAfterSize.toString();
                        for (int i = 0; i < jsonArrayAfterSize.length(); i++){
                            JSONObject objectSize = jsonArrayAfterSize.getJSONObject(i);
                            if (JsonIntIsNullCheck(objectSize,"type") == 1){
                                mLinearInsulationVisible.setVisibility(View.VISIBLE);
                            }else{
                                mLinearSheetPaperVisible.setVisibility(View.VISIBLE);
                            }
                            AfterSizeList(JsonIntIsNullCheck(objectSize,"type"),JsonIntIsNullCheck(objectSize,"width"),JsonIntIsNullCheck(objectSize,"height"),JsonIntIsNullCheck(objectSize,"count"));
                        }
                    }
                    mAddress = JsonIsNullCheck(object,"address");
                    mForm = JsonIsNullCheck(object,"form");
                    mDueDate = JsonIsNullCheck(object,"due_date");

                    mTextRegion.setText(JsonIsNullCheck(object,"address"));
                    mTextForm.setText(FormType(JsonIsNullCheck(object,"form")));
                    mTextDueDate.setText(DueDate(JsonIsNullCheck(object,"due_date")));
                    mTextDescription.setText(JsonIsNullCheck(object,"description"));
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Estimate_Detail_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
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
            case R.id.btn_estimate_detail_next_move : {
                Intent intent = new Intent(this,EstimateSubmitActivity.class);
                intent.putExtra("orderNo",mOrdeNo);
                intent.putExtra("orderCode",mOrdeCode);
                intent.putExtra("afterSize",mAfterSize);
                intent.putExtra("address",mAddress);
                intent.putExtra("dueDate",mDueDate);
                intent.putExtra("form",mForm);
                startActivity(intent);
                break;
            }
        }
    }
}