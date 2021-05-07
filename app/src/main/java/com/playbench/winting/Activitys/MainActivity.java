package com.playbench.winting.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.Fragments.ListFragment;
import com.playbench.winting.Fragments.MyFilmFragment;
import com.playbench.winting.Fragments.NewRequestFragment;
import com.playbench.winting.Fragments.SettingFragment;
import com.playbench.winting.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "MainActivity";
    private LinearLayout            mLinearTabNewRequest;
    private LinearLayout            mLinearTabList;
    private LinearLayout            mLinearTabMyFilm;
    private LinearLayout            mLinearTabSetting;

    private FragmentManager         mFragmentManager;
    private Fragment                mFragmentNewRequest;
    private Fragment                mFragmentList;
    private Fragment                mFragmentMyFilm;
    private Fragment                mFragmentSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindViewById();

    }

    void FindViewById(){
        mLinearTabNewRequest        = findViewById(R.id.lin_main_tab_new_request);
        mLinearTabList              = findViewById(R.id.lin_main_tab_list);
        mLinearTabMyFilm            = findViewById(R.id.lin_main_tab_my_film);
        mLinearTabSetting           = findViewById(R.id.lin_main_tab_setting);

        mFragmentManager            = getSupportFragmentManager();

        mLinearTabNewRequest.setOnClickListener(this);
        mLinearTabList.setOnClickListener(this);
        mLinearTabMyFilm.setOnClickListener(this);
        mLinearTabSetting.setOnClickListener(this);

        mLinearTabNewRequest.performClick();
    }

    private void tintSelect(LinearLayout lin1,LinearLayout lin2,LinearLayout lin3,LinearLayout lin4){
        lin1.setClickable(false);
        lin2.setClickable(true);
        lin3.setClickable(true);
        lin4.setClickable(true);
        for (int i = 0; i < lin1.getChildCount(); i++){
            if (lin1.getChildAt(i) instanceof TextView){
                TextView textView1 = (TextView)lin1.getChildAt(i);
                textView1.setTextColor(getResources().getColor(R.color.colorPrimary));

                TextView textView2 = (TextView)lin2.getChildAt(i);
                textView2.setTextColor(getResources().getColor(R.color.colorTextColor));

                TextView textView3 = (TextView)lin3.getChildAt(i);
                textView3.setTextColor(getResources().getColor(R.color.colorTextColor));

                TextView textView4 = (TextView)lin4.getChildAt(i);
                textView4.setTextColor(getResources().getColor(R.color.colorTextColor));

            }else if (lin1.getChildAt(i) instanceof ImageView){
                ImageView imageView1 = (ImageView) lin1.getChildAt(i);
                imageView1.setColorFilter(getResources().getColor(R.color.colorPrimary));

                ImageView imageView2 = (ImageView) lin2.getChildAt(i);
                imageView2.setColorFilter(getResources().getColor(R.color.colorTextColor));

                ImageView imageView3 = (ImageView) lin3.getChildAt(i);
                imageView3.setColorFilter(getResources().getColor(R.color.colorTextColor));

                ImageView imageView4 = (ImageView) lin4.getChildAt(i);
                imageView4.setColorFilter(getResources().getColor(R.color.colorTextColor));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lin_main_tab_new_request : {
                tintSelect(mLinearTabNewRequest,mLinearTabList,mLinearTabMyFilm,mLinearTabSetting);

                if(mFragmentNewRequest == null) {
                    mFragmentNewRequest = new NewRequestFragment();
                    mFragmentManager.beginTransaction().add(R.id.frame_layout, mFragmentNewRequest).commit();
                }
                if(mFragmentNewRequest != null) mFragmentManager.beginTransaction().show(mFragmentNewRequest).commit();
                if(mFragmentList != null) mFragmentManager.beginTransaction().hide(mFragmentList).commit();
                if(mFragmentMyFilm != null) mFragmentManager.beginTransaction().hide(mFragmentMyFilm).commit();
                if(mFragmentSetting != null) mFragmentManager.beginTransaction().hide(mFragmentSetting).commit();

                break;
            }
            case R.id.lin_main_tab_list : {
                tintSelect(mLinearTabList,mLinearTabNewRequest,mLinearTabMyFilm,mLinearTabSetting);

                if(mFragmentList == null) {
                    mFragmentList = new ListFragment();
                    mFragmentManager.beginTransaction().add(R.id.frame_layout, mFragmentList).commit();
                }

                if(mFragmentNewRequest != null) mFragmentManager.beginTransaction().hide(mFragmentNewRequest).commit();
                if(mFragmentList != null) mFragmentManager.beginTransaction().show(mFragmentList).commit();
                if(mFragmentMyFilm != null) mFragmentManager.beginTransaction().hide(mFragmentMyFilm).commit();
                if(mFragmentSetting != null) mFragmentManager.beginTransaction().hide(mFragmentSetting).commit();
                break;
            }
            case R.id.lin_main_tab_my_film : {
                tintSelect(mLinearTabMyFilm,mLinearTabList,mLinearTabNewRequest,mLinearTabSetting);

                if(mFragmentMyFilm == null) {
                    mFragmentMyFilm = new MyFilmFragment();
                    mFragmentManager.beginTransaction().add(R.id.frame_layout, mFragmentMyFilm).commit();
                }
                if(mFragmentNewRequest != null) mFragmentManager.beginTransaction().hide(mFragmentNewRequest).commit();
                if(mFragmentList != null) mFragmentManager.beginTransaction().hide(mFragmentList).commit();
                if(mFragmentMyFilm != null) mFragmentManager.beginTransaction().show(mFragmentMyFilm).commit();
                if(mFragmentSetting != null) mFragmentManager.beginTransaction().hide(mFragmentSetting).commit();
                break;
            }
            case R.id.lin_main_tab_setting : {
                tintSelect(mLinearTabSetting,mLinearTabList,mLinearTabMyFilm,mLinearTabNewRequest);

                if(mFragmentSetting == null) {
                    mFragmentSetting = new SettingFragment();
                    mFragmentManager.beginTransaction().add(R.id.frame_layout, mFragmentSetting).commit();
                }
                if(mFragmentNewRequest != null) mFragmentManager.beginTransaction().hide(mFragmentNewRequest).commit();
                if(mFragmentList != null) mFragmentManager.beginTransaction().hide(mFragmentList).commit();
                if(mFragmentMyFilm != null) mFragmentManager.beginTransaction().hide(mFragmentMyFilm).commit();
                if(mFragmentSetting != null) mFragmentManager.beginTransaction().show(mFragmentSetting).commit();
                break;
            }
        }
    }
}