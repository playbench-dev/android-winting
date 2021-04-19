package com.playbench.winting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.Activitys.MyList.MyListDetailActivity;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NewRequestItem> mItemArrayList = new ArrayList<>();

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
            view = inflater.inflate(R.layout.view_new_request_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.mTextRegion = view.findViewById(R.id.txt_new_request_list_item_region);
            viewHolder.mTextProgress = view.findViewById(R.id.txt_new_request_list_item_progress);
            viewHolder.mTextDueDate = view.findViewById(R.id.txt_new_request_list_item_due_date);
            viewHolder.mTextRegDate = view.findViewById(R.id.txt_new_request_list_item_reg_date);
            viewHolder.mNewTag = view.findViewById(R.id.img_new_request_list_item_tag);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.mTextRegion.setText(mItemArrayList.get(i).getmRegion());
        viewHolder.mTextProgress.setText(mItemArrayList.get(i).getmProgress());
        viewHolder.mTextDueDate.setText(mItemArrayList.get(i).getmDueDate());
        viewHolder.mTextRegDate.setText(mItemArrayList.get(i).getmRegDate());
        viewHolder.mNewTag.setVisibility(View.INVISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyListDetailActivity.class);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    private class ViewHolder{
        public TextView mTextRegion;
        public TextView mTextProgress;
        public TextView mTextDueDate;
        public TextView mTextRegDate;
        public ImageView mNewTag;
    }

    public void addItem(NewRequestItem newRequestItem){
        mItemArrayList.add(newRequestItem);
    }
}
