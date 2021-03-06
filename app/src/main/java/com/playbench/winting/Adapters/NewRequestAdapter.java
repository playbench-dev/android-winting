package com.playbench.winting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

import java.util.ArrayList;

import static com.playbench.winting.Utils.Util.DueDate;
import static com.playbench.winting.Utils.Util.mCodeList;
import static com.playbench.winting.Utils.Util.mNameList;

public class NewRequestAdapter extends BaseAdapter {

    private String                      TAG = "NewRequestAdapter";
    private Context                     mContext;
    private ArrayList<NewRequestItem>   mItemArrayList = new ArrayList<>();
    private EstimateViewPagerAdpater    mAdapter;

    public NewRequestAdapter(Context mContext) {
        this.mContext           = mContext;
    }

    @Override
    public int getCount() {
        return mItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_new_request_list_item_02,null);

            viewHolder = new ViewHolder();

            viewHolder.mViewPager = view.findViewById(R.id.view_pager_new_request);
            viewHolder.mTextCurrentCnt = view.findViewById(R.id.txt_new_request_list_item_pager_cnt);
            viewHolder.mTextTotalCnt = view.findViewById(R.id.txt_new_request_list_item_pager_total_cnt);
            viewHolder.mTextAddress = view.findViewById(R.id.txt_new_request_list_item_address);
            viewHolder.mTextProgress = view.findViewById(R.id.txt_new_request_list_item_status);
            viewHolder.mTextDueDate = view.findViewById(R.id.txt_new_request_list_item_due_date);
            viewHolder.mTextRegDate = view.findViewById(R.id.txt_new_request_list_item_reg_date);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        Log.i(TAG,"visitImage : " + mItemArrayList.get(i).getmVisitImage());


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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EstimateDetailActivity.class);
                intent.putExtra("orderNo",mItemArrayList.get(i).getmOrderNo());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    private class ViewHolder{
        public ViewPager mViewPager;
        public TextView mTextCurrentCnt;
        public TextView mTextTotalCnt;
        public TextView mTextAddress;
        public TextView mTextProgress;
        public TextView mTextDueDate;
        public TextView mTextRegDate;
    }

    public void addItem(NewRequestItem newRequestItem){
        mItemArrayList.add(newRequestItem);
    }
}
