package com.playbench.winting.Activitys.Estimate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.playbench.winting.R;

public class FilmInsertActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "FilmInsertActivity";
    private ImageView               mImageBack;
    private RadioButton             mRdoFilm;
    private RadioButton             mRdoSheet;
    private EditText                mEditBrand;
    private EditText                mEditName;
    private LinearLayout            mLinearSpecVisible;
    private EditText                mEditVLT;
    private EditText                mEditUVR;
    private EditText                mEditIRR;
    private EditText                mEditTSER;
    private EditText                mEditGuarantee;
    private Button                  mButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_insert);

        FindViewById();

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mRdoFilm                    = findViewById(R.id.rdo_film_insert_select_film);
        mRdoSheet                   = findViewById(R.id.rdo_film_insert_select_sheet);
        mEditBrand                  = findViewById(R.id.edt_film_insert_brand);
        mEditName                   = findViewById(R.id.edt_film_insert_name);
        mLinearSpecVisible          = findViewById(R.id.lin_film_insert_spec_parent);
        mEditVLT                    = findViewById(R.id.edt_film_insert_vlt);
        mEditUVR                    = findViewById(R.id.edt_film_insert_uvr);
        mEditIRR                    = findViewById(R.id.edt_film_insert_irr);
        mEditTSER                   = findViewById(R.id.edt_film_insert_tser);
        mEditGuarantee              = findViewById(R.id.edt_film_insert_guarantee);
        mButtonSave                 = findViewById(R.id.btn_film_insert_save);

        mImageBack.setOnClickListener(this);
        mRdoFilm.setOnClickListener(this);
        mRdoSheet.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.rdo_film_insert_select_film : {
                mLinearSpecVisible.setVisibility(View.VISIBLE);
                mRdoFilm.setTextColor(getResources().getColor(R.color.white));
                mRdoSheet.setTextColor(getResources().getColor(R.color.colorTextColor));
                break;
            }
            case R.id.rdo_film_insert_select_sheet : {
                mLinearSpecVisible.setVisibility(View.GONE);
                mRdoFilm.setTextColor(getResources().getColor(R.color.colorTextColor));
                mRdoSheet.setTextColor(getResources().getColor(R.color.white));
                break;
            }
            case R.id.btn_film_insert_save : {

                break;
            }
        }
    }
}