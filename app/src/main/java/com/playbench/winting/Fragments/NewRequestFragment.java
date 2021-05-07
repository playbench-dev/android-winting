package com.playbench.winting.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.playbench.winting.Adapters.NewRequestAdapter;
import com.playbench.winting.Adapters.TestAdapter;
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
    private RecyclerView mListView;
    private TestAdapter mAdapter;
    private int                     PAGE_NUM = 0;
    private int                     PAGE_SHOW_CNT = 15;
    private boolean                 mLastItemVisibleFlag = false;
    private boolean                 mLockListView = false;
    private MwSharedPreferences     mPref;
    private ProgressDialog          mProgressDialog;

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
        mProgressDialog             = new ProgressDialog(getActivity(),R.style.MyTheme);
        mProgressDialog.setCancelable(false);

        NetworkCall(ORDER_LIST);

        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 0;
                NetworkCall(ORDER_LIST);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        mListView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int lastVisibleItemPosition = ((LinearLayoutManager)mListView.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCnt = mListView.getAdapter().getItemCount();

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && mLockListView) {
                    PAGE_NUM++;
                    NetworkCall(ORDER_LIST);
                }
            }
        });

//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemVisibleFlag && mLockListView == false) {
//                    PAGE_NUM++;
//                    NetworkCall(ORDER_LIST);
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                mLastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
//            }
//        });

        return v;
    }

    void NetworkCall(String mCode){
        mProgressDialog.show();
        if (mCode.equals(ORDER_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(mPref.getStringValue(USER_NO),""+PAGE_NUM,""+PAGE_SHOW_CNT,mPref.getStringValue(REGION));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            mProgressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(mResult);
            if (jsonObject.getString(ERROR_CD).equals(REQUEST_SUCCESS)){
                if (PAGE_NUM == 0){
                    mAdapter = new TestAdapter(getActivity());
                }
                JSONArray jsonArray = jsonObject.getJSONArray(RESOURCES);
                if (mCode.equals(ORDER_LIST)){
                    JSONArray jsonArrayVisit = null;
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        NewRequestItem newRequestItem = new NewRequestItem();
                        newRequestItem.setmOrderNo(JsonIsNullCheck(object,"order_no"));
                        newRequestItem.setmOrderCode(JsonIsNullCheck(object,"order_code"));
                        newRequestItem.setmRegion(JsonIsNullCheck(object,"region"));
                        newRequestItem.setmProgress("모집");
                        newRequestItem.setmDueDate(JsonIsNullCheck(object,"due_date"));
                        newRequestItem.setmRegDate(JsonIsNullCheck(object,"reg_date"));
                        newRequestItem.setmForm(JsonIsNullCheck(object,"form"));
                        newRequestItem.setmAfterSize(JsonIsNullCheck(object,"after_size"));
                        newRequestItem.setmAddress(JsonIsNullCheck(object,"address"));

                        String filePath = "";
                        if (object.has("visit_image")){
                            if (object.isNull("visit_image")){
                                newRequestItem.setmVisitImage("");
                            }else{
                                jsonArrayVisit = object.getJSONArray("visit_image");
                                for (int j = 0; j < jsonArrayVisit.length(); j++){
                                    if (j == 0){
                                        filePath += "" + jsonArrayVisit.get(j).toString();
                                    }else{
                                        filePath += "," + jsonArrayVisit.get(j).toString();
                                    }
                                }
                                newRequestItem.setmVisitImage(filePath);
                            }
                        }else{
                            newRequestItem.setmVisitImage("");
                        }
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
