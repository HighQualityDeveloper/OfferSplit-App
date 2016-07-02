package com.fragK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
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

public class ProfileFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static ProfileFrag_OSK tempP;

    ImageView imgHomeBtn;
    TextView txtSaveBtn;
    EditText edFullName, edPhoneNum;


    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempP = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumProfile;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        txtSaveBtn = (TextView) rootView.findViewById(R.id.txtSaveBtn);

        edFullName = (EditText) rootView.findViewById(R.id.edFullName);
        edPhoneNum = (EditText) rootView.findViewById(R.id.edPhoneNum);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        edFullName.setText(spk.getString(appSetter_OSK.keyUName, ""));
        edPhoneNum.setText(spk.getString(appSetter_OSK.keyUPhone, ""));

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.hideSoftKeyboardKroid();
                MasterAct_OSK.mMenuDrawer.toggleMenu();
            }
        });

        txtSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.hideSoftKeyboardKroid();
                if (validateK()) {
                    if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                        kttpUpdateProfileK(edFullName.getText().toString(), edPhoneNum.getText().toString());
                    }
                }
            }
        });

        return rootView;
    }//oncreate =================================================

    public boolean validateK() {
        if (edFullName.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a full name",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edPhoneNum.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a password",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }//validateK

    public void kttpUpdateProfileK(String name, final String phone) {
        String url = serviceMasterOSK.getUpdateProfileURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            //JSONObject jUsr = new JSONObject();
            jBody.put("name", name);
            //jUsr.put("email", spk.getString(appSetter_OSK.keyUEmail, ""));
            if (!phone.equals(spk.getString(appSetter_OSK.keyUPhone, ""))) {
                jBody.put("phone", phone);
            }

            /*JSONArray jLoc = new JSONArray();
            jLoc.put(spk.getFloat(appSetter_OSK.keyULat, 0f));
            jLoc.put(spk.getFloat(appSetter_OSK.keyULng, 0f));

            JSONObject jSett = new JSONObject();
            jSett.put("whistle", spk.getBoolean(appSetter_OSK.keyIsWhistle, true));
            jSett.put("radius", spk.getInt(appSetter_OSK.keyRadius, 10));
            jSett.put("notification", spk.getBoolean(appSetter_OSK.keyIsNotification, true));
            jSett.put("expiry", spk.getInt(appSetter_OSK.keyExpiry, 0));

            jUsr.put("location", jLoc);
            jUsr.put("settings", jSett);*/

            //jBody.put("user", jUsr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("osk", jBody.toString());

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
                Log.i("osk", "fail" );
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("osk", statusCode + "- fail ");
                //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                if (statusCode == 401) {
                    MasterAct_OSK.tempMaster.logOutAndOutK();
                } else {
                    try {
                        Log.i("osk", statusCode + " errorResponse " );
                        if (errorResponse.has("message")) {
                            Toast.makeText(getActivity(), errorResponse.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Fail. Try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Fail,", Toast.LENGTH_SHORT).show();
                    }
                }
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
                try {
                    Log.i("osk", statusCode + " content ");
                    JSONObject jobject = response;
                    if (statusCode == 200) {
                        if (jobject.has("user")) {
                            JSONObject joUser = jobject.getJSONObject("user");

                            edtr = spk.edit();
                            edtr.putString(appSetter_OSK.keyUName, joUser.getString("name"));
                            edtr.putString(appSetter_OSK.keyUPhone, joUser.getString("phone"));
                            //edtr.putString(appSetter_OSK.keyAccessToken, joUser.getString("accessToken"));
                            edtr.commit();

                            final AlertAppFragKroid alert = new AlertAppFragKroid();
                            Bundle b = new Bundle();
                            b.putString("message", getResources().getString(R.string.popProUpdateSuc));
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
                    } else {
                        try {
                            Log.i("osk", statusCode + " errorResponse ");
                            if (response.has("message")) {
                                Toast.makeText(getActivity(), response.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Fail. Try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Fail,", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }//kttpUpdateProfileK

}
