package com.objectK;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Kinchit.
 */
public class objDealMasterK implements Parcelable {

    String _idUser = "";
    String _idDeal = "";
    String name = "";
    String shopName = "";
    String deal = "";
    String price = "";
    String start = "";
    String end = "";
    String expiry = "";
    String comments = "";
    double lat = 0f;
    double lng = 0f;
    LatLng latLng;

    String accepted = "";
    String rejected = "";

    Marker marker ;
    String markerID = "";

    String phone = "";
    String posted = "";
    String acceptedBy = "";

    String startCur = "";
    String endCur = "";
    String expiryCur = "";

    public objDealMasterK() {
    }

    protected objDealMasterK(Parcel in) {
        _idUser = in.readString();
        _idDeal = in.readString();
        name = in.readString();
        shopName = in.readString();
        deal = in.readString();
        price = in.readString();
        start = in.readString();
        end = in.readString();
        expiry = in.readString();
        comments = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        accepted = in.readString();
        rejected = in.readString();
        markerID = in.readString();
        acceptedBy = in.readString();
        startCur = in.readString();
        endCur = in.readString();
        expiryCur = in.readString();
    }


    public static final Creator<objDealMasterK> CREATOR = new Creator<objDealMasterK>() {
        @Override
        public objDealMasterK createFromParcel(Parcel in) {
            return new objDealMasterK(in);
        }

        @Override
        public objDealMasterK[] newArray(int size) {
            return new objDealMasterK[size];
        }
    };

    public String get_idUser() {
        return _idUser;
    }

    public void set_idUser(String _idUser) {
        this._idUser = _idUser;
    }

    public String get_idDeal() {
        return _idDeal;
    }

    public void set_idDeal(String _idDeal) {
        this._idDeal = _idDeal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
        startCur = getDateUTCtoCurrentKroid(start);
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
        endCur = getDateUTCtoCurrentKroid(end);
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
        expiryCur = getDateUTCtoCurrentKroid(expiry);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setLatLngK(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        latLng = new LatLng(lat, lng);
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public String getStartCur() {
        return startCur;
    }

    public void setStartCur(String startCur) {
        this.startCur = startCur;
    }

    public String getEndCur() {
        return endCur;
    }

    public void setEndCur(String endCur) {
        this.endCur = endCur;
    }

    public String getExpiryCur() {
        return expiryCur;
    }

    public void setExpiryCur(String expiryCur) {
        this.expiryCur = expiryCur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_idUser);
        dest.writeString(_idDeal);
        dest.writeString(name);
        dest.writeString(shopName);
        dest.writeString(deal);
        dest.writeString(price);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(expiry);
        dest.writeString(comments);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeParcelable(latLng, flags);
        dest.writeString(accepted);
        dest.writeString(rejected);
        dest.writeString(markerID);
        dest.writeString(phone);
        dest.writeString(posted);
        dest.writeString(acceptedBy);
        dest.writeString(startCur);
        dest.writeString(endCur);
        dest.writeString(expiryCur);
    }

    private String getDateUTCtoCurrentKroid(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        Calendar cal = Calendar.getInstance();
        try {
            value = formatter.parse(dateString);
            cal.setTime(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);
        return dt;
    }//getDateUTCtoCurrentKroid

}
