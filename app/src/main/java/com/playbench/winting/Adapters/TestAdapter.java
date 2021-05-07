package com.playbench.winting.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

import java.util.ArrayList;

import static com.playbench.winting.Utils.Util.DueDate;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private String                      TAG = "NewRequestAdapter";
    private Context                     mContext;
    private ArrayList<NewRequestItem>   mItemArrayList = new ArrayList<>();
    private EstimateViewPagerAdpater    mAdapter;

    public TestAdapter(Context mContext) {
        this.mContext           = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = null;

        view = inflater.inflate(R.layout.view_new_request_list_item_02,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        mAdapter = new EstimateViewPagerAdpater(mContext,mItemArrayList.get(i).getmOrderNo());

        if (mItemArrayList.get(i).getmVisitImage().length() > 0){
            String[] imgList = mItemArrayList.get(i).getmVisitImage().split(",");
            for (int x = 0; x < imgList.length; x++) {
                mAdapter.addItem(imgList[x]);
            }
            viewHolder.mTextCurrentCnt.setText("1");
            viewHolder.mTextTotalCnt.setText(" / " + imgList.length);
        }else{
            mAdapter.addItem("");
            viewHolder.mTextCurrentCnt.setText("1");
            viewHolder.mTextTotalCnt.setText(" / 1");
        }

        viewHolder.mViewPager.setAdapter(mAdapter);

        ViewHolder finalViewHolder = viewHolder;
        viewHolder.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                finalViewHolder.mTextCurrentCnt.setText(""+(++position));
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewHolder.mTextAddress.setText(mItemArrayList.get(i).getmAddress());
        viewHolder.mTextProgress.setText(mItemArrayList.get(i).getmProgress());
        viewHolder.mTextDueDate.setText(DueDate(mItemArrayList.get(i).getmDueDate()));
        viewHolder.mTextRegDate.setText(mItemArrayList.get(i).getmRegDate().substring(0,10));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EstimateDetailActivity.class);
                intent.putExtra("orderNo",mItemArrayList.get(i).getmOrderNo());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewPager mViewPager;
        public TextView mTextCurrentCnt;
        public TextView mTextTotalCnt;
        public TextView mTextAddress;
        public TextView mTextProgress;
        public TextView mTextDueDate;
        public TextView mTextRegDate;

        public ViewHolder(@NonNull View view) {
            super(view);
            mViewPager = view.findViewById(R.id.view_pager_new_request);
            mTextCurrentCnt = view.findViewById(R.id.txt_new_request_list_item_pager_cnt);
            mTextTotalCnt = view.findViewById(R.id.txt_new_request_list_item_pager_total_cnt);
            mTextAddress = view.findViewById(R.id.txt_new_request_list_item_address);
            mTextProgress = view.findViewById(R.id.txt_new_request_list_item_status);
            mTextDueDate = view.findViewById(R.id.txt_new_request_list_item_due_date);
            mTextRegDate = view.findViewById(R.id.txt_new_request_list_item_reg_date);
        }
    }

    public void addItem(NewRequestItem newRequestItem){
        mItemArrayList.add(newRequestItem);
    }

}
