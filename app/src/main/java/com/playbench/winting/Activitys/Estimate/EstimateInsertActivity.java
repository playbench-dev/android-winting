package com.playbench.winting.Activitys.Estimate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.playbench.winting.R;

public class EstimateInsertActivity extends AppCompatActivity implements View.OnClickListener {

    private String                  TAG = "EstimateInsertActivity";
    private ImageView               mImageBack;
    private TextView                mTextAddress;
    private TextView                mTextDueDate;
    private TextView                mTextForm;
    private LinearLayout            mLinearFilmListParent;
    private TextView                mTextTotalPrice;
    private Button                  mButtonFinish;
    private int                     mTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_insert);

        FindViewById();

        mTextAddress.setText("미사강변한강로 295번지 605호");
        mTextDueDate.setText("2021-05-06");
        mTextForm.setText("오피스텔");

        for (int i = 0; i < 4; i++){
            FilmList("아마테라스","90","530000");
        }

    }

    void FindViewById(){
        mImageBack                  = findViewById(R.id.img_back);
        mTextAddress                = findViewById(R.id.txt_estimate_insert_address);
        mTextDueDate                = findViewById(R.id.txt_estimate_insert_due_date);
        mTextForm                   = findViewById(R.id.txt_estimate_insert_form);
        mLinearFilmListParent       = findViewById(R.id.lin_estimate_insert_film_list_parent);
        mTextTotalPrice             = findViewById(R.id.txt_estimate_insert_total_price);
        mButtonFinish               = findViewById(R.id.btn_estimate_insert_finish);
    }

    void FilmList(String filmName, String squareMeter, String price){
        View listView               = new View(this);
        listView                    = getLayoutInflater().inflate(R.layout.view_estimate_insert_film_list_item,null);
        TextView mTextFilm          = listView.findViewById(R.id.txt_estimate_insert_list_item_film);
        TextView mTextSquareMeter   = listView.findViewById(R.id.txt_estimate_insert_list_item_square_meter);
        TextView mTextPrice         = listView.findViewById(R.id.txt_estimate_insert_list_item_price);

        mLinearFilmListParent.addView(listView);

        mTextFilm.setText(filmName);
        mTextSquareMeter.setText(squareMeter);
        mTextPrice.setText(price);

        mTotalPrice += Integer.parseInt(price);

        mTextTotalPrice.setText(""+mTotalPrice);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_estimate_insert_finish : {

                break;
            }
        }
    }
}