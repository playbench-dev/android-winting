package com.playbench.winting.Activitys.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.playbench.winting.R;

public class AgreeActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "AgreeActivity";
    private ImageView               mImageBack;
    private TextView                mTextSelect;
    private WebView                 mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mTextSelect                 = findViewById(R.id.txt_agree_select);
        mWebView                    = findViewById(R.id.web_view_agree);

        mImageBack.setOnClickListener(this);
        mTextSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_agree_select : {

                break;
            }
        }
    }
}