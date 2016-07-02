package com.offersplit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actK.LogInAct_OSK;
import com.actK.MasterAct_OSK;
import com.appKroid.appSetter_OSK;
import com.crashlytics.android.Crashlytics;
import com.google.android.gcm.GCMRegistrar;
import com.kroid.objTimeKroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    SharedPreferences spk;
    SharedPreferences.Editor edtr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spk = getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);

        Log.i("osk", "-" + spk.getBoolean(appSetter_OSK.keyIsLogin, false));
        /*Log.i("osk", "-"+spk.getString(appSetter_OSK.keyID_,"XX"));
        Log.i("osk", "-"+spk.getString(appSetter_OSK.keyUName,"XX"));
        Log.i("osk", "-"+spk.getString(appSetter_OSK.keyUPhone,"XX"));
        Log.i("osk", "-"+spk.getString(appSetter_OSK.keyUEmail,"XX"));
        Log.i("osk", "-"+spk.getString(appSetter_OSK.keyAccessToken,"XX"));
        Log.i("osk", "-" + spk.getFloat(appSetter_OSK.keyULat, 0f));
        Log.i("osk", "-" + spk.getFloat(appSetter_OSK.keyULng, 0f));
        Log.i("osk", "-" + spk.getInt(appSetter_OSK.keyRadius, 0));
        Log.i("osk", "-" + spk.getInt(appSetter_OSK.keyExpiry, 0));
        Log.i("osk", "-" + spk.getBoolean(appSetter_OSK.keyIsNotification, false));
        Log.i("osk", "-" + spk.getBoolean(appSetter_OSK.keyIsWhistle, false));*/

        setUpGCM();

        if (spk.getBoolean(appSetter_OSK.keyIsLogin, false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, MasterAct_OSK.class));
                    finish();
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, LogInAct_OSK.class));
                    finish();
                }
            }, 1000);
        }


    }//onCreate ========================================



    public String[] getDateDifferenceInYearK(String bDate) {
        String[] result = new String[2];
        result[0] = "";
        result[1] = "";
        Calendar cal = Calendar.getInstance();
        /*Log.i("test", bDate.substring(0, 4));
        Log.i("test", bDate.substring(5, 7));
        Log.i("test", bDate.substring(8, 10));
        Log.i("test", cal.get(Calendar.YEAR)+"");
        Log.i("test", cal.get(Calendar.MONTH)+"");*/
        int bDayYY = Integer.parseInt(bDate.substring(0, 4));
        int bDayMM = Integer.parseInt(bDate.substring(5, 7));
        int curYY = cal.get(Calendar.YEAR);
        int curMM = cal.get(Calendar.MONTH) + 1;
        int age = curYY-bDayYY;
        if (curMM > bDayMM) {
            age--;
        }
        int range = (int) (age/10);
        int rangeFrom = range*10;
        result[0] = age+"";
        result[1] = rangeFrom +"-"+ (rangeFrom+10);
        return result;
    }//getDateDifferenceInYearK

    public boolean setUpGCM() {
        if (!isNetworkAvailableK3(false)) {
            Log.i("setUpGCM", "no internet");
            return false;
        }
        /** for 1st time register device device_type DVL-Up-er.Kroid*/
        // Make sure the device has the proper dependencies.
        //	GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        Log.i("regid sdsds", regId + "");
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(MainActivity.this, appSetter_OSK.gcmSenderID);
            return false;
        }
        return true;
    }//setUpGCM

    public boolean isNetworkAvailableK3(boolean isShow) {
        /* DVL-Up-er.Kroid */
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return true;
        } else {
            if (isShow) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.sInternetReq),
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }//isNetworkAvailableK3

}
