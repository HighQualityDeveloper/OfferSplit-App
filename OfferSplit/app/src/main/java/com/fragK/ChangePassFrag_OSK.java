package com.fragK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.actK.MasterAct_OSK;
import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.kroid.AlertAppFragKroid;
import com.kroid.menudrawer.MenuDrawer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.offersplit.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ChangePassFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static ChangePassFrag_OSK tempCP;

    ImageView imgHomeBtn;
    TextView txtSaveBtn;
    EditText edCurrPassword, edNewPassword, edConfrimPassword;


    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.change_pass_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempCP = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumChangePass;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        txtSaveBtn = (TextView) rootView.findViewById(R.id.txtSaveBtn);

        edCurrPassword = (EditText) rootView.findViewById(R.id.edCurrPassword);
        edNewPassword = (EditText) rootView.findViewById(R.id.edNewPassword);
        edConfrimPassword = (EditText) rootView.findViewById(R.id.edConfrimPassword);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------



        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.mMenuDrawer.toggleMenu();
            }
        });

        txtSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.hideSoftKeyboardKroid();
                if (validateK()) {
                    if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                        kttpChangePassK(edCurrPassword.getText().toString(),
                                edNewPassword.getText().toString());
                    }
                }
            }
        });

        return rootView;
    }//oncreate =================================================

    public boolean validateK() {
        if (edCurrPassword.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter current password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edNewPassword.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter new password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edNewPassword.getText().toString().length()<6) {
            Toast.makeText(getActivity(), "A minimum of 6 characters is required for the password",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edConfrimPassword.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Confirm new password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!edConfrimPassword.getText().toString().equals(edNewPassword.getText().toString())) {
            Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }//validateK

    public void kttpChangePassK(final String old, final String newP) {
        String url = serviceMasterOSK.getChangePasswordURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            JSONObject jUsr = new JSONObject();
            jUsr.put("old", old);
            jUsr.put("new", newP);

            jBody.put("user", jUsr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type", "application/json");
        kttp.addHeader("Authorization", spk.getString(appSetter_OSK.keyAccessToken, ""));

        kttp.post(getActivity(), url, entity, "application/json", new JsonHttpResponseHandler() {
            ProgressDialog pd = new ProgressDialog(getActivity());

            @Override
            public void onStart() {
                super.onStart();
                pd.setMessage(getResources().getString(R.string.sLoading));
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("osk", "fail");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + "- fail " );
                   // Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                    if (statusCode == 401) {
                        MasterAct_OSK.tempMaster.logOutAndOutK();
                    }
                    if (errorResponse.has("message")) {
                        Toast.makeText(getActivity(), errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                /*if (errorResponse.has("message")) {
                    try {
                        Toast.makeText(getActivity(), errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("osk", statusCode + " content " );
                if (statusCode == 200) {
                    edtr = spk.edit();
                    edtr.putString(appSetter_OSK.keyUPassword, newP);
                    edtr.commit();

                    edCurrPassword.setText("");
                    edNewPassword.setText("");
                    edConfrimPassword.setText("");

                    final AlertAppFragKroid alert = new AlertAppFragKroid();
                    Bundle b = new Bundle();
                    b.putString("message", getResources().getString(R.string.popPassChangeSuc));
                    alert.setArguments(b);
                    alert.setOnButtonClick(new AlertAppFragKroid.onAlertButtonKroid() {
                        @Override
                        public void onButtonOkClick() {
                            alert.dismiss();
                        }
                    });
                    alert.setCancelable(false);
                    alert.show(getFragmentManager(), "Alert One");
                }
            }
        });
    }//kttpChangePassK

}
