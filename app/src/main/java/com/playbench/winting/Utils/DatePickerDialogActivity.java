package com.playbench.winting.Utils;

import android.app.Activity;
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
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.playbench.winting.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerDialogActivity extends DialogFragment {

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private ConfirmButtonListener confirmButtonListener;
    String dt = "";

    public DatePickerDialogActivity(String dt, ConfirmButtonListener confirmButtonListener) {
        this.confirmButtonListener = confirmButtonListener;
        this.dt = dt;
    }

    public DatePickerDialogActivity(ConfirmButtonListener confirmButtonListener) {
        this.confirmButtonListener = confirmButtonListener;
    }

    public DatePickerDialogActivity() {
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
        datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
        cancelButton = (Button) dialog.findViewById(R.id.btn_date_picker_cancel);
        confirmButton = (Button) dialog.findViewById(R.id.btn_date_picker_done);

        datePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9);
        int dialogHeight = (int) (metrics.heightPixels * 0.5);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);
        final Calendar pickedDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        },
                pickedDate.get(Calendar.YEAR),
                pickedDate.get(Calendar.MONTH),
                pickedDate.get(Calendar.DAY_OF_MONTH)

        );

        if (dt.length() > 0){
            datePicker.updateDate(Integer.parseInt(dt.substring(0,4)),Integer.parseInt(dt.substring(4,6))-1,Integer.parseInt(dt.substring(6,8)));
        }

        maxDate.set(pickedDate.get(Calendar.YEAR),pickedDate.get(Calendar.MONTH),pickedDate.get(Calendar.DAY_OF_MONTH));
        datePicker.setMaxDate(maxDate.getTimeInMillis());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        datePickerDialog.setCancelable(false);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String confirmDate = format1.format(newDate.getTime());
                confirmButtonListener.confirmButton(confirmDate);
                dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_date_picker_dialog, container, false);

        return v;
    }

    public interface ConfirmButtonListener {
        void confirmButton(String data);
    }
}
