package com.fragK;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.DatePicker;
import android.app.DatePickerDialog;

import com.actK.MasterAct_OSK;
import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.kroid.AlertAppFragKroid;
import com.kroid.DatePickerFragKroid;
import com.kroid.GPSTrackerKroid;
import com.kroid.TimePickerFragKroid;
import com.kroid.menudrawer.MenuDrawer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.objectK.objDealMasterK;
import com.offersplit.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AddDealFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static AddDealFrag_OSK tempA;

    ImageView imgHomeBtn;
    RelativeLayout relPostBtn, relCancelBtn, relExpDate;
    EditText edStoreName, edDescription, edPrice, edComment;
    TextView txtExpDate;

    public static String strDateYMD="", strDateDMY="", strDateMDY="", strTime24=""; // kroid Date
    Calendar calender = Calendar.getInstance();
    int yearDialog = calender.get(Calendar.YEAR);
    int monthDialog = calender.get(Calendar.MONTH);
    int dayDialog = calender.get(Calendar.DAY_OF_MONTH);
    int hourDialog = calender.get(Calendar.HOUR_OF_DAY);
    int minuteDialog = calender.get(Calendar.MINUTE);
    int hourCount = 0;

    public static double latitudeK, longitudeK;
    public static String strLatitude="", strLongitudeK="";
    public static String cityK, countryK;
    GPSTrackerKroid gps;

    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_deal_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempA = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumAddDeal;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        txtExpDate = (TextView) rootView.findViewById(R.id.txtExpDate);

        edStoreName = (EditText) rootView.findViewById(R.id.edStoreName);
        edDescription = (EditText) rootView.findViewById(R.id.edDescription);
        edPrice = (EditText) rootView.findViewById(R.id.edPrice);
        edComment = (EditText) rootView.findViewById(R.id.edComment);

        relPostBtn = (RelativeLayout) rootView.findViewById(R.id.relPostBtn);
        relCancelBtn = (RelativeLayout) rootView.findViewById(R.id.relCancelBtn);
        relExpDate = (RelativeLayout) rootView.findViewById(R.id.relExpDate);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        gps = new GPSTrackerKroid(getActivity());
        strLatitude="";
        strLongitudeK="";

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        relPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateK()) {
                    if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                        kttpAddDealK(edStoreName.getText().toString(),
                                edDescription.getText().toString(),
                                Integer.parseInt(edPrice.getText().toString()), hourCount+"",
                                edComment.getText().toString());
                    }
                }
            }
        });

        relCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        relExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDatePickerKroid();
                alertHourSelectionK();
            }
        });

        return rootView;
    }//oncreate =================================================

    public boolean validateK() {
        if (edStoreName.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a store name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edDescription.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a description", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edPrice.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a price", Toast.LENGTH_SHORT).show();
            return false;
        } else if (hourCount < 1) {
            Toast.makeText(getActivity(), "Select an expiry hours", Toast.LENGTH_SHORT).show();
            return false;
        }/* else if (strDateMDY.length()==0 || strTime24.length()==0) {
            Toast.makeText(getActivity(), "Select an expiry date & time", Toast.LENGTH_SHORT).show();
            return false;
        }*/ else if (edComment.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a comment", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strLatitude.length()==0 && strLongitudeK.length()==0) {
            callGPSK();
            return false;
        }
        return true;
    }//validateK

    private void callGPSK() {
        gps.getLocationKroid(new GPSTrackerKroid.onLocationCallKroid() {
            @Override
            public void onGetLocationKroid(double latitude, double longitude) {
                latitudeK = latitude;
                longitudeK = longitude;
                strLatitude = latitude + "";
                strLongitudeK = longitude + "";

                HomeFrag_OSK.latitudeK = latitude;
                HomeFrag_OSK.longitudeK = longitude;
                HomeFrag_OSK.strLatitude = latitude + "";
                HomeFrag_OSK.strLongitudeK = longitude + "";
                /*Address add = gps.getGeocoderListK(latitudeK, longitudeK);
                cityK = add.getLocality();
                countryK = add.getCountryName();*/
                //customAlertForLatLngK();

            }

            @Override
            public void onNotEnableKroid(String message) {
                showSettingsAlertK3();
            }
        });
    }//callGPSK

    public void showSettingsAlertK3() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("GPS setting");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to setting menu");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }//showSettingsAlertK3
    
    public void customAlertForLatLngK() {
        AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
        a.setMessage("select");
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.custom_latlng_k, null);
        final EditText edLat = (EditText) vi.findViewById(R.id.edLat);
        final EditText edLng = (EditText) vi.findViewById(R.id.edLng);
        edLat.setText(""+latitudeK);
        edLng.setText("" + longitudeK);
        a.setView(vi);
        a.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                latitudeK = Double.parseDouble(edLat.getText().toString());
                longitudeK = Double.parseDouble(edLng.getText().toString());
                strLatitude = latitudeK + "";
                strLongitudeK = longitudeK + "";
                /*Address add = gps.getGeocoderListK(latitudeK, longitudeK);
                cityK = add.getLocality();
                countryK = add.getCountryName();*/
            }
        });
        a.setNegativeButton("Cancel", null);
        a.show();
    }//customAlertForLatLngK

    public void kttpAddDealK(String shopName, final String deal, final int price, final String end,
                             final String comments) {
        String url = serviceMasterOSK.getPostDealURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        /*"deal": {
            "shopName": "Pizzahut",
            "deal": "Tasty Pizzas",
            "price": 999,
            "end": "04/30/2016",
            "comments": "Buy1Take 2"
        }*/
        try {
            JSONObject jDeal = new JSONObject();
            jDeal.put("shopName", shopName);
            jDeal.put("deal", deal);
            jDeal.put("price", price);
            jDeal.put("end", end);
            jDeal.put("comments", comments);

            JSONArray jLoc = new JSONArray();
            jLoc.put(longitudeK);
            jLoc.put(latitudeK);
            // 160528 change - long lat

            jDeal.put("location", jLoc);

            jBody.put("deal", jDeal);
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
                Log.i("osk", "fail");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + "- fail " );
                    //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                    if (statusCode == 401) {
                        MasterAct_OSK.tempMaster.logOutAndOutK();
                    }

                    if (errorResponse.has("message")) {
                        try {
                            Toast.makeText(getActivity(), errorResponse.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error occurred. Try again", Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed. Try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("osk", statusCode + " content " );
                    JSONObject jobject = response;
                    if (statusCode == 200) {
                        if (jobject.has("matchingDeals")) {
                            JSONArray jaDeal = jobject.getJSONArray("matchingDeals");
                            int ll = jaDeal.length();
                            if (ll != 0) {
                               /* {
                                        "location": {
                                    "type": "Point",
                                            "coordinates": [
                                    21.197202,
                                            63.9080663
                                    ]
                                },
                                    "deals": {
                                            "_id": "572c3a18bab2df0a0067a581",
                                },
                                }*/
                                ArrayList<objDealMasterK> itemHomeD = new ArrayList<objDealMasterK>();
                                for (int i = 0; i < jaDeal.length(); i++) {
                                    JSONObject job = jaDeal.getJSONObject(i);
                                    JSONObject jDl = job.getJSONObject("deals");
                                    JSONArray jLc = job.getJSONObject("location").getJSONArray("coordinates");

                                    objDealMasterK obj = new objDealMasterK();
                                    obj.set_idUser(job.getString("_id"));
                                    obj.setName(job.getString("name"));

                                    obj.set_idDeal(jDl.getString("_id"));
                                    obj.setExpiry(jDl.getString("expiry"));
                                    obj.setComments(jDl.getString("comments"));
                                    obj.setEnd(jDl.getString("end"));
                                    obj.setPrice(jDl.getString("price"));
                                    obj.setDeal(jDl.getString("deal"));
                                    obj.setShopName(jDl.getString("shopName"));
                                    obj.setStart(jDl.getString("start"));

                                    if (jDl.has("accepted")) {
                                        obj.setAccepted(jDl.getString("accepted"));
                                    }
                                    if (jDl.has("acceptedBy")) {
                                        obj.setAcceptedBy(jDl.getString("acceptedBy"));
                                    }

                                    double ln = (double) jLc.get(0);
                                    double lt = (double) jLc.get(1);
                                    // 160528 change - long lat

                                    /*obj.setLat(lt);
                                    obj.setLng(ln);*/
                                    obj.setLatLngK(lt, ln);

                                    itemHomeD.add(obj);
                                }
                                HomeFrag_OSK.itemHomeDeals = new ArrayList<objDealMasterK>();
                                HomeFrag_OSK.itemSelectedID = new ArrayList<String>();
                                HomeFrag_OSK.itemHomeDeals.clear();
                                HomeFrag_OSK.itemSelectedID.clear();
                                HomeFrag_OSK.itemHomeDeals = itemHomeD;
                                HomeFrag_OSK.isLoadFromAdd = true;

                            }
                        }

                        HistoryFrag_OSK.tabCurrent = 1;
                        HistoryFrag_OSK.isLoadMyDeal = false;
                        HistoryFrag_OSK.isLoadAccept = false;
                        HistoryFrag_OSK.isLoadReject = false;

                        final AlertAppFragKroid alert = new AlertAppFragKroid();
                        Bundle b = new Bundle();
                        b.putString("message", getResources().getString(R.string.popDealPostSuc));
                        alert.setArguments(b);
                        alert.setOnButtonClick(new AlertAppFragKroid.onAlertButtonKroid() {
                            @Override
                            public void onButtonOkClick() {
                                alert.dismiss();
                                getActivity().onBackPressed();
                            }
                        });
                        alert.setCancelable(false);
                        alert.show(getFragmentManager(), "Alert One");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }//kttpUpdateDealK

    private void showDatePickerKroid() {
        // kroid Date
        DatePickerFragKroid date = new DatePickerFragKroid();
        Bundle args = new Bundle();
        args.putInt("year", yearDialog);
        args.putInt("month", monthDialog);
        args.putInt("day", dayDialog);
        args.putInt("gap", 1);
        date.setArguments(args);
        /** DVL-Up-er.Kroid **/
        date.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                yearDialog = year;
                monthDialog = monthOfYear;
                dayDialog = dayOfMonth;
                strDateYMD = String.format("%d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);
                //strDateDMY = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear+1, year);
                strDateMDY = String.format("%02d/%02d/%d", monthOfYear + 1, dayOfMonth, year);
                //txtExpDate.setText(strDateMDY);
                showTimePickerKroid();
            }
        });
        date.show(getFragmentManager(), "Date Picker");
    }//showDatePickerKroid

    private void showTimePickerKroid() {
        // kroid Date
        TimePickerFragKroid time = new TimePickerFragKroid();
        Bundle args = new Bundle();
        args.putInt("hour", hourDialog);
        args.putInt("minute", minuteDialog);
        time.setArguments(args);
        /** DVL-Up-er.Kroid **/
        time.setCallBack(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourDialog = hourOfDay;
                minuteDialog = minute;
                strTime24 = String.format("%02d:%02d", hourOfDay, minute);
                txtExpDate.setText(strDateMDY +" "+ strTime24);
            }
        });
        time.show(getFragmentManager(), "Time Picker");
    }//showTimePickerKroid

    public void alertHourSelectionK() {
        /** DVL-Up-er.Kroid **/
        final String[] choice1 = { "1 hour", "2 hours", "3 hours", "4 hours", "5 hours", "6 hours"
                , "7 hours", "8 hours", "9 hours", "10 hours", "11 hours", "12 hours", "13 hours"
                , "14 hours", "15 hours", "16 hours", "17 hours", "18 hours", "19 hours", "20 hours"
                , "21 hours", "22 hours", "23 hours", "24 hours"} ;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(choice1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                hourCount = which+1;
                txtExpDate.setText(choice1[which]);
            }
        });
        builder.create().show();
    }//alertHourSelectionK

}
