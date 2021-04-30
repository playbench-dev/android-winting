package com.playbench.winting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.Activitys.MyList.MyListDetailActivity;
import com.playbench.winting.Itmes.EstimateItem;
import com.playbench.winting.R;

import java.util.ArrayList;

import static com.playbench.winting.Utils.Util.EstimateProgress;

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EstimateItem> mItemArrayList = new ArrayList<>();

    public MyListAdapter(Context mContext) {
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

        viewHolder.mTextRegion.setText(mItemArrayList.get(i).getRegion());
        viewHolder.mTextProgress.setText(EstimateProgress(mItemArrayList.get(i).getProgress()));
        viewHolder.mTextStartDate.setText(mItemArrayList.get(i).getStartDate().substring(0,10));
        viewHolder.mTextEndDate.setText(mItemArrayList.get(i).getEndDate().substring(0,10));
        viewHolder.mTextPrice.setText(mItemArrayList.get(i).getPrice());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyListDetailActivity.class);
                intent.putExtra("estimateNo",mItemArrayList.get(i).getEstimateNo());
                intent.putExtra("orderNo",mItemArrayList.get(i).getOrderNo());
                mContext.startActivity(intent);
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
