package com.playbench.winting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.playbench.winting.Adapters.NewRequestAdapter;
import com.playbench.winting.Itmes.NewRequestItem;
import com.playbench.winting.R;

public class NewRequestFragment extends Fragment {

    private String              TAG = "NewRequestFragment";
    private ListView            mListView;
    private NewRequestAdapter   mAdapter;

    public NewRequestFragment (){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v                  = inflater.inflate(R.layout.fragment_new_request, container, false);
        mListView               = v.findViewById(R.id.list_view_new_request);

        mAdapter                = new NewRequestAdapter(getActivity());

        for (int i = 0; i < 20; i++){
            NewRequestItem newRequestItem = new NewRequestItem();
            newRequestItem.setmRegion("지역"+i);
            newRequestItem.setmProgress("진행상태");
            newRequestItem.setmDueDate("예정일");
            newRequestItem.setmRegDate("등록일");
            if (i % 2 == 0){
                newRequestItem.setmForm("Y");
            }else{
                newRequestItem.setmForm("N");
            }
            mAdapter.addItem(newRequestItem);
        }

        mListView.setAdapter(mAdapter);

        return v;
    }
}
