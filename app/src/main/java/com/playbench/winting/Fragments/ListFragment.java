package com.playbench.winting.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.playbench.winting.Adapters.MyListAdapter;
import com.playbench.winting.Adapters.NewRequestAdapter;
import com.playbench.winting.Adapters.Test2Adapter;
import com.playbench.winting.Itmes.EstimateItem;
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
import static com.playbench.winting.Utils.NetworkUtils.ANTECEDENTS_LIST;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.FILM_LIST;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_LIST;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.JsonIntIsNullCheck;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_NO;

public class ListFragment extends Fragment implements AsyncResponse {

    private String                  TAG = "ListFragment";
    private SwipeRefreshLayout      mSwipeRefresh;
    private RecyclerView mListView;
    private Test2Adapter mAdapter;
    private MwSharedPreferences     mPref;
    private int                     PAGE_NUM = 0;
    private int                     PAGE_SHOW_CNT = 15;
    private boolean                 mLastItemVisibleFlag = false;
    private boolean                 mLockListView = false;
    private int                     LIST_TOTAL_CNT = 0;
    private int                     CURRENT_CNT = 0;
    public static int               ESTIMATE_CODE = 1111;
    private ProgressDialog          mProgressDialog;

    public ListFragment (){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPref                       = new MwSharedPreferences(getActivity());

        View v                      = inflater.inflate(R.layout.fragment_list, container, false);
        mSwipeRefresh               = v.findViewById(R.id.swipe_my_list);
        mListView                   = v.findViewById(R.id.list_view_my_list);
        mProgressDialog             = new ProgressDialog(getActivity(),R.style.MyTheme);
        mProgressDialog.setCancelable(false);

        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 0;
                CURRENT_CNT = 0;
                NetworkCall(ANTECEDENTS_LIST);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager)mListView.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCnt = mListView.getAdapter().getItemCount();

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && (CURRENT_CNT < LIST_TOTAL_CNT) && mLockListView) {
                    mLockListView = false;
                    PAGE_NUM++;
                    NetworkCall(ANTECEDENTS_LIST);
                }
            }
        });

        NetworkCall(ANTECEDENTS_LIST);

        return v;
    }

    void NetworkCall(String mCode){
        mProgressDialog.show();
        if (mCode.equals(ANTECEDENTS_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(mPref.getStringValue(USER_NO),""+PAGE_NUM,""+PAGE_SHOW_CNT);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            mProgressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                if (PAGE_NUM == 0){
                    mAdapter = new Test2Adapter(getActivity(),this);
                }
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);

                LIST_TOTAL_CNT = JsonIntIsNullCheck(jsonObject,"total");
                CURRENT_CNT += jsonArray.length();
                if (mCode.equals(ANTECEDENTS_LIST)){
                    JSONArray jsonArrayVisit = null;
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        EstimateItem estimateItem = new EstimateItem();
                        estimateItem.setEstimateNo(JsonIsNullCheck(object,"estimate_no"));
                        estimateItem.setEstimateCode(JsonIsNullCheck(object,"estimate_code"));
                        estimateItem.setOrderNo(JsonIsNullCheck(object,"order_no"));
                        estimateItem.setRegion(JsonIsNullCheck(object,"region"));
                        estimateItem.setProgress(JsonIsNullCheck(object,"progress"));
                        estimateItem.setStartDate(JsonIsNullCheck(object,"start_date"));
                        estimateItem.setEndDate(JsonIsNullCheck(object,"end_date"));
                        estimateItem.setPrice(JsonIsNullCheck(object,"price"));
                        estimateItem.setAddress(JsonIsNullCheck(object,"address"));

                        String filePath = "";
                        if (object.has("visit_image")){
                            if (object.isNull("visit_image")){
                                estimateItem.setVisitImage("");
                            }else{
                                jsonArrayVisit = object.getJSONArray("visit_image");
                                for (int j = 0; j < jsonArrayVisit.length(); j++){
                                    if (j == 0){
                                        filePath += "" + jsonArrayVisit.get(j).toString();
                                    }else{
                                        filePath += "," + jsonArrayVisit.get(j).toString();
                                    }
                                }
                                Log.i(TAG,"visit : " + filePath);
                                estimateItem.setVisitImage(filePath);
                            }
                        }else{
                            estimateItem.setVisitImage("");
                        }

                        mAdapter.addItem(estimateItem);
                    }
                    if (PAGE_NUM == 0){
                        mListView.setAdapter(mAdapter);
                    }
                    mAdapter.notifyDataSetChanged();
                    mLockListView = true;
                }
            }else{
                FragmentManager fragmentManager = getFragmentManager();
                new OneButtonDialog(getString(R.string.Main_Second_Tab),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ESTIMATE_CODE){
            if (resultCode == RESULT_OK){
                PAGE_NUM = 0;
                NetworkCall(ANTECEDENTS_LIST);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
