package com.playbench.winting.Activitys.Estimate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.Itmes.MyFilmItem;
import com.playbench.winting.R;
import com.playbench.winting.Utils.FilmSelectDialog;

import java.util.ArrayList;

public class EstimateSubmitActivity extends AppCompatActivity implements View.OnClickListener {

    private String                      TAG = "EstimateSubmitActivity";
    private ImageView                   mImageBack;
    private TextView                    mTextInsulation;
    private TextView                    mSheetPaper;
    private LinearLayout                mLinearInsulationParent;
    private LinearLayout                mLinearSheetPaperParent;
    private LinearLayout                mLinearFilmListParent;
    private LinearLayout                mLinearFilmInsert;
    private Button                      mButtonMove;
    private FilmSelectDialog            mFilmSelectDialog;
    private ArrayList<MyFilmItem>       mMyFilmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_submit);

        FindViewById();

        for (int i = 0; i < 3; i++){
            AfterSizeList(1,12000,23000,10,23);
        }

        for (int i = 0; i < 3; i++){
            AfterSizeList(2,12000,23000,10,23);
        }

        for (int i = 0; i < 10; i++){
            MyFilmItem myFilmItem = new MyFilmItem();
            myFilmItem.setFilmName("아마테라스"+i);
            myFilmItem.setFilmNo(""+i);
            mMyFilmList.add(myFilmItem);
        }


        FilmList();
    }

    void FindViewById(){
        mImageBack                      = findViewById(R.id.img_back);
        mTextInsulation                 = findViewById(R.id.txt_estimate_submit_insulation);
        mSheetPaper                     = findViewById(R.id.txt_estimate_submit_sheet);
        mLinearInsulationParent         = findViewById(R.id.lin_estimate_submit_insulation_parent);
        mLinearSheetPaperParent         = findViewById(R.id.lin_estimate_submit_sheet_parent);
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

        mTextFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilmSelectDialog(EstimateSubmitActivity.this,mMyFilmList).setOkButtonClickListener(new FilmSelectDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(String film) {
                        mTextFilm.setText(film);
                    }
                });
            }
        });
    }

    void AfterSizeList(int flag, int vertical, int horizontal, int paperCnt, int squareMeter){   //flag 1 - 단열 2 - 시트지
        View listView               = new View(this);
        listView                    = getLayoutInflater().inflate(R.layout.view_estimate_detail_after_size_list_item,null);
        TextView mTextVertical      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_vertical);
        TextView mTextHorizontal    = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_horizontal);
        TextView mTextPaperCnt      = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_paper_cnt);
        TextView mTextSquareMeter   = listView.findViewById(R.id.txt_estimate_detail_after_size_list_item_square_meter);

        mTextVertical.setText("" + vertical);
        mTextHorizontal.setText("" + horizontal);
        mTextPaperCnt.setText("" + paperCnt);
        mTextSquareMeter.setText("" + squareMeter);

        if (flag == 1){
            mLinearInsulationParent.addView(listView);
        }else{
            mLinearSheetPaperParent.addView(listView);
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
                Intent intent = new Intent(this,EstimateInsertActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}