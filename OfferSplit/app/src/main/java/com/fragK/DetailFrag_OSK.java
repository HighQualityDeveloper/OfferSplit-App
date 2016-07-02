package com.fragK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.actK.MasterAct_OSK;
import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.kroid.AlertAppFragKroid;
import com.kroid.AlertTwoAppFragKroid;
import com.kroid.menudrawer.MenuDrawer;
import com.kroid.objTimeKroid;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.objectK.objDealMasterK;
import com.objectK.objListMapCamK;
import com.offersplit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class DetailFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static DetailFrag_OSK tempD;

    ImageView imgHomeBtn;
    LinearLayout linearLayout4, linUserDetail;
    RelativeLayout relBottomMyDeal, relBottomAccept;
    TextView txtUpdateBtn, txtDeleteBtn, txtRejectBtn, txtRejectedBy, txtTitle, txtStore, txtPrice,
            txtExpIn, txtDes, txtDate, txtUserName, txtPhoneNum;

    int from, pos;
    ArrayList<objDealMasterK> list;
    objDealMasterK curObj;

    DateFormatSymbols dfs;
    static String[] months;
    SimpleDateFormat dateFormat;

    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detail_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempD = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumDetail;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        linearLayout4 = (LinearLayout) rootView.findViewById(R.id.linearLayout4);
        linUserDetail = (LinearLayout) rootView.findViewById(R.id.linUserDetail);

        txtUpdateBtn = (TextView) rootView.findViewById(R.id.txtUpdateBtn);
        txtDeleteBtn = (TextView) rootView.findViewById(R.id.txtDeleteBtn);
        txtRejectBtn = (TextView) rootView.findViewById(R.id.txtRejectBtn);
        txtRejectedBy = (TextView) rootView.findViewById(R.id.txtRejectedBy);
        txtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
        txtStore = (TextView) rootView.findViewById(R.id.txtStore);
        txtPrice = (TextView) rootView.findViewById(R.id.txtPrice);
        txtExpIn = (TextView) rootView.findViewById(R.id.txtExpIn);
        txtDes = (TextView) rootView.findViewById(R.id.txtDes);
        txtDate = (TextView) rootView.findViewById(R.id.txtDate);
        txtUserName = (TextView) rootView.findViewById(R.id.txtUserName);
        txtPhoneNum = (TextView) rootView.findViewById(R.id.txtPhoneNum);

        relBottomMyDeal = (RelativeLayout) rootView.findViewById(R.id.relBottomMyDeal);
        relBottomAccept = (RelativeLayout) rootView.findViewById(R.id.relBottomAccept);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups ------------------------------------------

        dfs = new DateFormatSymbols(Locale.US);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        months = dfs.getMonths();

        list = new ArrayList<objDealMasterK>();
        curObj = new objDealMasterK();
        Bundle b = getArguments();
        from = b.getInt("from");
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

        txtUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.fireUpdateDealK(list, pos);
            }
        });

        txtDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertTwoAppFragKroid alert = new AlertTwoAppFragKroid();
                Bundle b = new Bundle();
                b.putString("message", getResources().getString(R.string.popDealDeleteQ));
                alert.setArguments(b);
                alert.setOnButtonClick(new AlertTwoAppFragKroid.onAlertButtonKroid() {
                    @Override
                    public void onButtonYesClick() {
                        alert.dismiss();
                        if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                            kttpDeleteDealK();
                        }
                    }

                    @Override
                    public void onButtonNoClick() {
                        alert.dismiss();
                    }
                });
                alert.setCancelable(false);
                alert.show(getFragmentManager(), "Alert Two");
            }
        });

        txtRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertTwoAppFragKroid alert = new AlertTwoAppFragKroid();
                Bundle b = new Bundle();
                b.putString("message", getResources().getString(R.string.popDealRejectQ));
                alert.setArguments(b);
                alert.setOnButtonClick(new AlertTwoAppFragKroid.onAlertButtonKroid() {
                    @Override
                    public void onButtonYesClick() {
                        alert.dismiss();
                        if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                            kttpAcceptRejectDealK(false);
                        }
                    }

                    @Override
                    public void onButtonNoClick() {
                        alert.dismiss();
                    }
                });
                alert.setCancelable(false);
                alert.show(getFragmentManager(), "Alert Two");
            }
        });

        return rootView;
    }//oncreate =================================================

    public void setUpDetail() {
        txtTitle.setText(curObj.getDeal());
        txtStore.setText(curObj.getShopName());
        txtPrice.setText("Rs." + curObj.getPrice() + " /person");//Rs.500/person
        txtDes.setText(curObj.getComments());

        if (curObj.getEndCur().length()!=0) {
            txtExpIn.setText("Expiry " + convertDateKroid(curObj.getEndCur()));
        } else {
            txtExpIn.setText("");
        }

        if (curObj.getStartCur().length()!=0) {
            txtDate.setText(getDateStringK(curObj.getStartCur()));
        } else {
            txtDate.setText("");
        }

        boolean isDealExpire;
        String exDate = curObj.getEndCur();//.substring(0, 19)
        exDate = exDate.replace("T", " ");
        objTimeKroid oTime = new objTimeKroid();
        oTime = MasterAct_OSK.tempMaster.getTimeDifferenceKroid(exDate);
        if (Integer.parseInt(oTime.getDay()) > 0) {
            isDealExpire = false;
        } else {
            if (Integer.parseInt(oTime.getHour()) > 0) {
                isDealExpire = false;
            } else {
                isDealExpire = (Integer.parseInt(oTime.getMinute()) < 0);
            }
        }


        switch (from) {
            case 1:
                linearLayout4.setVisibility(View.VISIBLE);
                relBottomMyDeal.setVisibility(View.VISIBLE);
                relBottomAccept.setVisibility(View.GONE);
                linUserDetail.setVisibility(View.GONE);
                break;
            case 2:
                linearLayout4.setVisibility(View.VISIBLE);
                relBottomMyDeal.setVisibility(View.GONE);
                //relBottomAccept.setVisibility(View.VISIBLE);
                linUserDetail.setVisibility(View.VISIBLE);

                if (isDealExpire) {
                    relBottomAccept.setVisibility(View.GONE);
                } else {
                    relBottomAccept.setVisibility(View.VISIBLE);
                }

                txtPhoneNum.setText(curObj.getPhone());
                txtUserName.setText(curObj.getName());
                break;
            case 3:
                linearLayout4.setVisibility(View.GONE);
                linUserDetail.setVisibility(View.VISIBLE);
                txtRejectedBy.setVisibility(View.VISIBLE);

                txtPhoneNum.setText(curObj.getPhone());
                txtUserName.setText(curObj.getName());
                break;
        }//switch
    }//setUpDetail


    public void kttpDeleteDealK() {
        String url = serviceMasterOSK.getDeleteDealURL(curObj.get_idDeal());
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();

        kttp.addHeader("Content-Type", "application/json");
        kttp.addHeader("Authorization", spk.getString(appSetter_OSK.keyAccessToken, ""));

        kttp.delete(getActivity(), url, new JsonHttpResponseHandler() {
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
                Log.i("osk", statusCode + "- fail " );
                //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                if (statusCode == 401) {
                    MasterAct_OSK.tempMaster.logOutAndOutK();
                }
                Toast.makeText(getActivity(), "Fail, Try again", Toast.LENGTH_SHORT).show();
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
                    HistoryFrag_OSK.isLoadMyDeal = false;
                    getActivity().onBackPressed();
                }
            }
        });
    }//kttpUpdateDealK

    public void kttpAcceptRejectDealK(final boolean isAccept) {
        String url = "";
        if (isAccept) {
            url = serviceMasterOSK.getAcceptDealURL();
        } else {
            url = serviceMasterOSK.getRejectDealURL();
        }
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("deal", curObj.get_idDeal());//dlid
            jBody.put("posted", curObj.getPosted());
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("osk", "fail" );
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("osk", statusCode + "- fail " );
//                Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                if (statusCode == 401) {
                    //MasterAct_OSK.tempMaster.logOutAndOutK();
                }
                Toast.makeText(getActivity(), "Fail. Try again", Toast.LENGTH_SHORT).show();
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
                    HistoryFrag_OSK.tabCurrent = 3;
                    HistoryFrag_OSK.isLoadReject = false;
                    getActivity().onBackPressed();
                }
            }
        });
    }//kttpAcceptRejectDealK


    public String convertDateKroid(String inDate) {
        //inDate formate - 2016-05-07T07:03:04.178Z yyyy-MM-dd HH:mm:ss
        String outDate = "";
        String outTime = "";
        DateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newInDate = inDate.substring(0, 19);
        newInDate = newInDate.replace("T", " ");
        Log.i("osk", newInDate + "");
        try {
            Date d = dF.parse(newInDate);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            outDate = String.format("%02d %s",c.get(Calendar.DAY_OF_MONTH), getMonthForIntKroid3(c.get(Calendar.MONTH)),
                    c.get(Calendar.YEAR));
            String ampm = c.get(Calendar.AM_PM) == 0 ? "am" : "pm";
            outTime = String.format("%02d:%02d%s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                    ampm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outDate +" "+ outTime;
    }//convertDateKroid

    public String getDateStringK(String date) {
        //Log.i("osk", date+"");
        String strYear = date.substring(0,4);
        String strMonth = date.substring(5,7);
        String strDay = date.substring(8,10);
        String dt = getMonthForIntKroid3(Integer.parseInt(strMonth)-1) +" "+ strDay;
        return dt;
    }

    public static String getMonthForIntKroid3(int num) {
        String month = "";
        if (num >= 0 && num <= 11) {
            month = months[num].substring(0,3);
            //month = months[num];
        }
        return month;
    }//getMonthForIntK3

}
