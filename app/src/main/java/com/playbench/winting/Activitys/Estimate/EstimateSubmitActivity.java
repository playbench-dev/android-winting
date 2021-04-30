package com.playbench.winting.Activitys.Estimate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.Adapters.FilmListAdapter;
import com.playbench.winting.Itmes.MyFilmItem;
import com.playbench.winting.R;
import com.playbench.winting.Utils.FilmSelectDialog;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;
import com.playbench.winting.views.NumberTextWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.FILM_LIST;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.JsonIntIsNullCheck;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_NO;

public class EstimateSubmitActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String                      TAG = "EstimateSubmitActivity";
    private ImageView                   mImageBack;
    private TextView                    mTextInsulation;
    private TextView                    mSheetPaper;
    private LinearLayout                mLinearInsulationVisible;
    private LinearLayout                mLinearSheetPaperVisible;
    private LinearLayout                mLinearInsulationParent;
    private LinearLayout                mLinearSheetPaperParent;
    private LinearLayout                mLinearFilmListParent;
    private LinearLayout                mLinearFilmInsert;
    private Button                      mButtonMove;
    private ArrayList<MyFilmItem>       mMyFilmList = new ArrayList<>();
    private ArrayList<MyFilmItem>       mMyFilmSelectList = new ArrayList<>();
    private ArrayList<EditText>         mEditHebeList = new ArrayList<>();
    private ArrayList<EditText>         mEditPriceList = new ArrayList<>();
    private Intent                      beforeIntent;
    private MwSharedPreferences         mPref;
    private FilmSelectDialog            mFilmSelectDialog;

    private int                         FILM_INSERT = 1111;
    private JSONArray                   mFilmJsonArray = new JSONArray();
    private JSONObject                  mFilmJsonObject = new JSONObject();
    DecimalFormat df = new DecimalFormat("###,###.####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_submit);

        beforeIntent            = getIntent();
        mPref                   = new MwSharedPreferences(this);

        FindViewById();

        Log.i(TAG,"afterSize : " + beforeIntent.getStringExtra("afterSize"));

        if (beforeIntent.hasExtra("afterSize")){
            try {
                JSONArray jsonArray = new JSONArray(beforeIntent.getStringExtra("afterSize"));
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject objectSize = jsonArray.getJSONObject(i);
                    if (JsonIntIsNullCheck(objectSize,"type") == 1){
                        mLinearInsulationVisible.setVisibility(View.VISIBLE);
                    }else{
                        mLinearSheetPaperVisible.setVisibility(View.VISIBLE);
                    }
                    AfterSizeList(JsonIntIsNullCheck(objectSize,"type"),JsonIntIsNullCheck(objectSize,"width"),JsonIntIsNullCheck(objectSize,"height"),JsonIntIsNullCheck(objectSize,"count"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        NetworkCall(FILM_LIST);

        FilmList();
    }

    void FindViewById(){
        mImageBack                      = findViewById(R.id.img_back);
        mTextInsulation                 = findViewById(R.id.txt_estimate_submit_insulation);
        mSheetPaper                     = findViewById(R.id.txt_estimate_submit_sheet);
        mLinearInsulationParent         = findViewById(R.id.lin_estimate_submit_insulation_parent);
        mLinearSheetPaperParent         = findViewById(R.id.lin_estimate_submit_sheet_parent);
        mLinearInsulationVisible        = findViewById(R.id.lin_estimate_submit_insulation_visible);
        mLinearSheetPaperVisible        = findViewById(R.id.lin_estimate_submit_sheet_visible);
        mLinearFilmListParent           = findViewById(R.id.lin_estimate_submit_film_list_parent);
        mLinearFilmInsert               = findViewById(R.id.lin_estimate_submit_film_insert);
        mButtonMove                     = findViewById(R.id.btn_estimate_submit_next_move);

        mImageBack.setOnClickListener(this);
        mLinearFilmInsert.setOnClickListener(this);
        mButtonMove.setOnClickListener(this);
    }

    void FilmList(){
        View listView                   = new View(this);
        listView                        = getLayoutInflater().inflate(R.layout.view_estimate_submit_list_item_film,null);
        TextView mTextFilm              = listView.findViewById(R.id.txt_estimate_submit_list_item_film);
        EditText mEditSquareMeter       = listView.findViewById(R.id.edt_estimate_submit_list_item_square_meter);
        EditText mEditPrice             = listView.findViewById(R.id.edt_estimate_submit_list_item_price);

        mLinearFilmListParent.addView(listView);

        mEditHebeList.add(mEditSquareMeter);
        mEditPriceList.add(mEditPrice);

        final String[] result = {""};

        mEditPrice.addTextChangedListener(new TextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(result[0])){
                    result[0] = df.format(Long.parseLong(s.toString().replaceAll(",", "")));
                    mEditPrice.setText(result[0]);
                    mEditPrice.setSelection(result[0].length());
                }
            }
        });

        mTextFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilmSelectDialog = new FilmSelectDialog(EstimateSubmitActivity.this,mMyFilmList);
                mFilmSelectDialog.setTextButtonClickListener(new FilmSelectDialog.TextButtonClickListener() {
                    @Override
                    public void TextButtonClickListener(View v) {
                        Intent intent = new Intent(EstimateSubmitActivity.this, FilmInsertActivity.class);
                        startActivityForResult(intent,FILM_INSERT);
                    }
                });
                mFilmSelectDialog.setOkButtonClickListener(new FilmSelectDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(String film, int idx) {
                        mTextFilm.setText(film);
                        mMyFilmSelectList.add(mMyFilmList.get(idx));
                    }
                });
            }
        });
    }

    void FilmJsonMake(String filmNo, String brand, String product, String VLT, String UVR, String IRR, String TSER, String asDate, String hebe, String price){
        try {
            mFilmJsonObject = new JSONObject();
            mFilmJsonObject.accumulate("film_no",filmNo);
//            mFilmJsonObject.accumulate("brand",brand);
            mFilmJsonObject.accumulate("film_name",product);
//            mFilmJsonObject.accumulate("vlt",VLT);
//            mFilmJsonObject.accumulate("uvr",UVR);
//            mFilmJsonObject.accumulate("irr",IRR);
//            mFilmJsonObject.accumulate("tser",TSER);
//            mFilmJsonObject.accumulate("as_date",asDate);
            mFilmJsonObject.accumulate("hebe",hebe);
            mFilmJsonObject.accumulate("price",price);

            mFilmJsonArray.put(mFilmJsonObject);
        }catch (JSONException e){

        }
    }

    void AfterSizeList(int flag, int vertical, int horizontal, int paperCnt){   //flag 1 - 단열 2 - 시트지
        View listView               = new View(this);
        listView                    = getLayoutInflater().inflate(R.layout.view_estimate_detail_after_size_list_item,null);
        TextView mTextVertical      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_vertical);
        TextView mTextHorizontal    = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_horizontal);
        TextView mTextPaperCnt      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_paper_cnt);
        TextView mTextSquareMeter   = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_square_meter);

        mTextVertical.setText("" + vertical);
        mTextHorizontal.setText("" + horizontal);
        mTextPaperCnt.setText("" + paperCnt);
        double squareMeter = ((vertical * 10) * (horizontal * 10) / 1000000);
        mTextSquareMeter.setText("" + Math.round(squareMeter));

        if (flag == 1){
            mLinearInsulationParent.addView(listView);
        }else{
            mLinearSheetPaperParent.addView(listView);
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(FILM_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mPref.getStringValue(USER_NO),"1","20");
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(FILM_LIST)){

                    mMyFilmList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++){
                        MyFilmItem myFilmItem = new MyFilmItem();
                        myFilmItem.setFilmNo(JsonIsNullCheck(jsonArray.getJSONObject(i),"film_no"));
                        myFilmItem.setFilmType(JsonIsNullCheck(jsonArray.getJSONObject(i),"type"));
                        myFilmItem.setFilmBrand(JsonIsNullCheck(jsonArray.getJSONObject(i),"brand"));
                        myFilmItem.setFilmName(JsonIsNullCheck(jsonArray.getJSONObject(i),"product"));
                        myFilmItem.setFilmVLT(JsonIsNullCheck(jsonArray.getJSONObject(i),"vlt"));
                        myFilmItem.setFilmUVR(JsonIsNullCheck(jsonArray.getJSONObject(i),"uvr"));
                        myFilmItem.setFilmIRR(JsonIsNullCheck(jsonArray.getJSONObject(i),"irr"));
                        myFilmItem.setFilmTSER(JsonIsNullCheck(jsonArray.getJSONObject(i),"tser"));
                        myFilmItem.setFilmAsDate(JsonIsNullCheck(jsonArray.getJSONObject(i),"as_date"));

                        mMyFilmList.add(myFilmItem);
                    }
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Estimate_Submit_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILM_INSERT){
            if (resultCode == RESULT_OK){
                NetworkCall(FILM_LIST);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.lin_estimate_submit_film_insert : {
                FilmList();
                break;
            }
            case R.id.btn_estimate_submit_next_move : {
                for (int i = 0; i < mMyFilmSelectList.size(); i++){
                    FilmJsonMake(mMyFilmList.get(i).getFilmNo(),mMyFilmList.get(i).getFilmBrand(),mMyFilmList.get(i).getFilmName(),mMyFilmList.get(i).getFilmVLT(),
                            mMyFilmList.get(i).getFilmUVR(),mMyFilmList.get(i).getFilmIRR(),mMyFilmList.get(i).getFilmTSER(),mMyFilmList.get(i).getFilmAsDate(),
                            mEditHebeList.get(i).getText().toString(),mEditPriceList.get(i).getText().toString());
                }
                Intent intent = new Intent(this,EstimateInsertActivity.class);
                intent.putExtra("orderNo",beforeIntent.getStringExtra("orderNo"));
                intent.putExtra("orderCode",beforeIntent.getStringExtra("orderCode"));
                intent.putExtra("address",beforeIntent.getStringExtra("address"));
                intent.putExtra("dueDate",beforeIntent.getStringExtra("dueDate"));
                intent.putExtra("form",beforeIntent.getStringExtra("form"));
                intent.putExtra("filmJson",mFilmJsonArray.toString());
                startActivity(intent);
                break;
            }
        }
    }
}