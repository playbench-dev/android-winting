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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.playbench.winting.Activitys.Estimate.EstimateDetailActivity;
import com.playbench.winting.Activitys.MyList.ImageSaveActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.Server;

import java.util.ArrayList;

import static com.playbench.winting.Utils.NetworkUtils.ESTIMATE_IMAGE_DELETE;

public class EstimateViewPagerAdpater extends PagerAdapter {

    Context mContext;
    ArrayList<String> mItemArrayList = new ArrayList<>();
    int mFlag = 1;
    String mFilePath;
    String mOrderNo;

    public EstimateViewPagerAdpater(Context mContext, int mFlag) {
        this.mContext = mContext;
        this.mFlag = mFlag;
    }

    public EstimateViewPagerAdpater(Context mContext, String mOrderNo) {
        this.mContext = mContext;
        this.mOrderNo = mOrderNo;
    }

    @Override
    public int getCount() {
        return mItemArrayList.size();
    }

    void ViewPagerPopUp(int idx, String imagePath){
        ImgDetailViewPagerAdapter adapter = new ImgDetailViewPagerAdapter(mContext,mFilePath);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_img_detail_viewpager, null);
        ViewPager viewPagerDetail = (ViewPager) contentView.findViewById(R.id.view_pager_img_detail);
        TextView txtClose = (TextView) contentView.findViewById(R.id.txt_img_detail);
        ImageView imgDelete = (ImageView)contentView.findViewById(R.id.img_img_delete);

        imgDelete.setVisibility(View.INVISIBLE);

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.addContentView(contentView, params);

        String[] imgList = imagePath.split(",");

        for (int i = 0; i < imgList.length; i++){
            adapter.addItem(imgList[i]);
        }

        viewPagerDetail.setAdapter(adapter);
        viewPagerDetail.setCurrentItem(idx);

        viewPagerDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            float tempPositionOffset = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerDetail.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dialog.show();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                if (mFlag == 2){
                    if (mFilePath.length() > 0){
                        ViewPagerPopUp(position,mFilePath);
                    }
                }else if (mFlag == 3){

                }else{
                    Intent intent = new Intent(mContext, EstimateDetailActivity.class);
                    intent.putExtra("orderNo",mOrderNo);
                    mContext.startActivity(intent);
                }
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
