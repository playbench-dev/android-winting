package com.playbench.winting.Activitys.MyList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.playbench.winting.Activitys.GalleryActivity;
import com.playbench.winting.Adapters.EstimateViewPagerAdpater;
import com.playbench.winting.Adapters.ImgDetailViewPagerAdapter;
import com.playbench.winting.R;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.Server;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;
import com.playbench.winting.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.ESTIMATE_IMAGE_CALL;
import static com.playbench.winting.Utils.NetworkUtils.ESTIMATE_IMAGE_DELETE;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;

public class ImageSaveActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String                  TAG = "ImageSaveActivity";
    private ImageView               mImageBack;
    private LinearLayout            mLinearBefore;
    private LinearLayout            mLinearBeforeListParent;
    private LinearLayout            mLinearCurrent;
    private LinearLayout            mLinearCurrentListParent;
    private LinearLayout            mLinearAfter;
    private LinearLayout            mLinearAfterListParent;

    private int                     BEFORE_INTENT = 1111;
    private int                     CURRENT_INTENT = 2222;
    private int                     AFTER_INTENT = 3333;
    private int                     SELECT_INTENT = 0;
    private String                  mState = "";
    private String                  mFilePath = "";
    private int                     mDeleteIdx;

    private ArrayList<String>       mListBefore = new ArrayList<>();
    private ArrayList<String>       mListCurrent = new ArrayList<>();
    private ArrayList<String>       mListAfter = new ArrayList<>();

    String realUrl = "";
    String[] realUrlList = new String[]{};
    ArrayList<String> testList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_save);

        FindViewById();

        NetworkCall(ESTIMATE_IMAGE_CALL);
    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mLinearBefore               = findViewById(R.id.lin_image_save_before);
        mLinearBeforeListParent     = findViewById(R.id.lin_image_save_before_parent);
        mLinearCurrent              = findViewById(R.id.lin_image_save_current);
        mLinearCurrentListParent    = findViewById(R.id.lin_image_save_current_parent);
        mLinearAfter                = findViewById(R.id.lin_image_save_after);
        mLinearAfterListParent      = findViewById(R.id.lin_image_save_after_parent);

        mImageBack.setOnClickListener(this);
        mLinearBefore.setOnClickListener(this);
        mLinearCurrent.setOnClickListener(this);
        mLinearAfter.setOnClickListener(this);

        if (Integer.parseInt(getIntent().getStringExtra("progress")) >= 3 || Integer.parseInt(getIntent().getStringExtra("progress")) == 1){
            mLinearBefore.setEnabled(false);
            mLinearCurrent.setEnabled(false);
            mLinearAfter.setEnabled(false);
        }
    }

    void fileMakedirs(){
        String str = Environment.getExternalStorageState();
        if (str.equals(Environment.MEDIA_MOUNTED)) {

            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dirPath);
            if (!file.exists()) { // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
            }
        } else {
            Toast.makeText(ImageSaveActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
        }
    }

    void imgDetail(String imgPath){
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.view_img_detail,null);

        ImageView img = (ImageView)contentView.findViewById(R.id.img_detail);
        TextView txtClose = (TextView)contentView.findViewById(R.id.txt_img_detail);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.addContentView(contentView,params);


        Glide.with(this).load("http:"+imgPath).into(img);

        dialog.show();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void check() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, 1234);
        }else{
            fileMakedirs();
            Intent i = new Intent(ImageSaveActivity.this, GalleryActivity.class);
            if (SELECT_INTENT == BEFORE_INTENT){
                i.putExtra("state","before");
            }else if (SELECT_INTENT == CURRENT_INTENT){
                i.putExtra("state","proceed");
            }else if (SELECT_INTENT == AFTER_INTENT){
                i.putExtra("state","after");
            }
            i.putExtra("orderNo",getIntent().getStringExtra("orderNo"));
            startActivityForResult(i, SELECT_INTENT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234:
                fileMakedirs();
                Intent i = new Intent(ImageSaveActivity.this, GalleryActivity.class);
                if (SELECT_INTENT == BEFORE_INTENT){
                    i.putExtra("state","before");
                }else if (SELECT_INTENT == CURRENT_INTENT){
                    i.putExtra("state","proceed");
                }else if (SELECT_INTENT == AFTER_INTENT){
                    i.putExtra("state","after");
                }
                i.putExtra("orderNo",getIntent().getStringExtra("orderNo"));
                startActivityForResult(i, SELECT_INTENT);
            default:
                break;
        }
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            fileMakedirs();
            Intent i = new Intent(ImageSaveActivity.this, GalleryActivity.class);
            if (SELECT_INTENT == BEFORE_INTENT){
                i.putExtra("state","before");
            }else if (SELECT_INTENT == CURRENT_INTENT){
                i.putExtra("state","proceed");
            }else if (SELECT_INTENT == AFTER_INTENT){
                i.putExtra("state","after");
            }
            i.putExtra("orderNo",getIntent().getStringExtra("orderNo"));
            startActivityForResult(i, SELECT_INTENT);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(ImageSaveActivity.this, "", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_INTENT) {
            if (resultCode == RESULT_OK) {
                NetworkCall(ESTIMATE_IMAGE_CALL);
            }
        }
    }

    void selectImg(final String idx, final String imagePath,String state) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_select_gallery_list_item, null);

        ImageView img = (ImageView) listView.findViewById(R.id.img_select_gallery_list_item);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(this)
                .load(Server.serverUrl + imagePath)
                .apply(requestOptions)
                .into(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mState = state;
                mFilePath = imagePath;
                ViewPagerPopUp(Integer.parseInt(idx),state,imagePath);
            }
        });

        if (SELECT_INTENT == BEFORE_INTENT){
            mListBefore.add(imagePath);
            mLinearBeforeListParent.addView(listView);
        }else if (SELECT_INTENT == CURRENT_INTENT){
            mListCurrent.add(imagePath);
            mLinearCurrentListParent.addView(listView);
        }else if (SELECT_INTENT == AFTER_INTENT){
            mListAfter.add(imagePath);
            mLinearAfterListParent.addView(listView);
        }else{
            if (state.equals("before")){
                mListBefore.add(imagePath);
                mLinearBeforeListParent.addView(listView);
            }else if (state.equals("proceed")){
                mListCurrent.add(imagePath);
                mLinearCurrentListParent.addView(listView);
            }else{
                mListAfter.add(imagePath);
                mLinearAfterListParent.addView(listView);
            }
        }
    }

    void ViewPagerPopUp(int idx, String state, String imagePath){
        EstimateViewPagerAdpater adapter = new EstimateViewPagerAdpater(this,3);
        final BottomSheetDialog dialog = new BottomSheetDialog(ImageSaveActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_img_detail_viewpager, null);

        ViewPager viewPagerDetail = (ViewPager) contentView.findViewById(R.id.view_pager_img_detail);
        TextView txtClose = (TextView) contentView.findViewById(R.id.txt_img_detail);
        ImageView imgDelete = (ImageView)contentView.findViewById(R.id.img_img_delete);

        if (Integer.parseInt(getIntent().getStringExtra("progress")) >= 3 || Integer.parseInt(getIntent().getStringExtra("progress")) == 1){
            imgDelete.setVisibility(View.GONE);
        }

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.addContentView(contentView, params);

        String file = "";

        if (state.equals("before")){
            for (int i = 0; i < mListBefore.size(); i++){
                adapter.addItem(mListBefore.get(i));
                if (i == 0){
                    file += "" + mListBefore.get(i);
                }else{
                    file += "," + mListBefore.get(i);
                }
            }
        }else if (state.equals("proceed")){
            for (int i = 0; i < mListCurrent.size(); i++){
                adapter.addItem(mListCurrent.get(i));
                if (i == 0){
                    file += "" + mListCurrent.get(i);
                }else{
                    file += "," + mListCurrent.get(i);
                }
            }
        }else{
            for (int i = 0; i < mListAfter.size(); i++){
                adapter.addItem(mListAfter.get(i));
                if (i == 0){
                    file += "" + mListAfter.get(i);
                }else{
                    file += "," + mListAfter.get(i);
                }
            }
        }
        adapter.addPath(file);

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
                mDeleteIdx = idx;
                mState = state;
                mFilePath = imagePath;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dialog.show();

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkCall(ESTIMATE_IMAGE_DELETE);
                dialog.dismiss();
            }
        });

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.lin_image_save_before : {
                SELECT_INTENT = BEFORE_INTENT;
                if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
                    TedPermission.with(this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
                break;
            }
            case R.id.lin_image_save_current : {
                SELECT_INTENT = CURRENT_INTENT;
                if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
                    TedPermission.with(this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
                break;
            }
            case R.id.lin_image_save_after : {
                SELECT_INTENT = AFTER_INTENT;
                if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
                    TedPermission.with(this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
                break;
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(ESTIMATE_IMAGE_CALL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(getIntent().getStringExtra("orderNo"));
        }else if (mCode.equals(ESTIMATE_IMAGE_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(getIntent().getStringExtra("orderNo"),mState,mFilePath);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ESTIMATE_IMAGE_CALL)){
                    JSONObject objectImage = jsonArray.getJSONObject(0);
                    JSONObject objectConstruction = objectImage.getJSONObject("construction_image");
                    JSONArray jsonArrayBefore = null;
                    JSONArray jsonArrayProceed = null;
                    JSONArray jsonArrayAfter = null;
                    if (SELECT_INTENT == 0){
                        if (objectConstruction.has("before")){
                            jsonArrayBefore = objectConstruction.getJSONArray("before");
                            for (int i = 0; i < jsonArrayBefore.length(); i++){
                                selectImg(""+i,jsonArrayBefore.getString(i),"before");
                            }
                        }
                        if (objectConstruction.has("proceed")){
                            jsonArrayProceed = objectConstruction.getJSONArray("proceed");
                            for (int i = 0; i < jsonArrayProceed.length(); i++){
                                selectImg(""+i,jsonArrayProceed.getString(i),"proceed");
                            }
                        }
                        if (objectConstruction.has("after")){
                            jsonArrayAfter = objectConstruction.getJSONArray("after");
                            for (int i = 0; i < jsonArrayAfter.length(); i++){
                                selectImg(""+i,jsonArrayAfter.getString(i),"after");
                            }
                        }
                    }else if (SELECT_INTENT == BEFORE_INTENT){
                        if (objectConstruction.has("before")){
                            mLinearBeforeListParent.removeAllViews();
                            mListBefore = new ArrayList<>();
                            jsonArrayBefore = objectConstruction.getJSONArray("before");
                            for (int i = 0; i < jsonArrayBefore.length(); i++){
                                selectImg(""+i,jsonArrayBefore.getString(i),"before");
                            }
                        }
                    }else if (SELECT_INTENT == CURRENT_INTENT){
                        if (objectConstruction.has("proceed")){
                            mLinearCurrentListParent.removeAllViews();
                            mListCurrent = new ArrayList<>();
                            jsonArrayProceed = objectConstruction.getJSONArray("proceed");
                            for (int i = 0; i < jsonArrayProceed.length(); i++){
                                selectImg(""+i,jsonArrayProceed.getString(i),"proceed");
                            }
                        }
                    }else if (SELECT_INTENT == AFTER_INTENT){
                        if (objectConstruction.has("after")){
                            mLinearAfterListParent.removeAllViews();
                            mListAfter = new ArrayList<>();
                            jsonArrayAfter = objectConstruction.getJSONArray("after");
                            for (int i = 0; i < jsonArrayAfter.length(); i++){
                                selectImg(""+i,jsonArrayAfter.getString(i),"after");
                            }
                        }
                    }

                }else if (mCode.equals(ESTIMATE_IMAGE_DELETE)){
                    if (mState.equals("before")){
                        mListBefore.remove(mDeleteIdx);
                        mLinearBeforeListParent.removeViewAt(mDeleteIdx);
                    }else if (mState.equals("proceed")){
                        mListCurrent.remove(mDeleteIdx);
                        mLinearCurrentListParent.removeViewAt(mDeleteIdx);
                    }else{
                        mListAfter.remove(mDeleteIdx);
                        mLinearAfterListParent.removeViewAt(mDeleteIdx);
                    }
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Image_Save_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }
}