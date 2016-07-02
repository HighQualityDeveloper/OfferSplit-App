package com.fragK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adptrK.PopupAdapter_OSK;
import com.appKroid.serviceMasterOSK;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.actK.MasterAct_OSK;
import com.appKroid.appSetter_OSK;
import com.kroid.AlertTwoAppFragKroid;
import com.kroid.GPSTrackerKroid;
import com.kroid.menudrawer.MenuDrawer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.objectK.objDealMasterK;
import com.objectK.objListMapCamK;
import com.offersplit.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HomeFrag_OSK extends Fragment implements OnMapReadyCallback {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static HomeFrag_OSK tempHome;
    static final float COORDINATE_OFFSET = 0.00002f;
    HashMap<String, String> markerLocation;
    ImageView imgHomeBtn, imgNotiBtn, imgPlus, imgRefresh, imgAcceptBtn, imgCancelBtn, imgShareBtn,
            imgWAshare, imgFBshare, imgTWIshare;
    RelativeLayout relSelection, relNoMapTouch, relBellCounter, relShareKit, relCancelShare;
    TextView txtSelName, txtSelDeal, txtSelPrice, txtSelExpiry, txtSelComm, txtBellCounter;

    private GoogleMap mMap;
    ArrayList<objListMapCamK> itemCamList;
    public static ArrayList<String> itemSelectedID;

    public static ArrayList<objDealMasterK> itemHomeDeals;
    public static boolean isLoadHome;

    BitmapDescriptor icon, iconAccept;
    objDealMasterK curOpenDeal;
    public static boolean isExitFromHome;
    public static SupportMapFragment mapFragment;

    public static double latitudeK, longitudeK;
    public static String strLatitude = "", strLongitudeK = "";
    public static String cityK, countryK;
    GPSTrackerKroid gps;
    LatLng curLatLng;
    int selectedMarkerPos;

    public static boolean isLoadFromAdd;

    boolean isShowCurr;

    public static String openChatUname="";

    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempHome = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumHome;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);
        imgNotiBtn = (ImageView) rootView.findViewById(R.id.imgNotiBtn);
        imgPlus = (ImageView) rootView.findViewById(R.id.imgPlus);
        imgRefresh = (ImageView) rootView.findViewById(R.id.imgRefresh);

        imgAcceptBtn = (ImageView) rootView.findViewById(R.id.imgAcceptBtn);
        imgCancelBtn = (ImageView) rootView.findViewById(R.id.imgCancelBtn);
        imgShareBtn = (ImageView) rootView.findViewById(R.id.imgShareBtn);
        imgWAshare = (ImageView) rootView.findViewById(R.id.imgWAshare);
        imgFBshare = (ImageView) rootView.findViewById(R.id.imgFBshare);
        imgTWIshare = (ImageView) rootView.findViewById(R.id.imgTWIshare);

        relSelection = (RelativeLayout) rootView.findViewById(R.id.relSelection);
        relNoMapTouch = (RelativeLayout) rootView.findViewById(R.id.relNoMapTouch);
        relBellCounter = (RelativeLayout) rootView.findViewById(R.id.relBellCounter);
        relShareKit = (RelativeLayout) rootView.findViewById(R.id.relShareKit);
        relCancelShare = (RelativeLayout) rootView.findViewById(R.id.relCancelShare);

        txtSelName = (TextView) rootView.findViewById(R.id.txtSelName);
        txtSelDeal = (TextView) rootView.findViewById(R.id.txtSelDeal);
        txtSelPrice = (TextView) rootView.findViewById(R.id.txtSelPrice);
        txtSelExpiry = (TextView) rootView.findViewById(R.id.txtSelExpiry);
        txtSelComm = (TextView) rootView.findViewById(R.id.txtSelComm);
        txtBellCounter = (TextView) rootView.findViewById(R.id.txtBellCounter);

        /*mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        /*final AlertTwoAppFragKroid alert = new AlertTwoAppFragKroid();
        Bundle b = new Bundle();
        b.putString("message", getResources().getString(R.string.popDealDeleteQ));
        alert.setArguments(b);
        alert.setOnButtonClick(new AlertTwoAppFragKroid.onAlertButtonKroid() {
            @Override
            public void onButtonYesClick() {

            }

            @Override
            public void onButtonNoClick() {

            }
        });
        alert.setCancelable(false);
        alert.show(getFragmentManager(), "Alert Two");*/

        itemCamList = new ArrayList<objListMapCamK>();

        icon = BitmapDescriptorFactory.fromResource(R.drawable.mappin);
        //iconAccept = BitmapDescriptorFactory.fromResource(R.drawable.mappinaccept);
        iconAccept = BitmapDescriptorFactory.fromResource(R.drawable.mappinacceptchat);

        gps = new GPSTrackerKroid(getActivity());
        //strLatitude="";
        //strLongitudeK="";

        relSelection.getLayoutParams().height = (MasterAct_OSK.screenHeightK / 2) - MasterAct_OSK._56dp;

        if (openChatUname.length()!=0) {
            MasterAct_OSK.tempMaster.fireChatK(openChatUname);
            openChatUname="";
        } else {

            if (isLoadFromAdd) {
                isShowCurr = false;
                loadMapK();
                isLoadFromAdd = false;
            } else {
                setHomeK();
            }

            setUpNotiCounterK();
        }

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MasterAct_OSK.tempMaster.hideSoftKeyboardKroid();
                MasterAct_OSK.mMenuDrawer.toggleMenu();
            }
        });

        imgNotiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.fireNotificationK();
            }
        });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.fireAddDealK();
            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoadHome = false;
                setHomeK();
            }
        });

        imgAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertTwoAppFragKroid alert = new AlertTwoAppFragKroid();
                Bundle b = new Bundle();
                b.putString("message", getResources().getString(R.string.popDealAcceptQ));
                alert.setArguments(b);
                alert.setOnButtonClick(new AlertTwoAppFragKroid.onAlertButtonKroid() {
                    @Override
                    public void onButtonYesClick() {
                        alert.dismiss();
                        if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                            kttpAcceptRejectDealK(true, curOpenDeal.get_idDeal(), curOpenDeal.get_idUser());
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

        imgCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relSelection.setVisibility(View.GONE);
                relNoMapTouch.setVisibility(View.GONE);
                imgRefresh.setVisibility(View.VISIBLE);
                curOpenDeal.setMarker(null);
                if (itemCamList.size() > 2) {
                    objListMapCamK o = itemCamList.get(itemCamList.size() - 3);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(o.getLatLng(), o.getZoom()));
                }
                /*if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                    kttpAcceptRejectDealK(false, curOpenDeal.get_idDeal(), curOpenDeal.get_idUser());
                }*/
            }
        });

        imgShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShowShareKitK(true);
            }
        });

        relCancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShowShareKitK(false);
            }
        });

        imgWAshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curOpenDeal != null) {
                    MasterAct_OSK.tempMaster.shareToWhatsAppKroid("Offer Split: " +
                            curOpenDeal.getDeal() + " at " + curOpenDeal.getShopName());
                }
            }
        });
        imgFBshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curOpenDeal != null) {
                    MasterAct_OSK.tempMaster.shareToFBKroid(curOpenDeal.getDeal() + " at " + curOpenDeal.getShopName());
                }
            }
        });
        imgTWIshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAct_OSK.tempMaster.shareToTwitterKroid("Offer Split: " +
                        curOpenDeal.getDeal() + " at " + curOpenDeal.getShopName());
            }
        });

        relNoMapTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nothing
            }
        });

        return rootView;
    }//oncreate =================================================

    public void hideShowShareKitK(boolean action) {
        if (action) {
            relSelection.setVisibility(View.GONE);
            relShareKit.setVisibility(View.VISIBLE);
        } else {
            relSelection.setVisibility(View.VISIBLE);
            relShareKit.setVisibility(View.GONE);
        }
    }//hideShowShareKitK

    public void setUpNotiCounterK() {
        relBellCounter.setVisibility(View.GONE);
        int c = 0;
        c = MasterAct_OSK.tempMaster.setNotiDataK();
        /*if (MasterAct_OSK.itemNotiData==null) {
            c = MasterAct_OSK.tempMaster.setNotiDataK();
        } else {
            c = MasterAct_OSK.itemNotiData.size();
        }*/
        if (c > 0) {
            txtBellCounter.setText(c + "");
            relBellCounter.setVisibility(View.VISIBLE);
        }
    }//setUpNotiCounterK

    private void setHomeK() {
        isShowCurr = true;
        loadMapK();
        if (strLatitude.length() == 0 && strLongitudeK.length() == 0) {
            callGPSK();
        } else if (itemHomeDeals == null || !isLoadHome) {
            if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                kttpSearchK(latitudeK, longitudeK);
            }
        } else {
            isShowCurr = false;
            loadMapK();
        }
    }//setHomeK

    private void callGPSK() {
        gps.getLocationKroid(new GPSTrackerKroid.onLocationCallKroid() {
            @Override
            public void onGetLocationKroid(double latitude, double longitude) {
                latitudeK = latitude;
                longitudeK = longitude;
                strLatitude = latitude + "";
                strLongitudeK = longitude + "";
                curLatLng = new LatLng(latitude, longitude);
                /*Address add = gps.getGeocoderListK(latitudeK, longitudeK);
                cityK = add.getLocality();
                countryK = add.getCountryName();*/
                if (MasterAct_OSK.tempMaster.isNetworkAvailableK3(true)) {
                    kttpSearchK(latitude, longitude);
                }
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
        alertDialog.setMessage("Enable GPS for search deals.");
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

    public void kttpSearchK(double latt, double lngg) {
        String url = serviceMasterOSK.getSearchDealURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        /*"location": [
            76.935032,
            11.040691
        ]*/
        try {
            JSONArray jLoc = new JSONArray();
            jLoc.put(lngg);
            jLoc.put(latt);
            // 160528 change - long lat

            jBody.put("location", jLoc);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + " fail ");
                    //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                    if (errorResponse.has("message")) {
                        try {
                            Toast.makeText(getActivity(), errorResponse.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException e) {
                    Log.i("osk", statusCode + " fail ");
                }
                if (statusCode == 401) {
                    MasterAct_OSK.tempMaster.logOutAndOutK();
                }
                Toast.makeText(getActivity(), "Failed to load. Try again", Toast.LENGTH_SHORT).show();
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
                        if (jobject.has("matchingDeals")) {
                            markerLocation=new HashMap<String, String>();
                            JSONArray jaDeal = jobject.getJSONArray("matchingDeals");
                            int ll = jaDeal.length();
                            itemSelectedID = new ArrayList<String>();
                            itemSelectedID.clear();
                            itemHomeDeals = new ArrayList<objDealMasterK>();
                            itemHomeDeals.clear();
                            if (ll == 0) {
                                Toast.makeText(getActivity(), "No deal found", Toast.LENGTH_SHORT).show();
                            } else {
                                /*"matchingDeals": [
                                {
                                    "_id": "571f2da8ca97eb10163e6e17",
                                    "name": "Prasanth",
                                    "deals": {
                                        "expiry": "2016-04-27T09:13:16.460Z",
                                        "comments": "free for all",
                                        "end": "2016-04-29T18:30:00.000Z",
                                        "price": 350,
                                        "deal": "Free compliment",
                                        "shopName": "Barbeque Nation",
                                        "start": "2016-04-26T09:13:16.460Z"
                                    },
                                    "location": {
                                        "type": "Point",
                                        "coordinates": [
                                            76.935032,
                                            11.040691
                                        ]
                                    }
                                },*/
                                for (int i = 0; i < ll; i++) {
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

                                    itemHomeDeals.add(obj);
                                }
                                isLoadHome = true;
                                if (ll == 0) {
                                    isShowCurr = true;
                                    loadMapK();
                                } else {
                                    isShowCurr = false;
                                    loadMapK();
                                }

                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }//kttpSearchK

    public void kttpAcceptRejectDealK(final boolean isAccept, String dealID, String postID) {
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
            jBody.put("deal", dealID);//dlid
            jBody.put("posted", postID);
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
                    Log.i("osk", statusCode + "\n fail ");
                    //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                    if (statusCode == 401) {
                        MasterAct_OSK.tempMaster.logOutAndOutK();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
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
                Log.i("osk", statusCode + " content " + response.toString());
                if (statusCode == 200) {
                    relSelection.setVisibility(View.GONE);
                    relNoMapTouch.setVisibility(View.GONE);
                    imgRefresh.setVisibility(View.VISIBLE);
                    if (isAccept) {
                        /*if (curOpenDeal.getMarker() != null) {
                            curOpenDeal.getMarker().setIcon(iconAccept);
                            itemSelectedID.add(curOpenDeal.getMarker().getId());
                        }*/
                        itemHomeDeals.get(selectedMarkerPos).setAccepted(spk.getString(appSetter_OSK.keyID_, "noid"));
                        isLoadHome = false;
                        setHomeK();
                        /*itemHomeDeals.get(selectedMarkerPos).getMarker().setIcon(iconAccept);
                        itemSelectedID.add(itemHomeDeals.get(selectedMarkerPos).getMarker().getId());*/
                        //Log.i("osk", "id acc" + itemHomeDeals.get(selectedMarkerPos).getMarker().getId());
                    } else {
                        curOpenDeal.setMarker(null);
                        if (itemCamList.size() > 2) {
                            objListMapCamK o = itemCamList.get(itemCamList.size() - 3);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(o.getLatLng(), o.getZoom()));
                        }
                    }
                }
            }
        });
    }//kttpAcceptRejectDealK

    private void loadMapK() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }//loadMapK

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mMap.setMyLocationEnabled(true);*/

        if (isShowCurr) {
            if (strLatitude.length()==0 || strLongitudeK.length()==0) {
                Marker mm = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(spk.getFloat(appSetter_OSK.keyULat, 00.0f),
                                spk.getFloat(appSetter_OSK.keyULng, 00.0f))));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(spk.getFloat(appSetter_OSK.keyULat, 00.0f),
                        spk.getFloat(appSetter_OSK.keyULng, 00.0f)), 15));
                Log.i("osk", "from saved");
            } else {
                Marker mm = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitudeK, longitudeK)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeK, longitudeK), 15));
                Log.i("osk", "from cur L");
            }
            return;
        }

        LatLngBounds.Builder ltBuilder = new LatLngBounds.Builder();
        itemSelectedID.clear();
        for (int i = 0; i < itemHomeDeals.size(); i++) {



            if (itemHomeDeals.get(i).getAccepted().equals(spk.getString(appSetter_OSK.keyID_,"noid")) ||
                    itemHomeDeals.get(i).get_idUser().equals(spk.getString(appSetter_OSK.keyID_,"noid"))) {
                Marker mm = mMap.addMarker(new MarkerOptions()
                        // .position(itemHomeDeals.get(i).getLatLng())
                        .position(coordinateForMarker(itemHomeDeals.get(i).getLat(), itemHomeDeals.get(i).getLng()))
                        .title(itemHomeDeals.get(i).getName())
                        .icon(iconAccept)
                        .snippet(itemHomeDeals.get(i).getShopName() + "-" +
                                itemHomeDeals.get(i).getDeal()));
                itemHomeDeals.get(i).setMarkerID(mm.getId());
                ltBuilder.include(mm.getPosition());
                itemSelectedID.add(mm.getId());
                Log.i("osk", mm.getId());
            } else {
                Marker mm = mMap.addMarker(new MarkerOptions()
                        // .position(itemHomeDeals.get(i).getLatLng())
                        .position(coordinateForMarker(itemHomeDeals.get(i).getLat(), itemHomeDeals.get(i).getLng()))
                        .title(itemHomeDeals.get(i).getName())
                        .icon(icon)
                        .snippet(itemHomeDeals.get(i).getShopName() +"-"+
                                itemHomeDeals.get(i).getDeal()));
                itemHomeDeals.get(i).setMarkerID(mm.getId());
                ltBuilder.include(mm.getPosition());
                Log.i("osk", mm.getId());
            }
        }

        LatLngBounds bounds = ltBuilder.build();

        mMap.setInfoWindowAdapter(new PopupAdapter_OSK(getActivity(),
                getActivity().getLayoutInflater(), itemHomeDeals));
        //mMap.setOnInfoWindowClickListener(this);

        /*if (curLatLng==null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemHomeDeals.get(0).getLatLng(),
                    MasterAct_OSK.tempMaster.calculateZoomLevelKroid()));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatLng,
                    MasterAct_OSK.tempMaster.calculateZoomLevelKroid()));
        }*/

        int padding = 90; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Log.i("marker", "id " + marker.getId());
                if (itemSelectedID.contains(marker.getId())) {
                    for (int i = 0; i < itemHomeDeals.size(); i++) {
                        if (itemHomeDeals.get(i).getMarkerID().equals(marker.getId())) {
                            MasterAct_OSK.tempMaster.fireChatK(itemHomeDeals.get(i));
                        }
                    }
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15f));
                    for (int i = 0; i < itemHomeDeals.size(); i++) {
                        if (itemHomeDeals.get(i).getMarkerID().equals(marker.getId())) {
                            openDealDeailK(itemHomeDeals.get(i));
                            selectedMarkerPos = i;
                        }
                    }
                }
                return false;
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //Log.i("osk", "cam change " + cameraPosition.target.toString() + " -- " + cameraPosition.zoom);
                itemCamList.add(new objListMapCamK(cameraPosition.target, cameraPosition.zoom));
            }
        });

    }//onMapReady ---

    public void destroy() {

    }

    // Ketan
    private LatLng coordinateForMarker(double latitude, double longitude) {


        String[] location = new String[2];
        Random rand = new Random();

        float finalX = rand.nextFloat() * (0.00010f - 0.00002f) + 0.00002f;


            if (mapAlreadyHasMarkerForLocation((latitude )
                    + "," + (longitude ))) {

                // If i = 0 then below if condition is same as upper one. Hence, no need to execute below if condition.
                location[0] = latitude + ( finalX) + "";
                location[1] = longitude + ( finalX) + "";

            }
        else {
                location[0] = latitude +  "";
                location[1] = longitude  + "";
            }
markerLocation.put("location",location[0]+","+location[1]);
        return new LatLng(Double.parseDouble(location[0]),Double.parseDouble(location[1]));
    }

    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(String location) {
        return (markerLocation.containsValue(location));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!isExitFromHome) {
            FragmentManager fm = getChildFragmentManager();
            Fragment fragment = (fm.findFragmentById(R.id.map));
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    public void openDealDeailK(objDealMasterK objD) {
        curOpenDeal = objD;
        //Log.i("osk", curOpenDeal.toString());
        relSelection.setVisibility(View.VISIBLE);
        txtSelName.setText(objD.getName());
        txtSelDeal.setText(objD.getDeal());
        txtSelPrice.setText(objD.getPrice() + " /person");//250 /person
        txtSelExpiry.setText(convertDateKroid(objD.getExpiryCur()));
        txtSelComm.setText(objD.getComments());

        relNoMapTouch.setVisibility(View.VISIBLE);
        imgRefresh.setVisibility(View.GONE);
    }//openDealDeailK

    public String convertDateKroid(String inDate) {
        //inDate formate - 2016-05-07T07:03:04.178Z yyyy-MM-dd HH:mm:ss
        String outDate = "";
        String outTime = "";
        DateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newInDate = inDate.substring(0, 19);
        newInDate = newInDate.replace("T", " ");
        //Log.i("osk", newInDate+"");
        try {
            Date d = dF.parse(newInDate);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            outDate = String.format("%02d-%02d-%d",c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1,
                    c.get(Calendar.YEAR));
            /*String ampm = c.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
            outTime = String.format("%02d:%02d %s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                    ampm);*/
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outDate;
    }//convertDateKroid

}
