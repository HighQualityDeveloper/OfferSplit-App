package com.adptrK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.objectK.objDealMasterK;
import com.objectK.objPinK;
import com.offersplit.R;

public class PopupAdapter_OSK implements InfoWindowAdapter {

  private View popup=null;
  private LayoutInflater inflater=null;
  private ArrayList<objDealMasterK> data =null;
  private Context ctxt=null;
  private Marker lastMarker=null;

  public PopupAdapter_OSK(Context ctxt, LayoutInflater inflater, ArrayList<objDealMasterK> d) {
    this.ctxt=ctxt;
    this.inflater=inflater;
    this.data=d;

  }

  @Override
  public View getInfoWindow(Marker marker) {
    return(null);
  }

  @SuppressLint("InflateParams")
  @Override
  public View getInfoContents(Marker marker) {
    if (popup == null) {
      popup=inflater.inflate(R.layout.marker_view_xk, null);
    }

    if (lastMarker == null
        || !lastMarker.getId().equals(marker.getId())) {
      lastMarker=marker;

      TextView tv=(TextView)popup.findViewById(R.id.title);
      tv.setText(marker.getTitle());

      TextView tv1=(TextView)popup.findViewById(R.id.snippet);
      tv1.setText(marker.getSnippet());

    }

    return(popup);
  }

}