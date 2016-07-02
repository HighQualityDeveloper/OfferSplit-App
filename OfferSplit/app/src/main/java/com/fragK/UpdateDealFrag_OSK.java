package com.fragK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actK.MasterAct_OSK;
import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.kroid.AlertAppFragKroid;
import com.kroid.DatePickerFragKroid;
import com.kroid.menudrawer.MenuDrawer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.objectK.objDealMasterK;
import com.offersplit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class UpdateDealFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static UpdateDealFrag_OSK tempU;

    ImageView imgHomeBtn;
    RelativeLayout relUpdateBtn, relCancelBtn, relExpDate;
    EditText edStoreName, edDescription, edPrice, edComment;
    TextView txtExpDate;

    public static String strDateYMD="", strDateDMY="", strDateMDY=""; // kroid Date
    Calendar calender = Calendar.getInstance();
    int yearDialog = calender.get(Calendar.YEAR);
    int monthDialog = calender.get(Calendar.MONTH);
    int dayDialog = calender.get(Calendar.DAY_OF_MONTH);

    int hourCount = 0;

    int pos;
    ArrayList<objDealMasterK> list;
    objDealMasterK curObj;


    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.update_deal_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempU = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumUpdateDeal;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        txtExpDate = (TextView) rootView.findViewById(R.id.txtExpDate);

        edStoreName = (EditText) rootView.findViewById(R.id.edStoreName);
        edDescription = (EditText) rootView.findViewById(R.id.edDescription);
        edPrice = (EditText) rootView.findViewById(R.id.edPrice);
        edComment = (EditText) rootView.findViewById(R.id.edComment);

        relUpdateBtn = (RelativeLayout) rootView.findViewById(R.id.relUpdateBtn);
        relCancelBtn = (RelativeLayout) rootView.findViewById(R.id.relCancelBtn);
        relExpDate = (RelativeLayout) rootView.findViewById(R.id.relExpDate);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        list = new ArrayList<objDealMasterK>();
        curObj = new objDealMasterK();
        Bundle b = getArguments();
        pos = b.getInt("pos");
        list = b.getParcelableArrayList("list");
        curObj = list.get(pos);

        setUpDetail();

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        relUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateK()) {
                    if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                        kttpUpdateDealK(edStoreName.getText().toString(),
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

    public void setUpDetail() {
        edStoreName.setText(curObj.getShopName());
        edDescription.setText(curObj.getDeal());
        edPrice.setText(curObj.getPrice());
        edComment.setText(curObj.getComments());

        String date = curObj.getEndCur();
        yearDialog = Integer.parseInt(date.substring(0,4));
        monthDialog = Integer.parseInt(date.substring(5, 7))-1;
        dayDialog = Integer.parseInt(date.substring(8, 10));
        strDateMDY = String.format("%02d/%02d/%d", monthDialog+1, dayDialog, yearDialog);
        //txtExpDate.setText(strDateMDY);
    }//setUpDetail

    public boolean validateK() {
        if (edStoreName.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a store name",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edDescription.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a description",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edPrice.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a price",
                    Toast.LENGTH_SHORT).show();
            return false;
        }/* else if (hourCount < 1) {
            Toast.makeText(getActivity(), "Select an expiry hours", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strDateMDY.length()==0) {
            Toast.makeText(getActivity(), "Select an expiry date",
                    Toast.LENGTH_SHORT).show();
            return false;
        }*/ else if (edComment.getText().toString().length()==0) {
            Toast.makeText(getActivity(), "Enter a comment",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }//validateK

    public void kttpUpdateDealK(String shopName, final String deal, final int price, final String end,
                                final String comments) {
        String url = serviceMasterOSK.getUpdateDealURL(curObj.get_idDeal());
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        /*{
            "shopName": "PizzaHut",
            "deal": "Buy2 take 3",
            "price": 600,
            "end": "2",
            "comments": "Tasty Pizzas"
        }*/
        try {
            jBody.put("shopName", shopName);
            jBody.put("deal", deal);
            jBody.put("price", price);
            if (!end.equals("0")) {
                jBody.put("end", end);
            }
            jBody.put("comments", comments);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type", "application/json");
        kttp.addHeader("Authorization", spk.getString(appSetter_OSK.keyAccessToken, ""));

        kttp.put(getActivity(), url, entity, "application/json", new JsonHttpResponseHandler() {
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + "- fail ");
                    if (statusCode == 401) {
                        MasterAct_OSK.tempMaster.logOutAndOutK();
                    }
                    Toast.makeText(getActivity(), "Failed. Try again later", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
                    Log.i("osk", statusCode + "");
                    JSONObject jobject = response;
                    if (statusCode == 200) {

                        if (jobject.has("updatedDeal")) {
                            JSONObject joDeal = jobject.getJSONObject("updatedDeal");
                            curObj.set_idDeal(joDeal.getString("_id"));
                            curObj.setShopName(joDeal.getString("shopName"));
                            curObj.setDeal(joDeal.getString("deal"));
                            curObj.setPrice(joDeal.getString("price"));
                            curObj.setStart(joDeal.getString("start"));
                            curObj.setExpiry(joDeal.getString("expiry"));
                            curObj.setComments(joDeal.getString("comments"));
                            curObj.setEnd(joDeal.getString("end"));

                            final AlertAppFragKroid alert = new AlertAppFragKroid();
                            Bundle b = new Bundle();
                            b.putString("message", getResources().getString(R.string.popDealUpdateSuc));
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
        args.putInt("gap", 0);
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
                txtExpDate.setText(strDateMDY);
            }
        });
        date.show(getFragmentManager(), "Date Picker");
    }//showDatePickerKroid

    public void alertHourSelectionK() {
        /** DVL-Up-er.Kroid **/
        final String[] choice1 = { "1 hour", "2 hours", "3 hours", "4 hours", "5 hours", "6 hours"
                , "7 hours", "8 hours", "9 hours", "10 hours", "11 hours", "12 hours", "13 hours"
                , "14 hours", "15 hours", "16 hours", "17 hours", "18 hours", "19 hours", "20 hours"
                , "21 hours", "22 hours", "23 hours", "24 hours"} ;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(choice1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                hourCount = which + 1;
                txtExpDate.setText(choice1[which]);
            }
        });
        builder.create().show();
    }//alertHourSelectionK

}
