package com.playbench.winting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.Activitys.MyList.MyListDetailActivity;
import com.playbench.winting.Itmes.EstimateItem;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

import java.util.ArrayList;

import static com.playbench.winting.Fragments.ListFragment.ESTIMATE_CODE;
import static com.playbench.winting.Utils.Util.DueDate;
import static com.playbench.winting.Utils.Util.EstimateProgress;
import static com.playbench.winting.Utils.Util.GetFormatDEC;

public class Test2Adapter extends RecyclerView.Adapter<Test2Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<EstimateItem> mItemArrayList = new ArrayList<>();
    private Fragment fragment;
    private MyListViewPagerAdapter    mAdapter;

    public Test2Adapter(Context mContext, Fragment fragment) {
        this.mContext           = mContext;
        this.fragment           = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = null;

        view = inflater.inflate(R.layout.view_my_list_item_02,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        mAdapter = new MyListViewPagerAdapter(mContext,fragment,mItemArrayList.get(i).getEstimateNo(),mItemArrayList.get(i).getOrderNo(),mItemArrayList.get(i).getProgress());

        if (mItemArrayList.get(i).getVisitImage().length() > 0){
            String[] imgList = mItemArrayList.get(i).getVisitImage().split(",");
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

        viewHolder.mTextAddress.setText(mItemArrayList.get(i).getAddress());
        viewHolder.mTextProgress.setText(EstimateProgress(mItemArrayList.get(i).getProgress()));

        if (mItemArrayList.get(i).getStartDate().length() > 10){
            viewHolder.mTextStartDate.setText(mItemArrayList.get(i).getStartDate().substring(0,10));
        }else{
            viewHolder.mTextStartDate.setText(mItemArrayList.get(i).getStartDate());
        }

        if (mItemArrayList.get(i).getEndDate().length() > 10){
            viewHolder.mTextEndDate.setText(mItemArrayList.get(i).getEndDate().substring(0,10));
        }else{
            viewHolder.mTextEndDate.setText(mItemArrayList.get(i).getEndDate());
        }

        viewHolder.mTextPrice.setText(GetFormatDEC(mItemArrayList.get(i).getPrice()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyListDetailActivity.class);
                intent.putExtra("estimateNo",mItemArrayList.get(i).getEstimateNo());
                intent.putExtra("orderNo",mItemArrayList.get(i).getOrderNo());
                intent.putExtra("progress",mItemArrayList.get(i).getProgress());
                fragment.startActivityForResult(intent,ESTIMATE_CODE);
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
        public TextView mTextStartDate;
        public TextView mTextEndDate;
        public TextView mTextPrice;

        public ViewHolder(@NonNull View view) {
            super(view);
            mViewPager = view.findViewById(R.id.view_pager_my_list);
            mTextCurrentCnt = view.findViewById(R.id.txt_my_list_list_item_pager_cnt);
            mTextTotalCnt = view.findViewById(R.id.txt_my_list_list_item_pager_total_cnt);
            mTextAddress = view.findViewById(R.id.txt_my_list_list_item_address);
            mTextProgress = view.findViewById(R.id.txt_my_list_list_item_progress);
            mTextStartDate = view.findViewById(R.id.txt_my_list_list_item_start_date);
            mTextEndDate = view.findViewById(R.id.txt_my_list_list_item_end_date);
            mTextPrice = view.findViewById(R.id.txt_my_list_list_item_price);
        }
    }

    public void addItem(EstimateItem estimateItem){
        mItemArrayList.add(estimateItem);
    }

}

