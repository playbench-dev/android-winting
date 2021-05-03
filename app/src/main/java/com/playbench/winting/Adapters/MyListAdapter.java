package com.playbench.winting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.playbench.winting.Activitys.MyList.MyListDetailActivity;
import com.playbench.winting.Itmes.EstimateItem;
import com.playbench.winting.R;

import java.util.ArrayList;

import static com.playbench.winting.Fragments.ListFragment.ESTIMATE_CODE;
import static com.playbench.winting.Utils.Util.EstimateProgress;
import static com.playbench.winting.Utils.Util.GetFormatDEC;
import static com.playbench.winting.Utils.Util.mCodeList;
import static com.playbench.winting.Utils.Util.mNameList;

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EstimateItem> mItemArrayList = new ArrayList<>();
    private Fragment fragment;

    public MyListAdapter(Context mContext, Fragment fragment) {
        this.mContext           = mContext;
        this.fragment           = fragment;
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
            view = inflater.inflate(R.layout.view_my_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.mTextRegion = view.findViewById(R.id.txt_my_list_item_region);
            viewHolder.mTextProgress = view.findViewById(R.id.txt_my_list_item_progress);
            viewHolder.mTextStartDate = view.findViewById(R.id.txt_my_list_item_due_date);
            viewHolder.mTextEndDate = view.findViewById(R.id.txt_my_list_item_reg_date);
            viewHolder.mTextPrice = view.findViewById(R.id.txt_my_list_item_price);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        if (mItemArrayList.get(i).getRegion().length() > 2){
            for (int x = 0; x < mCodeList.length; x++){
                if (mCodeList[x].equals(mItemArrayList.get(i).getRegion().substring(0,2))){
                    viewHolder.mTextRegion.setText(mNameList[x]);
                }
            }
        }else{
            for (int x = 0; x < mCodeList.length; x++){
                if (mCodeList[x].equals(mItemArrayList.get(i).getRegion())){
                    viewHolder.mTextRegion.setText(mNameList[x]);
                }
            }
        }

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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyListDetailActivity.class);
                intent.putExtra("estimateNo",mItemArrayList.get(i).getEstimateNo());
                intent.putExtra("orderNo",mItemArrayList.get(i).getOrderNo());
                intent.putExtra("progress",mItemArrayList.get(i).getProgress());
                fragment.startActivityForResult(intent,ESTIMATE_CODE);
            }
        });

        return view;
    }

    private class ViewHolder{
        public TextView mTextRegion;
        public TextView mTextProgress;
        public TextView mTextStartDate;
        public TextView mTextEndDate;
        public TextView mTextPrice;
    }

    public void addItem(EstimateItem estimateItem){
        mItemArrayList.add(estimateItem);
    }
}
