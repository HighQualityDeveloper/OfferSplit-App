package com.kroid;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.offersplit.R;

/**
 * Created by Kinchit.
 */
public class AlertTwoAppFragKroid extends DialogFragment {
    onAlertButtonKroid onButtonClick;
    private String message="";
    /** DVL-Up-er.Kroid **/

    public AlertTwoAppFragKroid() {

    }

    /** DVL-Up-er.Kroid **/
    public void setOnButtonClick(onAlertButtonKroid onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args!=null) {
            message = args.getString("message");
        }
    }
    /** DVL-Up-er.Kroid **/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog alertDialog = new Dialog(getActivity(), R.style.CustomDialog);
        View vi = null;
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = inflater.inflate(R.layout.alert_twobutton_xk, null);

        TextView txtAlertMsg = (TextView) vi.findViewById(R.id.txtAlertMsg);
        RelativeLayout relYESbtn = (RelativeLayout) vi.findViewById(R.id.relYESbtn);
        RelativeLayout relNObtn = (RelativeLayout) vi.findViewById(R.id.relNObtn);

        txtAlertMsg.setText(message);
        relYESbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick.onButtonYesClick();
            }
        });
        relNObtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick.onButtonNoClick();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.setContentView(vi);
        alertDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return alertDialog;
    }


    public interface onAlertButtonKroid {
        void onButtonYesClick();
        void onButtonNoClick();
    }
}
