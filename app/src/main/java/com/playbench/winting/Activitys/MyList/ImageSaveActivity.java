package com.playbench.winting.Activitys.MyList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
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
import com.playbench.winting.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageSaveActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "ImageSaveActivity";
    private ImageView               mImageBack;
    private LinearLayout            mLinearBefore;
    private LinearLayout            mLinearBeforeListParent;
    private LinearLayout            mLinearCurrent;
    private LinearLayout            mLinearCurrentListParent;
    private LinearLayout            mLinearAfter;
    private LinearLayout            mLinearAfterListParent;
    private Button                  mButtonFinish;

    private int                     BEFORE_INTENT = 1111;
    private int                     CURRENT_INTENT = 2222;
    private int                     AFTER_INTENT = 3333;
    private int                     SELECT_INTENT;

    String realUrl = "";
    String[] realUrlList = new String[]{};
    ArrayList<String> testList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_save);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mLinearBefore               = findViewById(R.id.lin_image_save_before);
        mLinearBeforeListParent     = findViewById(R.id.lin_image_save_before_parent);
        mLinearCurrent              = findViewById(R.id.lin_image_save_current);
        mLinearCurrentListParent    = findViewById(R.id.lin_image_save_current_parent);
        mLinearAfter                = findViewById(R.id.lin_image_save_after);
        mLinearAfterListParent      = findViewById(R.id.lin_image_save_after_parent);
        mButtonFinish               = findViewById(R.id.btn_image_save_finish);

        mImageBack.setOnClickListener(this);
        mLinearBefore.setOnClickListener(this);
        mLinearCurrent.setOnClickListener(this);
        mLinearAfter.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);
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
            startActivityForResult(i, SELECT_INTENT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234:
                fileMakedirs();
                Intent i = new Intent(ImageSaveActivity.this, GalleryActivity.class);
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
                if (SELECT_INTENT == BEFORE_INTENT){
                    mLinearBeforeListParent.removeAllViews();
                }else if (SELECT_INTENT == CURRENT_INTENT){
                    mLinearCurrentListParent.removeAllViews();
                }else if (SELECT_INTENT == AFTER_INTENT){
                    mLinearAfterListParent.removeAllViews();
                }

                realUrl = data.getStringExtra("realUrl");
                realUrlList = realUrl.split(",");
                testList = new ArrayList<>(Arrays.asList(realUrlList));
                for (int i = 0; i < testList.size(); i++) {
                    selectImg("" + i, testList.get(i), null);
                }
            }
        }
    }

    void selectImg(final String idx, final String imagePath, final Bitmap bitmap) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_select_gallery_list_item, null);

        ImageView img = (ImageView) listView.findViewById(R.id.img_select_gallery_list_item);
//        ImageView imgRemove = (ImageView) listView.findViewById(R.id.img_select_gallery_list_item_remove);

//        Glide.with(this).load(file).into(img);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(this)
                .load("http:" + imagePath)
                .apply(requestOptions)
                .into(img);

        final View finalListView = listView;

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDetail("http:" + imagePath);
            }
        });

//        imgRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linPictureParent.removeView(finalListView);
//
//                testList.remove(imagePath);
//                txtPictureLength.setText(""+testList.size()+"/5");
//                if (testList.size() == 0){
//                    realUrl = "";
//                }else{
//                    for (int i = 0; i < testList.size(); i++) {
//                        if (i == 0) {
//                            realUrl = testList.get(i);
//                        } else {
//                            realUrl += "," + testList.get(i);
//                        }
//                    }
//                }
//            }
//        });

        if (SELECT_INTENT == BEFORE_INTENT){
            mLinearBeforeListParent.addView(listView);
        }else if (SELECT_INTENT == CURRENT_INTENT){
            mLinearCurrentListParent.addView(listView);
        }else if (SELECT_INTENT == AFTER_INTENT){
            mLinearAfterListParent.addView(listView);
        }
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
            case R.id.btn_image_save_finish : {

                break;
            }
        }
    }
}