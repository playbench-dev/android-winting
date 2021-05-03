package com.playbench.winting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.playbench.winting.Adapters.NewRequestAdapter;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;
import com.playbench.winting.Utils.MwSharedPreferences;
import com.playbench.winting.Utils.NetworkUtils;
import com.playbench.winting.Utils.OneButtonDialog;
import com.playbench.winting.Utils.ServerManagement.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.playbench.winting.Utils.NetworkUtils.COMPANY_SEARCH;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_CD;
import static com.playbench.winting.Utils.NetworkUtils.ERROR_NM;
import static com.playbench.winting.Utils.NetworkUtils.LOGOUT;
import static com.playbench.winting.Utils.NetworkUtils.ORDER_LIST;
import static com.playbench.winting.Utils.NetworkUtils.REQUEST_SUCCESS;
import static com.playbench.winting.Utils.NetworkUtils.RESOURCES;
import static com.playbench.winting.Utils.NetworkUtils.WITHDRAWAL;
import static com.playbench.winting.Utils.Util.JsonIsNullCheck;
import static com.playbench.winting.Utils.Util.REGION;
import static com.playbench.winting.Utils.Util.USER_NO;

public class NewRequestFragment extends Fragment implements AsyncResponse {

    private String                  TAG = "NewRequestFragment";
    private SwipeRefreshLayout      mSwipeRefresh;
    private ListView                mListView;
    private NewRequestAdapter       mAdapter;
    private int                     PAGE_NUM = 0;
    private int                     PAGE_SHOW_CNT = 15;
    private boolean                 mLastItemVisibleFlag = false;
    private boolean                 mLockListView = false;
    private MwSharedPreferences     mPref;

    public NewRequestFragment (){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v                      = inflater.inflate(R.layout.fragment_new_request, container, false);
        mSwipeRefresh               = v.findViewById(R.id.swipe_new_request);
        mListView                   = v.findViewById(R.id.list_view_new_request);

        mPref                       = new MwSharedPreferences(getActivity());

        NetworkCall(ORDER_LIST);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 0;
                NetworkCall(ORDER_LIST);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisibleFlag && mLockListView == false) {
                    PAGE_NUM++;
                    NetworkCall(ORDER_LIST);
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
        if (mCode.equals(ORDER_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(mPref.getStringValue(USER_NO),""+PAGE_NUM,""+PAGE_SHOW_CNT,mPref.getStringValue(REGION));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                if (PAGE_NUM == 0){
                    mAdapter = new NewRequestAdapter(getActivity());
                }
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ORDER_LIST)){
                    for (int i = 0; i < jsonArray.length(); i++){
                        NewRequestItem newRequestItem = new NewRequestItem();
                        newRequestItem.setmOrderNo(JsonIsNullCheck(jsonArray.getJSONObject(i),"order_no"));
                        newRequestItem.setmOrderCode(JsonIsNullCheck(jsonArray.getJSONObject(i),"order_code"));
                        newRequestItem.setmRegion(JsonIsNullCheck(jsonArray.getJSONObject(i),"region"));
                        newRequestItem.setmProgress("모집");
                        newRequestItem.setmDueDate(JsonIsNullCheck(jsonArray.getJSONObject(i),"due_date"));
                        newRequestItem.setmRegDate(JsonIsNullCheck(jsonArray.getJSONObject(i),"reg_date"));
                        newRequestItem.setmForm(JsonIsNullCheck(jsonArray.getJSONObject(i),"form"));
                        newRequestItem.setmAfterSize(JsonIsNullCheck(jsonArray.getJSONObject(i),"after_size"));

                        mAdapter.addItem(newRequestItem);
                    }
                    if (PAGE_NUM == 0){
                        mListView.setAdapter(mAdapter);
                    }
                    mAdapter.notifyDataSetChanged();
                    mLockListView = true;
                }
            }else{
                FragmentManager fragmentManager = getFragmentManager();
                new OneButtonDialog(getString(R.string.Main_First_Tab),jsonObject.getString(ERROR_NM),null).show(fragmentManager,TAG);
            }
        }catch (JSONException e){

        }
    }
}
