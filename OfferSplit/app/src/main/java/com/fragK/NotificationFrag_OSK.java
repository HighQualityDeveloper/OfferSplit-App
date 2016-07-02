package com.fragK;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actK.MasterAct_OSK;
import com.adptrK.adptrNoti_OSK;
import com.appKroid.appSetter_OSK;
import com.kroid.menudrawer.MenuDrawer;
import com.objectK.objNotiK;
import com.offersplit.R;

import java.util.ArrayList;

public class NotificationFrag_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static NotificationFrag_OSK tempN;

    ImageView imgHomeBtn;
    ListView listView;

    adptrNoti_OSK adpN;
    public static ArrayList<objNotiK> item;

    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.notification_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempN = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumNoti;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        listView = (ListView) rootView.findViewById(R.id.listView);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        int c = MasterAct_OSK.tempMaster.setNotiDataK();
        Log.i("osk", "notiCount " + c);

        item = MasterAct_OSK.itemNotiData;

        adpN = new adptrNoti_OSK(getActivity(), item);
        listView.setAdapter(adpN);

        if (item.size()==0) {
            Toast.makeText(getActivity(), "No pending notifications", Toast.LENGTH_LONG).show();
        }

        edtr = spk.edit();
        edtr.putString(appSetter_OSK.keyNotiItems, "");
        edtr.commit();

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }//oncreate =================================================


}
