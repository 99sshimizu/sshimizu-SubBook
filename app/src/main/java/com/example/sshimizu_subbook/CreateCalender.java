package com.example.sshimizu_subbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by SarahS on 2018/01/25.
 */

public class CreateCalender extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year, month, dayOfMonth);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //日付が選択されたときの処理
        String DateStr = year + "/" + (month+1) + "/" + day;


    }

}
