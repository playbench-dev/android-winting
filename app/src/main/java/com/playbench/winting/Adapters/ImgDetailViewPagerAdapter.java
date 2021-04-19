package com.playbench.winting.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.playbench.winting.Itmes.GalleryItem;
import com.playbench.winting.R;
import com.playbench.winting.views.ImagePinch;

import java.util.ArrayList;
import java.util.List;


public class ImgDetailViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> listViewItems = new ArrayList<>();
    List<GalleryItem> galleryItems;
    int flag = 0;

    public ImgDetailViewPagerAdapter(Context context, List<GalleryItem> galleryItems) {
        this.context = context;
        this.galleryItems = galleryItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return galleryItems.size();
    }

    @Override
    public void startUpdate(View view) {

    }

    @Override
    public Object instantiateItem(View view, int i) {
        return null;
    }

    @Override
    public void destroyItem(View view, int i, Object o) {

    }

    @Override
    public void finishUpdate(View view) {

    }

    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        View view = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_image_detail, null);
        ImagePinch imgParent = (ImagePinch) view.findViewById(R.id.img_view_pager_parent);
        ImageView img = (ImageView) view.findViewById(R.id.img_view_pager);
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        Glide.with(context).asBitmap().load(getImageInfo(galleryItems.get(position).getIdTest())).override(display.getWidth(), display.getHeight()).into(img);

        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {

    }

    public void addItem(String imagePath) {
        listViewItems.add(imagePath);
    }


    private String getImageInfo(String thumbID) {
        String imageDataPath = null;
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, "_ID='" + thumbID + "'", null, null);

        if (imageCursor != null && imageCursor.moveToFirst()) {
            if (imageCursor.getCount() > 0) {
                int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);

                imageDataPath = imageCursor.getString(imgData);
            }
        }
        imageCursor.close();
        return imageDataPath;
    }

}
