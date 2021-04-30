package com.playbench.winting.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.playbench.winting.Activitys.Estimate.FilmInsertActivity;
import com.playbench.winting.Adapters.FilmListAdapter;
import com.playbench.winting.Itmes.MyFilmItem;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.FILM_LIST;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_LIST;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_NO;

public class MyFilmFragment extends Fragment implements View.OnClickListener , AsyncResponse {

    private String              TAG = "MyFilmFragment";
    private ImageView           mImageInsert;
    private ListView            mListView;
    private SwipeRefreshLayout  mSwipeRefresh;
    private FilmListAdapter     mAdapter;

    private int                 PAGE_NUM = 1;
    private int                 PAGE_SHOW_CNT = 15;
    private boolean             mLastItemVisibleFlag = false;
    private boolean             mLockListView = false;

    public static int           FILM_INSERT = 1111;
    public static int           FILM_EDIT = 1112;
    public static int           FILM_DELETE = 1113;

    private MwSharedPreferences mPref;

    public MyFilmFragment (){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPref                   = new MwSharedPreferences(getActivity());

        View v                  = inflater.inflate(R.layout.fragment_my_film, container, false);
        mImageInsert            = v.findViewById(R.id.img_my_film_insert);
        mListView               = v.findViewById(R.id.list_my_film);
        mSwipeRefresh           = v.findViewById(R.id.swipe_my_film);

        NetworkCall(FILM_LIST);

        mImageInsert.setOnClickListener(this);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 1;
                NetworkCall(FILM_LIST);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisibleFlag && mLockListView == false) {
                    PAGE_NUM++;
                    NetworkCall(FILM_LIST);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

        return v;
    }

    void NetworkCall(String mCode){
        if (mCode.equals(FILM_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(mPref.getStringValue(USER_NO),""+PAGE_NUM,""+PAGE_SHOW_CNT);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (PAGE_NUM == 1){
                    mAdapter = new FilmListAdapter(getActivity(),this);
                }
                if (mCode.equals(FILM_LIST)){
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

                        mAdapter.addItem(myFilmItem);
                    }
                    if (PAGE_NUM == 1){
                        mListView.setAdapter(mAdapter);
                    }
                    mAdapter.notifyDataSetChanged();
                    mLockListView = true;
                }
            }else{
                FragmentManager fragmentManager = getFragmentManager();
                new OneButtonDialog(getString(R.string.Main_Third_Tab),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILM_INSERT){
            if (resultCode == RESULT_OK){
                PAGE_NUM = 1;
                NetworkCall(FILM_LIST);
            }
        }else if (requestCode == FILM_EDIT){
            if (resultCode == RESULT_OK){
                Log.i(TAG,"result ok22");
                PAGE_NUM = 1;
                NetworkCall(FILM_LIST);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_my_film_insert : {
                Intent intent = new Intent(getActivity(),FilmInsertActivity.class);
                startActivityForResult(intent,FILM_INSERT);
                break;
            }
        }
    }
}
