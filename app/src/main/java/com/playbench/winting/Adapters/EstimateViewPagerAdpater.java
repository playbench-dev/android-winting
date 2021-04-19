package com.playbench.winting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.playbench.winting.R;

import java.util.ArrayList;

public class EstimateViewPagerAdpater extends PagerAdapter {

    Context mContext;
    ArrayList<String> mItemArrayList = new ArrayList<>();

    public EstimateViewPagerAdpater(Context mContext) {
        this.mContext = mContext;
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

        imageView.setImageResource(R.drawable.img_test);
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
}
