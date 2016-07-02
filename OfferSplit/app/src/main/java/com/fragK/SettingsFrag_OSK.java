package com.fragK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;
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
import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SettingsFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static SettingsFrag_OSK tempS;

    ImageView imgHomeBtn;
    SeekBar seekBar;
    SwitchCompat switchWhistle, switchNoti;
    TextView txtRadius;

    boolean curWhitsle, curNoti;
    int curRad;


    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.settings_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempS = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumSettings;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);

        switchWhistle = (SwitchCompat) rootView.findViewById(R.id.switchWhistle);
        switchNoti = (SwitchCompat) rootView.findViewById(R.id.switchNoti);

        txtRadius = (TextView) rootView.findViewById(R.id.txtRadius);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        curRad = spk.getInt(appSetter_OSK.keyRadius, 10);
        //curWhitsle = spk.getBoolean(appSetter_OSK.keyIsWhistle, true);
        curNoti = spk.getBoolean(appSetter_OSK.keyIsNotification, true);

        Log.i("osk", curRad +" "+ curWhitsle +" "+ curNoti);

        txtRadius.setText(curRad + " km");

        seekBar.setProgress(curRad);
        //switchWhistle.setChecked(curWhitsle);
        switchNoti.setChecked(curNoti);

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MasterAct_OSK.tempMaster.hideSoftKeyboardKroid();
                MasterAct_OSK.mMenuDrawer.toggleMenu();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRadius.setText((progress+1) + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if ((seekBar.getProgress()+1) != curRad) {
                    if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                        kttpUpdateProfileK(curWhitsle, curNoti, seekBar.getProgress()+1);
                    }
                }
            }
        });

        switchWhistle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != curWhitsle) {
                    if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                        kttpUpdateProfileK(isChecked, curNoti, curRad);
                    }
                }
            }
        });

        switchNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != curNoti) {
                    if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                        kttpUpdateProfileK(curWhitsle, isChecked, curRad);
                    }
                }
            }
        });

        return rootView;
    }//oncreate =================================================

    public void kttpUpdateProfileK(final boolean whitsle, final boolean notification, final int radius) {
        String url = serviceMasterOSK.getUpdateProfileURL();
        Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            JSONObject jUsr = new JSONObject();
            /*jUsr.put("name", name);
            jUsr.put("email", spk.getString(appSetter_OSK.keyUEmail, ""));
            jUsr.put("phone", phone);

            JSONArray jLoc = new JSONArray();
            jLoc.put(spk.getFloat(appSetter_OSK.keyULat, 0f));
            jLoc.put(spk.getFloat(appSetter_OSK.keyULng, 0f));*/

            JSONObject jSett = new JSONObject();
            //jSett.put("whistle", whitsle);
            jSett.put("radius", radius);
            jSett.put("notification", notification);
            //jSett.put("expiry", spk.getInt(appSetter_OSK.keyExpiry, 0));

            //jUsr.put("location", jLoc);
            //jUsr.put("settings", jSett);

            jBody.put("settings", jSett);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("osk", jBody.toString());

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
                Log.i("osk", "fail" + responseString + ", throwable: " +
                        throwable == null ? "" : throwable.getMessage().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + "\n fail " + throwable == null ? "null" : throwable.toString());
                    Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (statusCode == 401) {
                    MasterAct_OSK.tempMaster.logOutAndOutK();
                } else {
                    try {
                        Log.i("osk", statusCode + " errorResponse " + errorResponse == null ? "null" :
                                errorResponse.toString());
                        if (errorResponse.has("message")) {
                            Toast.makeText(getActivity(), errorResponse.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed. Try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Time out. Try again", Toast.LENGTH_SHORT).show();
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
                    Log.i("osk", statusCode + " content " + response.toString());
                    JSONObject jobject = response;
                    if (statusCode == 200) {
                        if (jobject.has("user")) {
                            JSONObject joUser = jobject.getJSONObject("user");
                            edtr = spk.edit();
                            edtr.putInt(appSetter_OSK.keyRadius, radius);
                            //edtr.putInt(appSetter_OSK.keyExpiry, joSett.getInt("expiry"));
                            //edtr.putBoolean(appSetter_OSK.keyIsWhistle, whitsle);
                            edtr.putBoolean(appSetter_OSK.keyIsNotification, notification);
                            //edtr.putString(appSetter_OSK.keyAccessToken, joUser.getString("accessToken"));
                            edtr.commit();
                            curRad = radius;
                            curWhitsle = whitsle;
                            curNoti = notification;
                        }
                    } else {
                        try {
                            Log.i("osk", statusCode + " errorResponse " + response == null ? "null" :
                                    response.toString());
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
    }//kttpUpdateDealK


}
