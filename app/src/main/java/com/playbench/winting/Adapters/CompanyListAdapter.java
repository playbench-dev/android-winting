package com.playbench.winting.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.Itmes.CompanyListItem;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

import java.util.ArrayList;

public class CompanyListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CompanyListItem> mItemArrayList = new ArrayList<>();
    private ArrayList<CompanyListItem> mItemArrayList1 = new ArrayList<>();

    public CompanyListAdapter(Context mContext) {
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
            view = inflater.inflate(R.layout.view_company_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.mTextTitle = view.findViewById(R.id.txt_company_list_item_title);
            viewHolder.mTextBoss = view.findViewById(R.id.txt_company_list_item_boss);
            viewHolder.mTextContact = view.findViewById(R.id.txt_company_list_item_contact);
            viewHolder.mTextCorporateNumber = view.findViewById(R.id.txt_company_list_item_corporate_number);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.mTextTitle.setText(mItemArrayList.get(i).getmCompanyName());
        viewHolder.mTextBoss.setText(mItemArrayList.get(i).getmOwnerName());
        viewHolder.mTextContact.setText(mItemArrayList.get(i).getmContact());
        viewHolder.mTextCorporateNumber.setText(mItemArrayList.get(i).getmCorporateNumber());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("companyNo",mItemArrayList.get(i).getmCompanyNo());
                intent.putExtra("companyName",mItemArrayList.get(i).getmCompanyName());
                ((Activity)mContext).setResult(Activity.RESULT_OK,intent);
                ((Activity)mContext).onBackPressed();
            }
        });

        return view;
    }

    private class ViewHolder{
        public TextView mTextTitle;
        public TextView mTextBoss;
        public TextView mTextContact;
        public TextView mTextCorporateNumber;
    }

    public void addItem(CompanyListItem companyListItem){
        mItemArrayList.add(companyListItem);
        mItemArrayList1.add(companyListItem);
    }

    public void searchText(String text){
        mItemArrayList.clear();
        if (text.length() == 0){

        }else {
            for (int i = 0; i < mItemArrayList1.size(); i++) {
                if (mItemArrayList1.get(i).getmCompanyName().toUpperCase().contains(text)) {
                    mItemArrayList.add(mItemArrayList1.get(i));
                }
            }
        }
        this.notifyDataSetChanged();
    }
}

