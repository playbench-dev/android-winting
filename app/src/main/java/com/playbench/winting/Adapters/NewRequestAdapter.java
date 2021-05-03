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
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

import java.util.ArrayList;

import static com.playbench.winting.Utils.Util.DueDate;
import static com.playbench.winting.Utils.Util.mCodeList;
import static com.playbench.winting.Utils.Util.mNameList;

public class NewRequestAdapter extends BaseAdapter {

    private Context                     mContext;
    private ArrayList<NewRequestItem>   mItemArrayList = new ArrayList<>();

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
            view = inflater.inflate(R.layout.view_new_request_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.mTextRegion = view.findViewById(R.id.txt_new_request_list_item_region);
            viewHolder.mTextProgress = view.findViewById(R.id.txt_new_request_list_item_progress);
            viewHolder.mTextDueDate = view.findViewById(R.id.txt_new_request_list_item_due_date);
            viewHolder.mTextRegDate = view.findViewById(R.id.txt_new_request_list_item_reg_date);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        if (mItemArrayList.get(i).getmRegion().length() > 2){
            for (int x = 0; x < mCodeList.length; x++){
                if (mCodeList[x].equals(mItemArrayList.get(i).getmRegion().substring(0,2))){
                    viewHolder.mTextRegion.setText(mNameList[x]);
                }
            }
        }else{
            for (int x = 0; x < mCodeList.length; x++){
                if (mCodeList[x].equals(mItemArrayList.get(i).getmRegion())){
                    viewHolder.mTextRegion.setText(mNameList[x]);
                }
            }
        }

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
        public TextView mTextRegion;
        public TextView mTextProgress;
        public TextView mTextDueDate;
        public TextView mTextRegDate;
    }

    public void addItem(NewRequestItem newRequestItem){
        mItemArrayList.add(newRequestItem);
    }
}
