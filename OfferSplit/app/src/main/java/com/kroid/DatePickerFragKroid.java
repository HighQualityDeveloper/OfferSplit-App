package com.kroid;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Kinchit.
 */
public class DatePickerFragKroid extends DialogFragment {
    OnDateSetListener ondateSet;
    /** DVL-Up-er.Kroid **/
    public DatePickerFragKroid() {
    }
    /** DVL-Up-er.Kroid **/
    public void setCallBack(OnDateSetListener ondate) {
        ondateSet = ondate;
    }
    /** DVL-Up-er.Kroid **/
    private int year, month, day, gap;
    /** DVL-Up-er.Kroid **/
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        gap = args.getInt("gap");
    }
    /** DVL-Up-er.Kroid **/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dpK = new DatePickerDialog(getActivity(), ondateSet, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.DAY_OF_MONTH, gap);
        dpK.getDatePicker().setMinDate(cal.getTimeInMillis());
        return dpK;
    }
}
