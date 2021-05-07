package com.playbench.winting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.playbench.winting.Activitys.MyList.MyListDetailActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.Server;

import java.util.ArrayList;

import static com.playbench.winting.Fragments.ListFragment.ESTIMATE_CODE;

public class MyListViewPagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<String> mItemArrayList = new ArrayList<>();
    int mFlag = 1;
    String mFilePath;
    String mEstimateNo;
    String mOrderNo;
    String mProgress;
    Fragment fragment;

    public MyListViewPagerAdapter(Context mContext, Fragment fragment, String mEstimateNo, String mOrderNo, String mProgress) {
        this.mContext = mContext;
        this.fragment = fragment;
        this.mFlag = mFlag;
        this.mEstimateNo = mEstimateNo;
        this.mOrderNo = mOrderNo;
        this.mProgress = mProgress;
    }

    @Override
    public int getCount() {
        return mItemArrayList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pager_image_item, null);
        ImageView imageView = view.findViewById(R.id.img_view_pager_item);

        if (mItemArrayList.get(position).length() == 0){
            Glide.with(mContext).load(R.drawable.img_test).into(imageView);
        }else{
            Glide.with(mContext).load(Server.serverUrl +mItemArrayList.get(position)).into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyListDetailActivity.class);
                intent.putExtra("estimateNo",mEstimateNo);
                intent.putExtra("orderNo",mOrderNo);
                intent.putExtra("progress",mProgress);
                fragment.startActivityForResult(intent,ESTIMATE_CODE);
            }
        });


        (container).addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View) object);
    }

    @Override
    public void  destroyItem(View container, int position, Object object) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }

    public void addItem(String path){
        mItemArrayList.add(path);
    }

    public void addPath(String path){
        this.mFilePath = path;
    }
}
