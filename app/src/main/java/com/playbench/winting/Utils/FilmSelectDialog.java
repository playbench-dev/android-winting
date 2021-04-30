package com.playbench.winting.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.playbench.winting.Activitys.Estimate.FilmInsertActivity;
import com.playbench.winting.Itmes.MyFilmItem;
import com.playbench.winting.R;

import java.util.ArrayList;

public class FilmSelectDialog extends Dialog implements View.OnClickListener {

    private String TAG = "FilmSelectDialog";
    private FilmSelectDialog.OkbuttonClickListener okButtonClickListener = null;
    private FilmSelectDialog.CancelButtonClickListener cancelButtonClickListener = null;
    private FilmSelectDialog.FilmInsertButtonClickListener filmInsertButtonClickListener = null;
    private FilmSelectDialog.TextButtonClickListener textButtonClickListener = null;
    private Button mButtonCancel;
    private Button mButtonDone;
    private TextView mTextFilmInsert;
    private NumberPicker mPicker;
    private Context context;
    private ArrayList<MyFilmItem> myFilmItemArrayList = new ArrayList<>();
    /////////////////////////////////////////////////////////////////////////////////////////////
    public interface OkbuttonClickListener {
        void OkButtonClick(String film, int idx);
    }

    public void setOkButtonClickListener(FilmSelectDialog.OkbuttonClickListener okbuttonClickListener) {
        this.okButtonClickListener = okbuttonClickListener;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public interface CancelButtonClickListener {
        void CancelButtonClick(View v);

    }

    public void setCancelButtonClickListener(FilmSelectDialog.CancelButtonClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
    }

    public interface FilmInsertButtonClickListener {
        void FilmInsertButtonClick(View v);
    }

    public void setfilmInsertButtonClickListener(FilmSelectDialog.FilmInsertButtonClickListener filmInsertButtonClickListener) {
        this.filmInsertButtonClickListener = filmInsertButtonClickListener;
    }

    public interface TextButtonClickListener {
        void TextButtonClickListener(View v);
    }

    public void setTextButtonClickListener(FilmSelectDialog.TextButtonClickListener textButtonClickListener) {
        this.textButtonClickListener = textButtonClickListener;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public FilmSelectDialog(Context context,ArrayList<MyFilmItem> myFilmItemArrayList) {
        super(context);
        this.context = context;
        this.myFilmItemArrayList = myFilmItemArrayList;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View layout = getLayoutInflater().inflate(R.layout.film_select_dialog,null);

        setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        setContentView(layout);

        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mButtonCancel           = findViewById(R.id.btn_dialog_cancel);
        mButtonDone             = findViewById(R.id.btn_dialog_done);
        mTextFilmInsert         = findViewById(R.id.txt_dialog_film_insert);
        mPicker                 = findViewById(R.id.picker_dialog);

        String[] myFilmList = new String[myFilmItemArrayList.size()];
        for (int i = 0; i < myFilmItemArrayList.size(); i++) {
            myFilmList[i] = myFilmItemArrayList.get(i).getFilmName();
        }

        mPicker.setMaxValue(myFilmList.length - 1);
        mPicker.setDisplayedValues(myFilmList);

        mButtonCancel.setOnClickListener(this);
        mButtonDone.setOnClickListener(this);
        mTextFilmInsert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_cancel: {
                if(cancelButtonClickListener !=null){
                    cancelButtonClickListener.CancelButtonClick(v);
                    dismiss();
                }else{
                    dismiss();
                }
                break;
            }
            case R.id.btn_dialog_done: {
                if(okButtonClickListener !=null){
                    okButtonClickListener.OkButtonClick(myFilmItemArrayList.get(mPicker.getValue()).getFilmName(),mPicker.getValue());
                    dismiss();
                }else {
                    dismiss();
                }
                break;
            }
            case R.id.txt_dialog_film_insert : {
                if(textButtonClickListener !=null){
                    textButtonClickListener.TextButtonClickListener(v);
                    dismiss();
                }else{
                    dismiss();
                }
                break;
            }
        }
    }
}
