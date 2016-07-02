package com.fragK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actK.MasterAct_OSK;
import com.adptrK.adptrMaster_OSK;
import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.kroid.menudrawer.MenuDrawer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.objectK.objDealMasterK;
import com.offersplit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HistoryFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static HistoryFrag_OSK tempH;

    ImageView imgHomeBtn, imgPlus;
    RelativeLayout relTab1, relTab2, relTab3, relTabStrip1, relTabStrip2, relTabStrip3;

    final int tabMyDeal=1, tabAccept=2, tabReject=3;
    public static int tabCurrent=0;
    boolean isSet1, isSet2, isSet3;

    adptrMaster_OSK adpMyDeal, adpAccept, adpReject;
    ListView listViewMyDeals, listViewAccept, listViewReject;
    public static ArrayList<objDealMasterK> itemMyDeal, itemAccept, itemReject;
    public static boolean isLoadMyDeal, isLoadAccept, isLoadReject;

    TextView txtNoDeal;


    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.history_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempH = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumHistory;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);
        imgPlus = (ImageView) rootView.findViewById(R.id.imgPlus);

        listViewMyDeals = (ListView) rootView.findViewById(R.id.listViewMyDeals);
        listViewAccept = (ListView) rootView.findViewById(R.id.listViewAccept);
        listViewReject = (ListView) rootView.findViewById(R.id.listViewReject);

        relTab1 = (RelativeLayout) rootView.findViewById(R.id.relTab1);
        relTab2 = (RelativeLayout) rootView.findViewById(R.id.relTab2);
        relTab3 = (RelativeLayout) rootView.findViewById(R.id.relTab3);
        relTabStrip1 = (RelativeLayout) rootView.findViewById(R.id.relTabStrip1);
        relTabStrip2 = (RelativeLayout) rootView.findViewById(R.id.relTabStrip2);
        relTabStrip3 = (RelativeLayout) rootView.findViewById(R.id.relTabStrip3);

        txtNoDeal = (TextView) rootView.findViewById(R.id.txtNoDeal);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        isSet1 = false;
        isSet2 = false;
        isSet3 = false;
        Log.i("tabCurrent", ""+tabCurrent);
        onTabClicK(tabCurrent);

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.hideSoftKeyboardKroid();
                MasterAct_OSK.mMenuDrawer.toggleMenu();
            }
        });

        relTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabClicK(tabMyDeal);
            }
        });
        relTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabClicK(tabAccept);
            }
        });
        relTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabClicK(tabReject);
            }
        });

        listViewMyDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MasterAct_OSK.tempMaster.fireDetailK(tabMyDeal, itemMyDeal, position);
            }
        });

        listViewAccept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MasterAct_OSK.tempMaster.fireDetailK(tabAccept, itemAccept, position);
            }
        });

        listViewReject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MasterAct_OSK.tempMaster.fireDetailK(tabReject, itemReject, position);
            }
        });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.fireAddDealK();
            }
        });

        return rootView;
    }//oncreate =================================================

    public void kttpHistoryK(final int tabNumFrom) {
        String url = serviceMasterOSK.getHistoryURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();

        kttp.addHeader("Content-Type", "application/json");
        kttp.addHeader("Authorization", spk.getString(appSetter_OSK.keyAccessToken, ""));

        kttp.get(getActivity(), url, new JsonHttpResponseHandler() {
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
                if (statusCode == 401) {
                    MasterAct_OSK.tempMaster.logOutAndOutK();
                }
                try {
                    Log.i("osk", statusCode + "- fail " );
                    //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                    if (errorResponse.has("message")) {
                        try {
                            Toast.makeText(getActivity(), errorResponse.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Please try again after some time",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Please try again after some time", Toast.LENGTH_SHORT).show();
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
                        JSONArray jaMyDeal = jobject.getJSONArray("mydeals");
                        JSONArray jaAccDeal = jobject.getJSONArray("accepted");
                        JSONArray jaRejDeal = jobject.getJSONArray("rejected");

                        itemMyDeal = new ArrayList<objDealMasterK>();
                        itemMyDeal.clear();
                        itemAccept = new ArrayList<objDealMasterK>();
                        itemAccept.clear();
                        itemReject = new ArrayList<objDealMasterK>();
                        itemReject.clear();

                        for (int i=0; i < jaMyDeal.length(); i++) {
                            JSONObject job = jaMyDeal.getJSONObject(i);
                            objDealMasterK obj = new objDealMasterK();
                            obj.set_idDeal(job.getString("_id"));
                            obj.setShopName(job.getString("shopName"));
                            obj.setDeal(job.getString("deal"));
                            obj.setPrice(job.getString("price"));
                            obj.setStart(job.getString("start"));
                            obj.setExpiry(job.getString("expiry"));
                            obj.setEnd(job.getString("end"));
                            obj.setComments(job.getString("comments"));
                            itemMyDeal.add(obj);
                        }

                        /*
                                */
                        for (int i=0; i < jaAccDeal.length(); i++) {
                            JSONObject job = jaAccDeal.getJSONObject(i);
                            objDealMasterK obj = new objDealMasterK();
                            obj.set_idDeal(job.getString("_id"));
                            obj.setShopName(job.getString("shopName"));
                            obj.setDeal(job.getString("deal"));
                            obj.setPrice(job.getString("price"));
                            obj.setName(job.getString("name"));
                            obj.setStart(job.getString("start"));
                            obj.setEnd(job.getString("end"));
                            obj.setExpiry(job.getString("expiry"));
                            obj.setAccepted(job.getString("accepted"));
                            obj.setPhone(job.getString("phone"));
                            obj.setPosted(job.getString("posted"));
                            obj.setComments(job.getString("comments"));
                            itemAccept.add(obj);
                        }

                        for (int i=0; i < jaRejDeal.length(); i++) {
                            JSONObject job = jaRejDeal.getJSONObject(i);
                            objDealMasterK obj = new objDealMasterK();
                            obj.set_idDeal(job.getString("_id"));
                            obj.setShopName(job.getString("shopName"));
                            obj.setDeal(job.getString("deal"));
                            obj.setPrice(job.getString("price"));
                            obj.setName(job.getString("name"));
                            obj.setStart(job.getString("start"));
                            obj.setEnd(job.getString("end"));
                            obj.setExpiry(job.getString("expiry"));
                            //obj.setAccepted(job.getString("accepted"));
                            obj.setPhone(job.getString("phone"));
                            obj.setPosted(job.getString("posted"));
                            obj.setComments(job.getString("comments"));
                            itemReject.add(obj);
                        }

                        if (itemMyDeal.size()>0) {
                            isLoadMyDeal = true;
                        }
                        if (itemAccept.size()>0) {
                            isLoadAccept = true;
                        }
                        if (itemReject.size()>0) {
                            isLoadReject = true;
                        }

                        switch (tabNumFrom) {
                            case tabMyDeal:
                                adpMyDeal = new adptrMaster_OSK(getActivity(), itemMyDeal, tabMyDeal);
                                isSet1 = true;
                                listViewMyDeals.setAdapter(adpMyDeal);
                                if (itemMyDeal.size()>0) {
                                    isLoadMyDeal = true;
                                    txtNoDeal.setVisibility(View.GONE);
                                } else {
                                    //Toast.makeText(getActivity(), "No deal found", Toast.LENGTH_SHORT).show();
                                    txtNoDeal.setVisibility(View.VISIBLE);
                                }
                                break;

                            case tabAccept:
                                adpAccept = new adptrMaster_OSK(getActivity(), itemAccept, tabAccept);
                                isSet2 = true;
                                listViewAccept.setAdapter(adpAccept);
                                if (itemAccept.size()>0) {
                                    isLoadAccept = true;
                                    txtNoDeal.setVisibility(View.GONE);
                                } else {
                                    //Toast.makeText(getActivity(), "No deal found", Toast.LENGTH_SHORT).show();
                                    txtNoDeal.setVisibility(View.VISIBLE);
                                }
                                break;

                            case tabReject:
                                adpReject = new adptrMaster_OSK(getActivity(), itemReject, tabReject);
                                isSet3 = true;
                                listViewReject.setAdapter(adpReject);
                                if (itemReject.size()>0) {
                                    isLoadReject = true;
                                    txtNoDeal.setVisibility(View.GONE);
                                } else {
                                    //Toast.makeText(getActivity(), "No deal found", Toast.LENGTH_SHORT).show();
                                    txtNoDeal.setVisibility(View.VISIBLE);
                                }
                                break;
                        }//switch

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }//kttpHistoryK

    public void onTabClicK(int tabNum) {
        switch (tabNum) {
            case 0:
                relTabStrip1.setVisibility(View.VISIBLE);
                relTabStrip2.setVisibility(View.GONE);
                relTabStrip3.setVisibility(View.GONE);

                listViewMyDeals.setVisibility(View.VISIBLE);
                listViewAccept.setVisibility(View.GONE);
                listViewReject.setVisibility(View.GONE);

                selectMyDealK();
                break;

            case tabMyDeal:
                relTabStrip1.setVisibility(View.VISIBLE);
                relTabStrip2.setVisibility(View.GONE);
                relTabStrip3.setVisibility(View.GONE);

                listViewMyDeals.setVisibility(View.VISIBLE);
                listViewAccept.setVisibility(View.GONE);
                listViewReject.setVisibility(View.GONE);

                selectMyDealK();
                break;
            case tabAccept:
                relTabStrip1.setVisibility(View.GONE);
                relTabStrip2.setVisibility(View.VISIBLE);
                relTabStrip3.setVisibility(View.GONE);

                listViewMyDeals.setVisibility(View.GONE);
                listViewAccept.setVisibility(View.VISIBLE);
                listViewReject.setVisibility(View.GONE);

                selectAcceptK();
                break;
            case tabReject:
                relTabStrip1.setVisibility(View.GONE);
                relTabStrip2.setVisibility(View.GONE);
                relTabStrip3.setVisibility(View.VISIBLE);

                listViewMyDeals.setVisibility(View.GONE);
                listViewAccept.setVisibility(View.GONE);
                listViewReject.setVisibility(View.VISIBLE);

                selectRejectK();
                break;
        }//switch
    }//onTabClicK

    private void selectMyDealK() {
        tabCurrent = tabMyDeal;
        if (itemMyDeal == null || !isLoadMyDeal) {
            if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                kttpHistoryK(tabMyDeal);
            }
        } else {
            if (!isSet1) {
                adpMyDeal = new adptrMaster_OSK(getActivity(), itemMyDeal, tabMyDeal);
                isSet1 = true;
                listViewMyDeals.setAdapter(adpMyDeal);
            }
            if (itemMyDeal.size()==0) {
                txtNoDeal.setVisibility(View.VISIBLE);
            } else {
                txtNoDeal.setVisibility(View.GONE);
            }
        }
    }//selectMyDealK

    private void selectAcceptK() {
        tabCurrent = tabAccept;
        if (itemAccept == null || !isLoadAccept) {
            if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                kttpHistoryK(tabAccept);
            }
        } else {
            if (!isSet2) {
                adpAccept = new adptrMaster_OSK(getActivity(), itemAccept, tabAccept);
                isSet2 = true;
                listViewAccept.setAdapter(adpAccept);
            }
            if (itemAccept.size()==0) {
                txtNoDeal.setVisibility(View.VISIBLE);
            } else {
                txtNoDeal.setVisibility(View.GONE);
            }
        }
    }//selectAcceptK

    private void selectRejectK() {
        tabCurrent = tabReject;
        if (itemReject == null || !isLoadReject) {
            if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                kttpHistoryK(tabReject);
            }
        } else {
            if (!isSet3) {
                adpReject = new adptrMaster_OSK(getActivity(), itemReject, tabReject);
                isSet3 = true;
                listViewReject.setAdapter(adpReject);
            }
            if (itemReject.size()==0) {
                txtNoDeal.setVisibility(View.VISIBLE);
            } else {
                txtNoDeal.setVisibility(View.GONE);
            }
        }
    }//selectRejectK

}
