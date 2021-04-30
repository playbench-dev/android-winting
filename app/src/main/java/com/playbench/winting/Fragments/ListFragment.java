package com.playbench.winting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.playbench.winting.Adapters.MyListAdapter;
import com.playbench.winting.Adapters.NewRequestAdapter;
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

import static com.playbench.winting.Utils.NetworkUtils.ANTECEDENTS_LIST;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_LIST;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.USER_NO;

public class ListFragment extends Fragment implements AsyncResponse {

    private String                  TAG = "ListFragment";
    private ListView                mListView;
    private MyListAdapter           mAdapter;
    private MwSharedPreferences     mPref;
    private int                     PAGE_NUM = 1;
    private int                     PAGE_SHOW_CNT = 15;
    private boolean                 mLastItemVisibleFlag = false;
    private boolean                 mLockListView = false;

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
        mListView                   = v.findViewById(R.id.list_view_my_list);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisibleFlag && mLockListView == false) {
                    PAGE_NUM++;
                    NetworkCall(ANTECEDENTS_LIST);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

        NetworkCall(ANTECEDENTS_LIST);

        return v;
    }

    void NetworkCall(String mCode){
        if (mCode.equals(ANTECEDENTS_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(mPref.getStringValue(USER_NO),""+PAGE_NUM,""+PAGE_SHOW_CNT);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                if (PAGE_NUM == 1){
                    mAdapter = new MyListAdapter(getActivity());
                }
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ANTECEDENTS_LIST)){
                    for (int i = 0; i < jsonArray.length(); i++){
                        EstimateItem estimateItem = new EstimateItem();
                        estimateItem.setEstimateNo(JsonIsNullCheck(jsonArray.getJSONObject(i),"estimate_no"));
                        estimateItem.setEstimateCode(JsonIsNullCheck(jsonArray.getJSONObject(i),"estimate_code"));
                        estimateItem.setOrderNo(JsonIsNullCheck(jsonArray.getJSONObject(i),"order_no"));
                        estimateItem.setRegion(JsonIsNullCheck(jsonArray.getJSONObject(i),"region"));
                        estimateItem.setProgress(JsonIsNullCheck(jsonArray.getJSONObject(i),"progress"));
                        estimateItem.setStartDate(JsonIsNullCheck(jsonArray.getJSONObject(i),"start_date"));
                        estimateItem.setEndDate(JsonIsNullCheck(jsonArray.getJSONObject(i),"end_date"));
                        estimateItem.setPrice(JsonIsNullCheck(jsonArray.getJSONObject(i),"price"));

                        mAdapter.addItem(estimateItem);
                    }
                    if (PAGE_NUM == 1){
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
}
