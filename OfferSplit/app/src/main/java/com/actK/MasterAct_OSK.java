package com.actK;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.fragK.AddDealFrag_OSK;
import com.fragK.ChangePassFrag_OSK;
import com.fragK.ChatFrag_OSK;
import com.fragK.DetailFrag_OSK;
import com.fragK.HistoryFrag_OSK;
import com.fragK.HomeFrag_OSK;
import com.fragK.NotificationFrag_OSK;
import com.fragK.ProfileFrag_OSK;
import com.fragK.SettingsFrag_OSK;
import com.fragK.UpdateDealFrag_OSK;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kroid.AlertAppFragKroid;
import com.kroid.AlertTwoAppFragKroid;
import com.kroid.menudrawer.MenuDrawer;
import com.kroid.menudrawer.Position;
import com.kroid.objTimeKroid;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.objectK.objChatK;
import com.objectK.objDealMasterK;
import com.objectK.objNotiK;
import com.offersplit.MainActivity;
import com.offersplit.R;
//import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.MinimalField;

public class MasterAct_OSK extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static MasterAct_OSK tempMaster;

    public static int screenWidthK , screenHeightK , refWidthK = 720 ,
            refHeightK = 1280 , _56dp , _6dp , _w, _h;	// KROID KIT
    Point size = new Point();

    public static MenuDrawer mMenuDrawer;

    RelativeLayout relHomeBtn, relProfileBtn, relHistoryBtn, relSettingsBtn, relChangePassBtn,
            relBTM, relBtmView;
    TextView txtLogoutBtn;

    public static HomeFrag_OSK homeFrag;
    public static ProfileFrag_OSK profileFrag;
    public static HistoryFrag_OSK historyFrag;
    public static SettingsFrag_OSK settingFrag;
    public static ChangePassFrag_OSK changePFrag;
    public static DetailFrag_OSK detailFrag;
    public static NotificationFrag_OSK notiFrag;
    public static AddDealFrag_OSK addDFrag;
    public static UpdateDealFrag_OSK updateDFrag;
    public static ChatFrag_OSK chatFrag;

    public static int curFragNum;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;

    public static ArrayList<objNotiK> itemNotiData;

    public static boolean isAppRunning;

    public static final String appName = "Offer Split";
    public static final String playPath = "https://play.google.com/store/apps/details?id=";
    public static final String fbPackage = "com.facebook.katana";
    public static final String twitterPackage = "com.twitter.android";
    public static final String linkedInPackage = "com.linkedin.android";
    public static final String whatsAppPackage = "com.whatsapp";
    public static final String sharingText = "Hey! Check out this amazing app I found! Download it here:"+
            "\nAndroid: " + playPath + "" +
            "\niPhone: ";
    public static final String sharingSocialText = "Check out this fantastic way to travel on private jets! FlyOnTheGo.com"+
            "\nAndroid: " + playPath + "" +
            "\niPhone: ";
    public static final String fbPageLink = "https://www.facebook.com/Offer-Split-1108852385841788/?fref=ts";

    /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
    private CallbackManager callbackManager;
    /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/

    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/

    /* --- XMPP chat --- */
    /* --- XMPP --- XMPP --- XMPP --- XMPP --- */
    public static final String HOST = appSetter_OSK.openFireHOST;//acra.dowhistle.com
    public static final int PORT = 5222;
    public static final String SERVICE = appSetter_OSK.openFireSERVICE;//dowhistle.com
    public static String USERNAME = "";//admin
    public static final String PASSWORD = appSetter_OSK.openFirePassword;//Password
    public static XMPPConnection connection;
    private Handler mHandler = new Handler();
    /* --- XMPP --- XMPP --- XMPP --- XMPP --- */

    public static boolean isChatOpen, isXMPPconnected;

    static int notiCountAdder=0;

    Button buttonFB, buttonWA;

    @Override
    protected void onStart() {
        super.onStart();
        isAppRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        onDestroyXMPPCall();
        isAppRunning = false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY,
                Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        getWindowManager().getDefaultDisplay().getSize(size);
        screenWidthK = size.x;
        screenHeightK = size.y;
        mMenuDrawer.setMenuSize((int) (screenWidthK * 0.8));
        mMenuDrawer.setContentView(R.layout.master_act_xk);
        mMenuDrawer.setMenuView(R.layout.menu_drawer_xk);
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());/*--- fb sdk ----*/
        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.i("loginManager", "call -in savedIns");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.v("LoginActivity", "cancel -in savedIns");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.v("LoginActivity", "error -in savedIns");
                    }
                });
        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/

        /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(MasterAct_OSK.this)
                .enableAutoManage(MasterAct_OSK.this, this)/* OnConnectionFailedListener */
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/

        spk = getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempMaster = this;

        relHomeBtn = (RelativeLayout) findViewById(R.id.relHomeBtn);
        relProfileBtn = (RelativeLayout) findViewById(R.id.relProfileBtn);
        relHistoryBtn = (RelativeLayout) findViewById(R.id.relHistoryBtn);
        relSettingsBtn = (RelativeLayout) findViewById(R.id.relSettingsBtn);
        relChangePassBtn = (RelativeLayout) findViewById(R.id.relChangePassBtn);
        relBTM = (RelativeLayout) findViewById(R.id.relBTM);
        relBtmView = (RelativeLayout) findViewById(R.id.relBtmView);

        txtLogoutBtn = (TextView) findViewById(R.id.txtLogoutBtn);

        buttonFB = (Button) findViewById(R.id.buttonFB);
        buttonWA = (Button) findViewById(R.id.buttonWA);

        // setups ------------------------------------------

        if (spk.getString(appSetter_OSK.keyLoginAs,"").length()!=0) {
            relChangePassBtn.setVisibility(View.GONE);
        } else {
            relChangePassBtn.setVisibility(View.VISIBLE);
        }

        int hhh = getNavBarHeightKroid(MasterAct_OSK.this);
        relBTM.getLayoutParams().height = hhh;
        relBtmView.getLayoutParams().height = hhh;

        _56dp = (int) getResources().getDimension(R.dimen._56dp);

        GCMkroid();

        curFragNum = 0;
        Bundle b = getIntent().getExtras();
        if (b==null) {
            fireHomeK();
        } else {
            if (b.containsKey("fromnotichat")) {
                if (b.getBoolean("fromnotichat")) {
                    //fireChatK(b.getString("uname"));
                    fireHomeK(b.getString("uname"));
                } else {
                    fireHomeK();
                }
            } else {
                fireHomeK();
            }
        }

        if (isNetworkAvailableK3(true)) {
            connectToXMPPK();
        }
        // onclick events ----------------------------------

        relHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDrawer.toggleMenu();
                fireHomeK();
            }
        });

        relProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDrawer.toggleMenu();
                fireProfileK();
            }
        });

        relHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDrawer.toggleMenu();
                fireHistoryK();
            }
        });

        relSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDrawer.toggleMenu();
                fireSettingK();
            }
        });

        relChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDrawer.toggleMenu();
                fireChangePassK();
            }
        });

        txtLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDrawer.toggleMenu();
                if (isNetworkAvailableK3(true)) {
                    kttpLogoutK();
                }
            }
        });

        /*txtLogoutBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("osk", spk.getString(appSetter_OSK.keyAccessToken, ""));
                return false;
            }
        });*/

        /*edtr = spk.edit();
        edtr.putString("testnoti", "");
        edtr.commit();

        addNotiItemK("{ data:{ message: 'Free Cappacino is accepted by Mukesh', status: 'accepted', title: 'Offer Split' } , timeToLive: 3000 }");
        addNotiItemK("{ data:{ message: 'Maria is accepted by Salaman Khan', status: 'accepted', title: 'Offer Split' } , timeToLive: 3000 }");
        addNotiItemK("{ data:{ message: 'Philip is rejected by Amir Khan', status: 'rejected', title: 'Offer Split' } , timeToLive: 3000 }");
        addNotiItemK("{ data:{ message: 'Ratan Tata is accepted by Jitendra', status: 'accepted', title: 'Offer Split' } , timeToLive: 3000 }");
        addNotiItemK("{ data:{ message: 'Neeta Ambani is rejected by Ranveer', status: 'rejected', title: 'Offer Split' } , timeToLive: 3000 }");
        addNotiItemK("{ data:{ message: 'John Cena is rejected by Hasmi', status: 'rejected', title: 'Offer Split' } , timeToLive: 3000 }");

        Log.i("osk", getNotiItemK());*/

        buttonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToFBKroid("Buy 1 get 2 at DreamShade");
            }
        });
        buttonWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWhatsAppKroid("Offer Split:\nBuy 1 get 2 at DreamShade");
            }
        });

    }//onCreate ========================================

    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {
            Log.i("osk", "G+ fail" + connectionResult.toString());
        } catch (NullPointerException e) {
            Log.i("osk", "G+ fail nullpointer");
        }
    }
    private void signOutToGplusKroid() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        try {
                            Log.i("osk", "G+ ");
                        } catch (NullPointerException e) {
                            Log.i("osk", "G+ Sout nullpointer");
                        }
                    }
                });
    }
    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/

    @Override
    public void onBackPressed() {
        String fragCurr = getSupportFragmentManager().findFragmentById(R.id.container_main).getTag();
        hideSoftKeyboardKroid();
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }
        else {
            if (fragCurr.contains("1")) {
                exitPopK3();
            } else if (fragCurr.contains("2")) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }//onBackPressed ----

    // -----------------------------

    public void fireHomeK() {
        if (curFragNum != appSetter_OSK.fragNumHome) {
            homeFrag = new HomeFrag_OSK();
            HomeFrag_OSK.isLoadHome = false;
            HomeFrag_OSK.isExitFromHome = false;
            HomeFrag_OSK.openChatUname = "";
            replaceFragK(homeFrag, appSetter_OSK.fragHome);
        }
    }//fireHomeK
    public void fireHomeK(String uname) {
        homeFrag = new HomeFrag_OSK();
        HomeFrag_OSK.isLoadHome = false;
        HomeFrag_OSK.isExitFromHome = false;
        HomeFrag_OSK.openChatUname = uname;
        replaceFragK(homeFrag, appSetter_OSK.fragHome);
    }//fireHomeK

    public void fireProfileK() {
        if (curFragNum != appSetter_OSK.fragNumProfile) {
            profileFrag = new ProfileFrag_OSK();
            replaceFragK(profileFrag, appSetter_OSK.fragProfile);
        }
    }

    public void fireHistoryK() {
        if (curFragNum != appSetter_OSK.fragNumHistory) {
            historyFrag = new HistoryFrag_OSK();
            HistoryFrag_OSK.tabCurrent = 1;
            HistoryFrag_OSK.isLoadMyDeal = false;
            HistoryFrag_OSK.isLoadAccept = false;
            HistoryFrag_OSK.isLoadReject = false;
            replaceFragK(historyFrag, appSetter_OSK.fragHistory);
        }
    }

    public void fireSettingK() {
        if (curFragNum != appSetter_OSK.fragNumSettings) {
            settingFrag = new SettingsFrag_OSK();
            replaceFragK(settingFrag, appSetter_OSK.fragSettings);
        }
    }

    public void fireChangePassK() {
        if (curFragNum != appSetter_OSK.fragNumChangePass) {
            changePFrag = new ChangePassFrag_OSK();
            replaceFragK(changePFrag, appSetter_OSK.fragChangePass);
        }
    }

    public void fireDetailK(int from, ArrayList<objDealMasterK> list, int pos) {
        if (curFragNum != appSetter_OSK.fragNumDetail) {
            detailFrag = new DetailFrag_OSK();
            Bundle b = new Bundle();
            b.putInt("from", from);
            b.putParcelableArrayList("list", list);
            b.putInt("pos", pos);
            detailFrag.setArguments(b);
            replaceToBackStackFragK(detailFrag, appSetter_OSK.fragDetail);
        }
    }

    public void fireNotificationK() {
        if (curFragNum != appSetter_OSK.fragNumNoti) {
            notiFrag = new NotificationFrag_OSK();
            replaceToBackStackFragK(notiFrag, appSetter_OSK.fragNoti);
        }
    }

    public void fireAddDealK() {
        if (curFragNum != appSetter_OSK.fragNumAddDeal) {
            addDFrag = new AddDealFrag_OSK();
            replaceToBackStackFragK(addDFrag, appSetter_OSK.fragAddDeal);
        }
    }

    public void fireUpdateDealK(ArrayList<objDealMasterK> list, int pos) {
        if (curFragNum != appSetter_OSK.fragNumUpdateDeal) {
            updateDFrag = new UpdateDealFrag_OSK();
            Bundle b = new Bundle();
            b.putParcelableArrayList("list", list);
            b.putInt("pos", pos);
            updateDFrag.setArguments(b);
            replaceToBackStackFragK(updateDFrag, appSetter_OSK.fragUpdateDeal);
        }
    }

    public void fireChatK(objDealMasterK obj) {
        if (curFragNum != appSetter_OSK.fragNumChat) {
            chatFrag = new ChatFrag_OSK();
            /*ArrayList<objDealMasterK> list = new ArrayList<objDealMasterK>();
            list.add(obj);
            Bundle b = new Bundle();
            b.putParcelableArrayList("list", list);
            b.putInt("pos", 0);*/
            Bundle b = new Bundle();
            if (obj.get_idUser().equals(spk.getString(appSetter_OSK.keyID_, "noid"))) {
                b.putString("toID", obj.getAccepted());
                b.putString("name", obj.getAcceptedBy());
            } else {
                b.putString("toID", obj.get_idUser());
                b.putString("name", obj.getName());
            }
            chatFrag.setArguments(b);
            replaceToBackStackFragK(chatFrag, appSetter_OSK.fragChat);
        }
    }

    public void fireChatK(String uname) {
        chatFrag = new ChatFrag_OSK();
        Bundle b = new Bundle();
        b.putString("toID", uname);
        b.putString("name", "");
        chatFrag.setArguments(b);
        replaceToBackStackFragK(chatFrag, appSetter_OSK.fragChat);
    }

    // -------------

    public void replaceFragK(Fragment f, String name) {
        hideSoftKeyboardKroid();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, f, name).commit();
        /* DVL-Up-er.Kroid */
    }//replaceFragK

    public void replaceToBackStackFragK(Fragment f, String name) {
        hideSoftKeyboardKroid();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, f, name)
                .addToBackStack(name).commit();
        /* DVL-Up-er.Kroid */
    }//replaceFragK
    // -----------------------------

    public void GCMkroid() {
        Log.i("GCM", "call");
        // Check if Internet present
        if (!isNetworkAvailableK3(false)) {
            // Internet Connection is not present
            /*AlertDialog.Builder ab = new AlertDialog.Builder(MasterAct_OSK.this);
            ab.setTitle(getResources().getString(R.string.str_Internet_Conn_Err));
            ab.setMessage(getResources().getString(R.string.sInternetReq));
            ab.setPositiveButton("Ok", null);
            ab.show();*/
            return;
        }

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        //Log.i("regid 123456", regId);
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(MasterAct_OSK.this, appSetter_OSK.gcmSenderID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {

                Log.i("GCMk","Already registered with GCM ");
                // hence the use of AsyncTask instead of a raw thread.
                // call login service ********
                kttpUpdateProfileK(regId);
            } else {
                /*** DVL-Up-er.Kroid ***/
                // call login service ********
                kttpUpdateProfileK(regId);
            }
        }
    }//GCMkroid

    public void kttpUpdateProfileK(String regId) {
        String url = serviceMasterOSK.getUpdateProfileURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("regId", regId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type", "application/json");
        kttp.addHeader("Authorization", spk.getString(appSetter_OSK.keyAccessToken, ""));

        kttp.post(MasterAct_OSK.this, url, entity, "application/json", new JsonHttpResponseHandler() {
            //ProgressDialog pd = new ProgressDialog(MasterAct_OSK.this);

            @Override
            public void onStart() {
                super.onStart();
                /*pd.setMessage(getResources().getString(R.string.sLoading));
                pd.setCancelable(false);
                pd.show();*/
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("osk", "fail" );
                if (statusCode == 401) {
                    MasterAct_OSK.tempMaster.logOutAndOutK();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("osk", statusCode + "- fail " );
                //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                if (statusCode == 401) {
                    MasterAct_OSK.tempMaster.logOutAndOutK();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("osk", statusCode + " content " );
                if (statusCode == 200) {
                    Log.i("osk", "GCM added");
                }
            }
        });
    }//kttpUpdateProfileK

    public void kttpLogoutK() {
        String url = serviceMasterOSK.getLogoutURL();
        //Log.i("osk", "url "+url);
        AsyncHttpClient kttp = new AsyncHttpClient();

        //StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");
        JSONObject jo = new JSONObject();
        try {
            jo.put("Authorization", spk.getString(appSetter_OSK.keyAccessToken, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("osk", jo.toString());

        kttp.addHeader("Content-Type", "application/json");
        kttp.addHeader("Authorization", spk.getString(appSetter_OSK.keyAccessToken, ""));

        kttp.delete(MasterAct_OSK.this, url, new AsyncHttpResponseHandler() {
            ProgressDialog pd = new ProgressDialog(MasterAct_OSK.this);

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
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    Log.i("osk", statusCode + " fail " );
                } catch (NullPointerException np) {
                    Log.i("osk", statusCode + " fail nullp");
                }
                Toast.makeText(MasterAct_OSK.this, "Time out. Please try again later",
                        Toast.LENGTH_LONG).show();
                if (statusCode == 401) {
                    logOutAndOutK();
                }
            }

            /*@Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("osk", "fail" + responseString + ", throwable: " +
                        throwable == null ? "" : throwable.getMessage().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("osk", statusCode + " fail " + throwable == null ? "null" : throwable.toString());
                //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                if (errorResponse.has("message")) {
                    try {
                        Toast.makeText(MasterAct_OSK.this, errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MasterAct_OSK.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }*/

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("osk", statusCode + " string");
                if (statusCode == 200) {
                    LoginManager.getInstance().logOut();
                    Log.i("osk", "logout");
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    try {
                                        Log.i("osk", "G+ " + status.toString());
                                    } catch (NullPointerException e) {
                                        Log.i("osk", "G+ Sout nullpointer");
                                    }
                                }
                            });
                    if (appSetter_OSK.keyLoginAs.equals(appSetter_OSK.socialTypeFB)) {

                    } else if (appSetter_OSK.keyLoginAs.equals(appSetter_OSK.socialTypeGOOGLE)) {

                    } else {

                    }
                    logOutAndOutK();
                }
            }

            /*@Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("osk", statusCode + " string");
                if (statusCode == 200) {
                    edtr = spk.edit();
                    edtr.putString(appSetter_OSK.keyID_, "");
                    edtr.putString(appSetter_OSK.keyUName, "");
                    edtr.putString(appSetter_OSK.keyUPhone, "");
                    edtr.putString(appSetter_OSK.keyUEmail, "");
                    edtr.putString(appSetter_OSK.keyAccessToken, "");
                    edtr.putString(appSetter_OSK.keyUPassword, "");

                    edtr.putFloat(appSetter_OSK.keyULat, 0f);
                    edtr.putFloat(appSetter_OSK.keyULng, 0f);

                    edtr.putInt(appSetter_OSK.keyRadius, 0);
                    edtr.putInt(appSetter_OSK.keyExpiry, 0);
                    edtr.putBoolean(appSetter_OSK.keyIsWhistle, false);
                    edtr.putBoolean(appSetter_OSK.keyIsNotification, false);

                    edtr.putBoolean(appSetter_OSK.keyIsLogin, false);

                    edtr.commit();

                    startActivity(new Intent(MasterAct_OSK.this, LogInAct_OSK.class));
                    if (curFragNum == appSetter_OSK.fragNumHome) {
                        HomeFrag_OSK.isExitFromHome = true;
                        finish();
                    } else {
                        finish();
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("osk", statusCode +" jo");
                if (statusCode == 200) {
                    edtr = spk.edit();
                    edtr.putString(appSetter_OSK.keyID_, "");
                    edtr.putString(appSetter_OSK.keyUName, "");
                    edtr.putString(appSetter_OSK.keyUPhone, "");
                    edtr.putString(appSetter_OSK.keyUEmail, "");
                    edtr.putString(appSetter_OSK.keyAccessToken, "");
                    edtr.putString(appSetter_OSK.keyUPassword, "");

                    edtr.putFloat(appSetter_OSK.keyULat, 0f);
                    edtr.putFloat(appSetter_OSK.keyULng, 0f);

                    edtr.putInt(appSetter_OSK.keyRadius, 0);
                    edtr.putInt(appSetter_OSK.keyExpiry, 0);
                    edtr.putBoolean(appSetter_OSK.keyIsWhistle, false);
                    edtr.putBoolean(appSetter_OSK.keyIsNotification, false);

                    edtr.putBoolean(appSetter_OSK.keyIsLogin, false);

                    edtr.commit();

                    startActivity(new Intent(MasterAct_OSK.this, LogInAct_OSK.class));
                    if (curFragNum == appSetter_OSK.fragNumHome) {
                        HomeFrag_OSK.isExitFromHome = true;
                        finish();
                    } else {
                        finish();
                    }
                }
            }*/
        });
    }//kttpUpdateDealK

    public void logOutAndOutK() {
        edtr = spk.edit();
        edtr.putString(appSetter_OSK.keyID_, "");
        edtr.putString(appSetter_OSK.keyUName, "");
        edtr.putString(appSetter_OSK.keyUPhone, "");
        edtr.putString(appSetter_OSK.keyUEmail, "");
        edtr.putString(appSetter_OSK.keyAccessToken, "");
        edtr.putString(appSetter_OSK.keyUPassword, "");

        edtr.putFloat(appSetter_OSK.keyULat, 0f);
        edtr.putFloat(appSetter_OSK.keyULng, 0f);

        edtr.putInt(appSetter_OSK.keyRadius, 0);
        /*edtr.putInt(appSetter_OSK.keyExpiry, 0);
        edtr.putBoolean(appSetter_OSK.keyIsWhistle, false);*/
        edtr.putBoolean(appSetter_OSK.keyIsNotification, false);

        edtr.putBoolean(appSetter_OSK.keyIsLogin, false);
        edtr.putString(appSetter_OSK.keyLoginAs, "");
        edtr.putString(appSetter_OSK.keyNotiItems, "");

        edtr.commit();

        startActivity(new Intent(MasterAct_OSK.this, LogInAct_OSK.class));
        if (curFragNum == appSetter_OSK.fragNumHome) {
            HomeFrag_OSK.isExitFromHome = true;
            finish();
        } else {
            finish();
        }
    }//logOutAndOutK

    public void exitPopK3() {
        final AlertTwoAppFragKroid alert = new AlertTwoAppFragKroid();
        Bundle b = new Bundle();
        b.putString("message", getResources().getString(R.string.popExitQ));
        alert.setArguments(b);
        alert.setOnButtonClick(new AlertTwoAppFragKroid.onAlertButtonKroid() {
            @Override
            public void onButtonYesClick() {
                if (curFragNum == appSetter_OSK.fragNumHome) {
                    HomeFrag_OSK.isExitFromHome = true;
                    alert.dismiss();
                    finish();
                } else {
                    alert.dismiss();
                    finish();
                }
            }

            @Override
            public void onButtonNoClick() {
                alert.dismiss();
            }
        });
        alert.setCancelable(false);
        alert.show(getSupportFragmentManager(), "Alert Two");
    }//exitPopK3

    public static String charsetUTF8 = "UTF-8";
    public static String plusEncode = "%2B";

    public String getCurrentTimeZoneK3() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US);
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z", Locale.US);
        String localTime = date.format(currentLocalTime);
        //Log.i("getCurrentTimeZoneK3", "GMT" + localTime.substring(0, 3) + ":" + localTime.substring(3, 5));
        //return (localTime.substring(0,3) +":"+ localTime.substring(3,5)).replace("+", "%2B");
        return (localTime.substring(0,3) +":"+ localTime.substring(3,5));
    }//getCurrentTimeZoneK3

    public objTimeKroid getTimeDifferenceKroid(String expCreateDate) {
        String currStr = simpleDateFormat.format(new Date());
        String creStr = expCreateDate;
        //Log.i("currStr", currStr);
        //Log.i("creStr", creStr);
        objTimeKroid result = new objTimeKroid();
        try {
            Date dateCurrent = simpleDateFormat.parse(currStr);
            Date date2 = simpleDateFormat.parse(creStr);
            result = getDifferenceKroid(dateCurrent, date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }//getTimeDifferenceKroid

    public objTimeKroid getDifferenceKroid(Date startDate, Date endDate){
        String result = "";
        objTimeKroid obj = new objTimeKroid();
        long different = endDate.getTime() - startDate.getTime();
        long totalMnt = 0;
        int totalWeek = 0;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        obj.setDay(elapsedDays+"");
        totalMnt = totalMnt + (elapsedDays*24*60);
        totalWeek = (int) (elapsedDays/7);
        if (elapsedDays != 0) {
            result = result + elapsedDays+"D";
        }

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        obj.setHour(elapsedHours+"");
        totalMnt = totalMnt + (elapsedHours*60);
        if (elapsedHours != 0) {
            result = result + (elapsedDays==0?"":" ") + elapsedHours+"H";
        }

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        obj.setMinute(elapsedMinutes + "");
        totalMnt = totalMnt + (elapsedMinutes);
        if (elapsedMinutes != 0) {
            result = result + (elapsedDays==0 && elapsedHours==0?"":" ") + elapsedMinutes+"M";
        }

        long elapsedSeconds = different / secondsInMilli;
        obj.setSecond(elapsedSeconds + "");

        obj.setDiffInString(result);
        obj.setTotal_minute(totalMnt + "");
        obj.setTotal_week(totalWeek);

        /*System.out.printf("%d days, %d hours, %d minutes, %d seconds, , %d total%n", elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds, totalMnt);*/

        return obj;
    }//getDifferenceKroid

    //int screenWidth
    public int calculateZoomLevelKroid() {
        double equatorLength = 10000; // in meters
        double widthInPixels = screenWidthK;
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * widthInPixels) > 2000) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        //Log.i("osk", "zoom level = "+zoomLevel);
        return zoomLevel;
    }//calculateZoomLevelKroid

    /*import com.facebook.share.model.ShareLinkContent;
    import com.facebook.share.widget.ShareDialog;
    import com.twitter.sdk.android.tweetcomposer.TweetComposer;*/
    public void shareToFBKroid(String sharingText) {
        String sharingTextWithLink = sharingText +". Get more offers like this, click to download app";
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle(appName)
                .setContentDescription(sharingTextWithLink)
                .setContentUrl(Uri.parse(playPath + "com.offersplit"))
                .build();
        //.setContentUrl(Uri.parse("http://www..com"))
        //.setImageUrl(Uri.parse("http://www./my_logo.png"))
        ShareDialog.show(MasterAct_OSK.this, content);
    }//shareToFBKroid

    public void shareToTwitterKroid(String sharingText) {
        /*TweetComposer.Builder builder = new TweetComposer.Builder(MasterAct_OSK.this)
                .text(sharingText);//.image(myImageUri);
        builder.show();*/
        String sharingTextWithLink = sharingText +". Get more offers like this, download at "+
                playPath + "com.offersplit";
        if (isInstalledOrNotKroid(twitterPackage)) {
            Intent whatsAppIntent = new Intent(android.content.Intent.ACTION_SEND);
            whatsAppIntent.setType("text/plain");
            whatsAppIntent.setPackage(twitterPackage);
            whatsAppIntent.putExtra(Intent.EXTRA_TEXT, sharingTextWithLink);
            startActivity(whatsAppIntent);
        } else {
            openPlayStoreKroid(twitterPackage);
        }
    }//shareToTwitterKroid

    public void shareToWhatsAppKroid(String sharingText) {
        String sharingTextWithLink = sharingText +".\nGet more offers like this, download at "+
                playPath + "com.offersplit";
        if (isInstalledOrNotKroid(whatsAppPackage)) {
            Intent whatsAppIntent = new Intent(android.content.Intent.ACTION_SEND);
            whatsAppIntent.setType("text/plain");
            whatsAppIntent.setPackage(whatsAppPackage);
            whatsAppIntent.putExtra(Intent.EXTRA_TEXT, sharingTextWithLink);
            startActivity(whatsAppIntent);
        } else {
            openPlayStoreKroid(whatsAppPackage);
        }
    }//shareToWhatsAppKroid

    public boolean isInstalledOrNotKroid(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }//isInstalledOrNotKroid

    public void openPlayStoreKroid(String packageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
                    + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playPath + packageName)));
        }
    }//openPlayStoreKroid

    public int getNavBarHeightKroid(Context c) {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        //Log.i("getNavBarHeightKroid", "hasMenuKey"+ hasMenuKey + "\nhasBackKey" + hasBackKey);
        if(!hasMenuKey && !hasBackKey) {
            Resources resources = c.getResources();
            int orientation = getResources().getConfiguration().orientation;
            int resourceId;
            resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
            if (resourceId > 0) {
                return getResources().getDimensionPixelSize(resourceId);
            }
        }//getNavBarHeightKroid
        Log.i("getNavBarHeightKroid", "" + result);
        return result;
    }

    /**
     * Hides the soft keyboard DVL-Up-er.Kroid
     */
    public void hideSoftKeyboardKroid() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboardKroid(View view) {
        /* DVL-Up-er.Kroid */
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public final static boolean isValidEmailKroid(CharSequence target) {
        /* DVL-Up-er.Kroid */
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }//isValidEmailKroid

    public boolean isNetworkAvailableK3(boolean isShowPop) {
        /* DVL-Up-er.Kroid */
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return true;
        } else {
            if (isShowPop) {
                Toast.makeText(MasterAct_OSK.this,
                        getResources().getString(R.string.sInternetReq),
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }//isNetworkAvailableK3

    public void test(String date) {
        objTimeKroid oTime = new objTimeKroid();
        oTime = MasterAct_OSK.tempMaster.getTimeDifferenceKroid(date);
        if (Integer.parseInt(oTime.getDay()) > 0) {
            if (oTime.getTotal_week() > 3) {
                //Log.i("osktest", "Expire in " + oTime.getTotal_week() + " week");;
            } else {
                //Log.i("osktest", "Expire in " + oTime.getDay() + " days");
            }
        } else {
            if (Integer.parseInt(oTime.getHour()) > 0) {
                //Log.i("osktest", "Expire in " + oTime.getHour() + " hours");
            } else {
                /*Log.i("osktest", Integer.parseInt(oTime.getMinute()) > 0 ?
                        "Expire in " + oTime.getMinute() + " min" : "Expired");*/
            }
        }
    }

    public int setNotiDataK() {
        int count = 0;
        itemNotiData = new ArrayList<objNotiK>();
        itemNotiData.clear();
        if (spk.getString(appSetter_OSK.keyNotiItems,"").length()!=0) {
            try {
                JSONObject job = new JSONObject("{data:[" + spk.getString(appSetter_OSK.keyNotiItems,"") + "]}");
                JSONArray jAry = job.getJSONArray("data");
                for (int i = 0; i < jAry.length(); i++) {
                    JSONObject j = jAry.getJSONObject(i);
                    objNotiK obj = new objNotiK();
                    obj.setMessage(j.getString("message"));
                    obj.setStatus(j.getString("status"));
                    obj.setTitle(j.getString("title"));
                    obj.setFrom(j.getString("from"));
                    itemNotiData.add(obj);
                }
                count = itemNotiData.size();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return count;
    }//setNotiDataK

    private void addNotiItemK(String response) {
        try {
            JSONObject job = new JSONObject(response);
            if (job.has("data")) {
                if (spk.getString("testnoti","").length()==0) {
                    edtr = spk.edit();
                    edtr.putString("testnoti", job.getJSONObject("data").toString());
                    edtr.commit();
                } else {
                    edtr = spk.edit();
                    edtr.putString("testnoti", spk.getString("testnoti","")+","+job.getJSONObject("data").toString());
                    edtr.commit();
                    Log.i("osk", ",");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getNotiItemK() {
        String arr = "";
        if (spk.getString("testnoti","").length()!=0) {
            arr = "{data:[" + spk.getString("testnoti","") + "]}";
        }
        return arr;
    }


    /* ------- XMPP chat ------- */
    /* ---- DVL-Up-er.Kroid ---- */
    /* ------- XMPP chat ------- */

    public void connectToXMPPK() {
        USERNAME = spk.getString(appSetter_OSK.keyID_, "");
        /*final ProgressDialog dialog = ProgressDialog.show(MasterAct_OSK.this, "Connecting...",
                "Please wait...", false);*/

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                if (connection != null) {
                    connection.disconnect();
                }
                connection = null;
                isXMPPconnected = false;
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        HOST, PORT, SERVICE);
                XMPPConnection connection = new XMPPConnection(connConfig);

                try {
                    connection.connect();
                    Log.i("osk", "Connected to " + connection.getHost());
                } catch (XMPPException ex) {
                    Log.e("osk", "Failed to connect to " + connection.getHost());
                    Log.e("osk", ex.toString());
                    setConnectionK(null);
                }
                try {
                    // SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                    connection.login(USERNAME, PASSWORD);
                    Log.i("osk", "Logged in as " + connection.getUser());
                    isXMPPconnected = true;

                    // Set the status to available
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    setConnectionK(connection);

                    /*Roster roster = connection.getRoster();
                    Collection<RosterEntry> entries = roster.getEntries();
                    for (RosterEntry entry : entries) {
                        Log.d("osk", "--------------------------------------");
                        Log.d("osk", "RosterEntry " + entry);
                        Log.d("osk", "User: " + entry.getUser());
                        Log.d("osk", "Name: " + entry.getName());
                        Log.d("osk", "Status: " + entry.getStatus());
                        Log.d("osk", "Type: " + entry.getType());
                        Presence entryPresence = roster.getPresence(entry.getUser());

                        Log.d("osk", "Presence Status: " + entryPresence.getStatus());
                        Log.d("osk", "Presence Type: " + entryPresence.getType());
                        Presence.Type type = entryPresence.getType();
                        if (type == Presence.Type.available)
                            Log.d("osk", "Presence AVIALABLE");
                        Log.d("osk", "Presence : " + entryPresence);

                    }*/
                } catch (XMPPException ex) {
                    Log.e("osk", "Failed to log in as " + USERNAME);
                    Log.e("osk", ex.toString());
                    setConnectionK(null);
                }

                //dialog.dismiss();
            }
        });
        t.start();
        //dialog.show();
    }//connectToXMPPK

    public void setConnectionK(XMPPConnection connection) {
        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    final Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = StringUtils.parseBareAddress(message.getFrom());
                        Log.i("osk", "Text Recieved " + message.toXML());
                        // Add the incoming message to the list view
                        /*mHandler.post(new Runnable() {
                            public void run() {
                                item.add(new objChatK("in", message.getBody()));
                                adp.notifyDataSetChanged();
                                listViewChat.setSelection(adp.getCount());
                            }gr
                        });*/
                        String uname = fromName.replace("@acra.dowhistle.com","");
                        if (isChatOpen) {
                            if (uname.equals(ChatFrag_OSK.toIDchecker)) {
                                ChatFrag_OSK.tempP.inMessageToChatKroid(message.getBody());
                            } else {
                                generateNotificationCurFragKroid(MasterAct_OSK.this, "",
                                        message.getBody(), uname);
                            }
                        } else {
                            generateNotificationCurFragKroid(MasterAct_OSK.this, "",
                                    message.getBody(), uname);
                        }
                        Log.i("osk", "in add "+setItemToChatK(uname,
                                spk.getString(appSetter_OSK.keyID_, ""), message.getBody()));
                    } else {
                        Log.i("osk", "Recieved message body null" );
                    }
                }
            }, filter);
        } else {
            Log.i("osk", "connection null" );
        }
    }//setConnectionK

    // call from onDestroy
    public void onDestroyXMPPCall() {
        try {
            if (connection != null) {
                connection.disconnect();
                isXMPPconnected = false;
            }
        } catch (Exception e) {
            Log.i("osk", "Exception onDestroy" );
        }
    }//onDestroyXMPPCall

    /* --- XMPP chat --- */

    public boolean setItemToChatK(String from, String to, String text) {
        String jsonObject = "{\"from\":\"" +from+ "\",\"to\":\"" +to+ "\",\"text\":\"" +text+ "\"}";
        String chatData = spk.getString(appSetter_OSK.keyChatItem, "");
        if (chatData.length()==0) {
            chatData = jsonObject;
        } else {
            chatData = chatData +","+ jsonObject;
        }
        edtr = spk.edit();
        edtr.putString(appSetter_OSK.keyChatItem, chatData);
        edtr.commit();
        return true;
    }//setItemToChatK

    private static void generateNotificationCurFragKroid(Context context, String title,
                                                         String message, String uname) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        NotificationManager mNotificationManager;
        long when = System.currentTimeMillis();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setColor(Color.parseColor("#00ADEF"));
        mBuilder.setSmallIcon(R.drawable.niticon);
        mBuilder.setLargeIcon(largeIcon);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent ;
        if (isAppRunning) {
            resultIntent = new Intent(context, MasterAct_OSK.class);
        } else {
            resultIntent = new Intent(context, MainActivity.class);
        }

        mBuilder.setContentTitle(title.length() == 0 ? appName : title);
        mBuilder.setContentText(message);



        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle(title.length() == 0 ? appName : title);
        //bigText.setSummaryText("Por: GORIO Engenharia");
        mBuilder.setStyle(bigText);


        resultIntent.putExtra("fromnotichat", true);
        resultIntent.putExtra("uname", uname);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        notiCountAdder++;
        mNotificationManager.notify(notiCountAdder, notification);
    }//generateNotificationCurFragKroid

}
