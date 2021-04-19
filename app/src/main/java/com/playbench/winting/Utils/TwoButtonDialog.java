package com.playbench.winting.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.playbench.winting.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TwoButtonDialog extends DialogFragment {

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private ConfirmButtonListener confirmButtonListener;
    String title = "";
    String contents = "";

    public TwoButtonDialog(String title, String contents, ConfirmButtonListener confirmButtonListener) {
        this.confirmButtonListener = confirmButtonListener;
        this.title = title;
        this.contents = contents;
    }

    public TwoButtonDialog(ConfirmButtonListener confirmButtonListener) {
        this.confirmButtonListener = confirmButtonListener;
    }

    public TwoButtonDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button cancelButton, confirmButton;
        final DatePicker datePicker;
        final Dialog dialog = getDialog();
        TextView mTextTitle = (TextView) dialog.findViewById(R.id.txt_two_btn_dialog_title);
        TextView mTextContents = (TextView) dialog.findViewById(R.id.txt_two_btn_dialog_contents);
        cancelButton = (Button) dialog.findViewById(R.id.btn_two_btn_dialog_cancel);
        confirmButton = (Button) dialog.findViewById(R.id.btn_two_btn_dialog_done);

        mTextTitle.setText(title);
        mTextContents.setText(contents);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9);
        int dialogHeight = (int) (metrics.heightPixels * 0.5);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(confirmButtonListener !=null){
                    confirmButtonListener.confirmButton(v);
                    dismiss();
                }else{
                    dismiss();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_two_btn_dialog, container, false);

        return v;
    }

    public interface ConfirmButtonListener {
        void confirmButton(View v);
    }


}

