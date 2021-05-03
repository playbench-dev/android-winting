package com.playbench.winting.Activitys.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.playbench.winting.Adapters.CompanyListAdapter;
import com.playbench.winting.Itmes.CompanyListItem;
import com.playbench.winting.R;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.playbench.winting.Utils.NetworkUtils.COMPANY_SEARCH;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.LOGIN;
import static com.playbench.winting.Utils.NetworkUtils.REGISTER;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_TOKEN;

public class CompanySearchActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String                  TAG = "CompanySearchActivity";
    private EditText                mEditSearch;
    private ImageView               mImageClear;
    private TextView                mTextClose;
    private ListView                mListView;
    private CompanyListAdapter      mCompanyAdapter;
    private int                     PAGE_NUM = 0;
    private int                     PAGE_SHOW_CNT = 15;
    private boolean                 mLastItemVisibleFlag = false;
    private boolean                 mLockListView = false;
    private String                  mSearchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);

        FindViewById();

        NetworkCall(COMPANY_SEARCH);

    }

    void FindViewById(){
        mEditSearch                 = findViewById(R.id.edt_company_search_text);
        mImageClear                 = findViewById(R.id.img_company_search_clear);
        mTextClose                  = findViewById(R.id.txt_company_search_close);
        mListView                   = findViewById(R.id.list_view_company_search);

        mImageClear.setOnClickListener(this);
        mTextClose.setOnClickListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisibleFlag && mLockListView == false) {
                    PAGE_NUM++;
                    NetworkCall(COMPANY_SEARCH);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                PAGE_NUM = 0;
                mSearchText = charSequence.toString();
                NetworkCall(COMPANY_SEARCH);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    void NetworkCall(String mCode){
        if (mCode.equals(COMPANY_SEARCH)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mSearchText,""+PAGE_NUM,""+PAGE_SHOW_CNT);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(COMPANY_SEARCH)){
                    if (PAGE_NUM == 0){
                        mCompanyAdapter = new CompanyListAdapter(this);
                    }
                    for (int i = 0; i < jsonArray.length(); i++){
                        CompanyListItem companyListItem = new CompanyListItem();
                        companyListItem.setmCompanyNo(JsonIsNullCheck(jsonArray.getJSONObject(i),"company_no"));
                        companyListItem.setmCompanyName(JsonIsNullCheck(jsonArray.getJSONObject(i),"company_name"));
                        companyListItem.setmOwnerName(JsonIsNullCheck(jsonArray.getJSONObject(i),"owner_name"));
                        companyListItem.setmContact(JsonIsNullCheck(jsonArray.getJSONObject(i),"contact"));
                        companyListItem.setmCorporateNumber(JsonIsNullCheck(jsonArray.getJSONObject(i),"corporate_number"));

                        mCompanyAdapter.addItem(companyListItem);
                    }
                    if (PAGE_NUM == 0){
                        mListView.setAdapter(mCompanyAdapter);
                    }
                    mLockListView = true;
                    mCompanyAdapter.notifyDataSetChanged();
                }
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                new OneButtonDialog(getString(R.string.Register_Company_Title),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }
}