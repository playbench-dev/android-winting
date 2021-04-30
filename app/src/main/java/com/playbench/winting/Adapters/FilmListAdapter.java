package com.playbench.winting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.Activitys.Estimate.FilmInsertActivity;
import com.playbench.winting.Activitys.MainActivity;
import com.playbench.winting.Fragments.MyFilmFragment;
import com.playbench.winting.Itmes.MyFilmItem;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

import java.util.ArrayList;

public class FilmListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MyFilmItem> mItemArrayList = new ArrayList<>();
    private Fragment fragment;

    public FilmListAdapter(Context mContext, Fragment fragment) {
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
            view = inflater.inflate(R.layout.view_my_film_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.mTextBrand         = view.findViewById(R.id.txt_my_film_list_item_brand);
            viewHolder.mTextName          = view.findViewById(R.id.txt_my_film_list_item_name);
            viewHolder.mTextIRR           = view.findViewById(R.id.txt_my_film_list_item_irr);
            viewHolder.mTextVLT           = view.findViewById(R.id.txt_my_film_list_item_vlt);
            viewHolder.mTextUVR           = view.findViewById(R.id.txt_my_film_list_item_uvr);
            viewHolder.mTextTSER          = view.findViewById(R.id.txt_my_film_list_item_tser);
            viewHolder.mLinearVisible     = view.findViewById(R.id.lin_my_film_list_item_visible);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.mTextBrand.setText(mContext.getString(R.string.My_Film_Brand) + " " + mContext.getString(R.string.My_Film_Middle) + " " + mItemArrayList.get(i).getFilmBrand());
        viewHolder.mTextName.setText(mContext.getString(R.string.My_Film_Name) + " " + mContext.getString(R.string.My_Film_Middle) + " " + mItemArrayList.get(i).getFilmName());
        viewHolder.mTextIRR.setText(mContext.getString(R.string.My_Film_IRR) + " " + mContext.getString(R.string.My_Film_Middle) + " " + mItemArrayList.get(i).getFilmIRR() + " " + mContext.getString(R.string.My_Film_Percent));
        viewHolder.mTextVLT.setText(mContext.getString(R.string.My_Film_VRT) + " " + mContext.getString(R.string.My_Film_Middle) + " " + mItemArrayList.get(i).getFilmVLT() + " " + mContext.getString(R.string.My_Film_Percent));
        viewHolder.mTextUVR.setText(mContext.getString(R.string.My_Film_UVR) + " " + mContext.getString(R.string.My_Film_Middle) + " " + mItemArrayList.get(i).getFilmUVR() + " " + mContext.getString(R.string.My_Film_Percent));
        viewHolder.mTextTSER.setText(mContext.getString(R.string.My_Film_TSER) + " " + mContext.getString(R.string.My_Film_Middle) + " " + mItemArrayList.get(i).getFilmTSER() + " " + mContext.getString(R.string.My_Film_Percent));

        if (mItemArrayList.get(i).getFilmType().equals("1")){
            viewHolder.mLinearVisible.setVisibility(View.VISIBLE);
        }else{
            viewHolder.mLinearVisible.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FilmInsertActivity.class);
                intent.putExtra("filmNo",mItemArrayList.get(i).getFilmNo());
                intent.putExtra("filmType",mItemArrayList.get(i).getFilmType());
                intent.putExtra("filmBrand",mItemArrayList.get(i).getFilmBrand());
                intent.putExtra("filmName",mItemArrayList.get(i).getFilmName());
                intent.putExtra("filmVLT",mItemArrayList.get(i).getFilmVLT());
                intent.putExtra("filmUVR",mItemArrayList.get(i).getFilmUVR());
                intent.putExtra("filmIRR",mItemArrayList.get(i).getFilmIRR());
                intent.putExtra("filmTSER",mItemArrayList.get(i).getFilmTSER());
                intent.putExtra("filmAsDate",mItemArrayList.get(i).getFilmAsDate());
                fragment.startActivityForResult(intent, MyFilmFragment.FILM_EDIT);
            }
        });

        return view;
    }

    private class ViewHolder{
        public TextView mTextBrand;
        public TextView mTextName;
        public TextView mTextIRR;
        public TextView mTextVLT;
        public TextView mTextUVR;
        public TextView mTextTSER;
        public LinearLayout mLinearVisible;
    }

    public void addItem(MyFilmItem myFilmItem){
        mItemArrayList.add(myFilmItem);
    }
}
