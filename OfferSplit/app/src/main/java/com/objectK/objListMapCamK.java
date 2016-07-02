package com.objectK;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kinchit.
 */
public class objListMapCamK {

    LatLng latLng = null;
    float zoom ;

    public objListMapCamK() {

    }

    public objListMapCamK(LatLng latLng, float zoom) {
        this.latLng = latLng;
        this.zoom = zoom;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
