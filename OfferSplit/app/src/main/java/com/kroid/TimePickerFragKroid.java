package com.kroid;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Kinchit.
 */
public class TimePickerFragKroid extends DialogFragment {
    TimePickerDialog.OnTimeSetListener onTimeSet;
    /** DVL-Up-er.Kroid **/
    public TimePickerFragKroid() {
    }
    /** DVL-Up-er.Kroid **/
    public void setCallBack(TimePickerDialog.OnTimeSetListener ondate) {
        onTimeSet = ondate;
    }
    /** DVL-Up-er.Kroid **/
    private int hourOfDay, minute, gap;
    /** DVL-Up-er.Kroid **/
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hourOfDay = args.getInt("hour");
        minute = args.getInt("minute");
        //gap = args.getInt("gap");
    }
    /** DVL-Up-er.Kroid **/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog dpK = new TimePickerDialog(getActivity(), onTimeSet, hourOfDay, minute, true);
        /*Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, gap);
        dpK.getDatePicker().setMinDate(cal.getTimeInMillis());*/
        return dpK;
    }
}
