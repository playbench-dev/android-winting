package com.playbench.winting.Activitys.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.playbench.winting.R;

public class LicenseActivity extends AppCompatActivity implements View.OnClickListener {

    private String              TAG = "LicenseActivity";
    private ImageView           mImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        FindViewById();
    }

    void FindViewById(){
        mImageBack              = findViewById(R.id.img_back);

        mImageBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
        }
    }
}