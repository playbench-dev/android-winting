package com.playbench.winting.Activitys.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.playbench.winting.R;

public class AgreeActivity extends AppCompatActivity implements View.OnClickListener {

    private String                      TAG = "AgreeActivity";
    private ImageView                   mImageBack;
    private TextView                    mTextSelect;
    private WebView                     mWebView;
    private String[]                    mCategoryList = new String[]{"서비스 이용 및 회원 가입 약관","개인정보 수집 및 이용에 대한 안내","개인정보 제 3자 제공에 관한 사항"};
    private BottomSheetDialog           mCategoryBottomSheetDialog;
    private View                        mBottomSheetDialogCategory;
    private LinearLayout                mLinearBottomSheetListParent;
    private Button                      mButtonService;
    private Button                      mButtonPersonal;
    private Button                      mButtonSensitivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                      = findViewById(R.id.img_back);
        mTextSelect                     = findViewById(R.id.txt_agree_select);
        mWebView                        = findViewById(R.id.web_view_agree);

        mCategoryBottomSheetDialog      = new BottomSheetDialog(this);
        mBottomSheetDialogCategory      = getLayoutInflater().inflate(R.layout.agree_category_popup, null);
        mLinearBottomSheetListParent    = (LinearLayout)mBottomSheetDialogCategory.findViewById(R.id.lin_agree_category_popup_list_parent);
        mButtonService                  = (Button)mBottomSheetDialogCategory.findViewById(R.id.btn_agree_category_popup_service);
        mButtonPersonal                 = (Button)mBottomSheetDialogCategory.findViewById(R.id.btn_agree_category_popup_personal);
        mButtonSensitivity              = (Button)mBottomSheetDialogCategory.findViewById(R.id.btn_agree_category_popup_sensitivity);

        mCategoryBottomSheetDialog.setContentView(mBottomSheetDialogCategory);

        mButtonService.setText(mCategoryList[0]);
        mButtonPersonal.setText(mCategoryList[1]);
        mButtonSensitivity.setText(mCategoryList[2]);

        mImageBack.setOnClickListener(this);
        mTextSelect.setOnClickListener(this);
        mButtonService.setOnClickListener(this);
        mButtonPersonal.setOnClickListener(this);
        mButtonSensitivity.setOnClickListener(this);

        if (getIntent().hasExtra("flag")){
            if (getIntent().getIntExtra("flag",0) == 1){
                mTextSelect.setText(mCategoryList[0]);
                mWebView.loadUrl("file:///android_asset/service.html");
            }else if (getIntent().getIntExtra("flag",0) == 2){
                mTextSelect.setText(mCategoryList[1]);
                mWebView.loadUrl("file:///android_asset/personal.html");
            }else if (getIntent().getIntExtra("flag",0) == 3){
                mTextSelect.setText(mCategoryList[2]);
                mWebView.loadUrl("file:///android_asset/personal_third.html");
            }
        }else{
            mTextSelect.setText(mCategoryList[0]);
            mWebView.loadUrl("file:///android_asset/service.html");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_agree_select : {
                mCategoryBottomSheetDialog.show();
                break;
            }
            case R.id.btn_agree_category_popup_service : {
                mCategoryBottomSheetDialog.dismiss();
                mTextSelect.setText(mCategoryList[0]);
                mWebView.loadUrl("file:///android_asset/service.html");
                break;
            }
            case R.id.btn_agree_category_popup_personal : {
                mCategoryBottomSheetDialog.dismiss();
                mTextSelect.setText(mCategoryList[1]);
                mWebView.loadUrl("file:///android_asset/personal.html");
                break;
            }
            case R.id.btn_agree_category_popup_sensitivity : {
                mCategoryBottomSheetDialog.dismiss();
                mTextSelect.setText(mCategoryList[2]);
                mWebView.loadUrl("file:///android_asset/personal_third.html");
                break;
            }
        }
    }
}