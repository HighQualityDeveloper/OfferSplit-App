package com.kroid;

import android.app.Dialog;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.view.View;
import android.view.LayoutInflater;
import android.graphics.drawable.ColorDrawable;

import com.offersplit.R;

/**
 * Created by Kinchit.
 */
public class AlertAppFragKroid extends DialogFragment {
    onAlertButtonKroid onButtonClick;
    private String message="";
    /** DVL-Up-er.Kroid **/

    public AlertAppFragKroid() {

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
        vi = inflater.inflate(R.layout.alert_onebutton_xk, null);

        TextView txtAlertMsg = (TextView) vi.findViewById(R.id.txtAlertMsg);
        RelativeLayout relOKbtn = (RelativeLayout) vi.findViewById(R.id.relOKbtn);

        txtAlertMsg.setText(message);
        relOKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick.onButtonOkClick();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.setContentView(vi);
        alertDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return alertDialog;
    }

    public interface onAlertButtonKroid {
        void onButtonOkClick();
    }
}
