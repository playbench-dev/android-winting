package com.playbench.winting.Activitys.Estimate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.Activitys.MainActivity;
import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.ESTIMATE_INSERT;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_DETAIL;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.COMPANY_NO;
import static com.playbench.winting.Utils.Util.DueDate;
import static com.playbench.winting.Utils.Util.FormType;
import static com.playbench.winting.Utils.Util.GetFormatDEC;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_NO;

public class EstimateInsertActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String                  TAG = "EstimateInsertActivity";
    private ImageView               mImageBack;
    private TextView                mTextAddress;
    private TextView                mTextDueDate;
    private TextView                mTextForm;
    private LinearLayout            mLinearFilmListParent;
    private TextView                mTextTotalPrice;
    private Button                  mButtonFinish;
    private int                     mTotalPrice = 0;
    private Intent                  beforeIntent;
    private MwSharedPreferences     mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_insert);

        beforeIntent = getIntent();
        mPref = new MwSharedPreferences(this);

        FindViewById();

        mTextAddress.setText(beforeIntent.getStringExtra("address"));
        mTextDueDate.setText(DueDate(beforeIntent.getStringExtra("dueDate")));
        mTextForm.setText(FormType(beforeIntent.getStringExtra("form")));

        Log.i(TAG,"filmList : " + beforeIntent.getStringExtra("filmJson"));

        try {
            JSONArray jsonArray = new JSONArray(beforeIntent.getStringExtra("filmJson"));
            for (int i = 0; i < jsonArray.length(); i++){
                FilmList(jsonArray.getJSONObject(i).getString("film_name"),jsonArray.getJSONObject(i).getString("hebe"),jsonArray.getJSONObject(i).getString("price"));
            }
        }catch (JSONException e){

        }
    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mTextAddress                = findViewById(R.id.txt_estimate_insert_address);
        mTextDueDate                = findViewById(R.id.txt_estimate_insert_due_date);
        mTextForm                   = findViewById(R.id.txt_estimate_insert_form);
        mLinearFilmListParent       = findViewById(R.id.lin_estimate_insert_film_list_parent);
        mTextTotalPrice             = findViewById(R.id.txt_estimate_insert_total_price);
        mButtonFinish               = findViewById(R.id.btn_estimate_insert_finish);

        mImageBack.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);
    }

    void FilmList(String filmName, String squareMeter, String price){
        View listView               = new View(this);
        listView                    = getLayoutInflater().inflate(R.layout.view_estimate_insert_film_list_item,null);
        TextView mTextFilm          = listView.findViewById(R.id.txt_estimate_insert_list_item_film);
        TextView mTextSquareMeter   = listView.findViewById(R.id.txt_estimate_insert_list_item_square_meter);
        TextView mTextPrice         = listView.findViewById(R.id.txt_estimate_insert_list_item_price);

        mLinearFilmListParent.addView(listView);

        mTextFilm.setText(filmName);
        mTextSquareMeter.setText(squareMeter);
        mTextPrice.setText(price);

        mTotalPrice += Integer.parseInt(price.replace(",",""));

        mTextTotalPrice.setText(GetFormatDEC(String.valueOf(mTotalPrice)));
    }

    void NetworkCall(String mCode){
        if (mCode.equals(ESTIMATE_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_NO),getIntent().getStringExtra("orderNo"),
                    beforeIntent.getStringExtra("filmJson"));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ESTIMATE_INSERT)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new OneButtonDialog(getString(R.string.Estimate_Insert_Title), jsonObject.getString(ERROR_NM), new OneButtonDialog.ConfirmButtonListener() {
                        @Override
                        public void confirmButton(View v) {
                            Intent intent = new Intent(EstimateInsertActivity.this, MainActivity.class);
                            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            onBackPressed();
                        }
                    }).show(fragmentManager,TAG);
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Estimate_Insert_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_estimate_insert_finish : {
                NetworkCall(ESTIMATE_INSERT);
                break;
            }
        }
    }
}