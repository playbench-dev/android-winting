package com.playbench.winting.Activitys.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.playbench.winting.R;

public class CompanySearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "CompanySearchActivity";
    private EditText                mEditSearch;
    private ImageView               mImageClear;
    private TextView                mTextClose;
    private ListView                mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);

        FindViewById();

    }

    void FindViewById(){
        mEditSearch                 = findViewById(R.id.edt_company_search_text);
        mImageClear                 = findViewById(R.id.img_company_search_clear);
        mTextClose                  = findViewById(R.id.txt_company_search_close);
        mListView                   = findViewById(R.id.list_view_company_search);

        mImageClear.setOnClickListener(this);
        mTextClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_company_search_close : {
                onBackPressed();
                break;
            }
            case R.id.img_company_search_clear : {
                mEditSearch.setText("");
                break;
            }
        }
    }
}