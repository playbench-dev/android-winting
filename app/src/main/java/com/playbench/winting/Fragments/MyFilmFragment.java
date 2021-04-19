package com.playbench.winting.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.playbench.winting.Activitys.Estimate.FilmInsertActivity;
import com.playbench.winting.R;

public class MyFilmFragment extends Fragment {

    private String              TAG = "MyFilmFragment";
    private ImageView           mImageInsert;
    private LinearLayout        mLinearListParent;

    public MyFilmFragment (){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v                  = inflater.inflate(R.layout.fragment_my_film, container, false);
        mImageInsert            = v.findViewById(R.id.img_my_film_insert);
        mLinearListParent       = v.findViewById(R.id.lin_my_film_insert_list_item);

        for (int i = 0; i < 10; i++){
            FilmList(""+i, "brand"+i, "name"+i,""+i,""+i,""+i,""+i,i%2);
        }

        return v;
    }

    void FilmList(String filmNo, String brand, String name, String irr, String vlt, String uvr, String tser, int type){
        View listView           = new View(getActivity());
        listView                = getLayoutInflater().inflate(R.layout.view_my_film_list_item,null);
        TextView mTextBrand     = listView.findViewById(R.id.txt_my_film_list_item_brand);
        TextView mTextName      = listView.findViewById(R.id.txt_my_film_list_item_name);
        TextView mTextIRR       = listView.findViewById(R.id.txt_my_film_list_item_irr);
        TextView mTextVLT       = listView.findViewById(R.id.txt_my_film_list_item_vlt);
        TextView mTextUVR       = listView.findViewById(R.id.txt_my_film_list_item_uvr);
        TextView mTextTSER      = listView.findViewById(R.id.txt_my_film_list_item_tser);
        LinearLayout mLinearVisible = listView.findViewById(R.id.lin_my_film_list_item_visible);

        mTextBrand.setText(getString(R.string.My_Film_Brand) + " " + getString(R.string.My_Film_Middle) + " " + brand);
        mTextName.setText(getString(R.string.My_Film_Name) + " " + getString(R.string.My_Film_Middle) + " " + name);
        mTextIRR.setText(getString(R.string.My_Film_IRR) + " " + getString(R.string.My_Film_Middle) + " " + irr + " " + getString(R.string.My_Film_Percent));
        mTextVLT.setText(getString(R.string.My_Film_VRT) + " " + getString(R.string.My_Film_Middle) + " " + vlt + " " + getString(R.string.My_Film_Percent));
        mTextUVR.setText(getString(R.string.My_Film_UVR) + " " + getString(R.string.My_Film_Middle) + " " + uvr + " " + getString(R.string.My_Film_Percent));
        mTextTSER.setText(getString(R.string.My_Film_TSER) + " " + getString(R.string.My_Film_Middle) + " " + tser + " " + getString(R.string.My_Film_Percent));

        if (type == 1){
            mLinearVisible.setVisibility(View.VISIBLE);
        }else{
            mLinearVisible.setVisibility(View.GONE);
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilmInsertActivity.class);
                startActivity(intent);
            }
        });

        mLinearListParent.addView(listView);
    }
}
