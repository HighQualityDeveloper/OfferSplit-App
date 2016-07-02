package com.adptrK;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.util.Log;

import com.actK.MasterAct_OSK;
import com.kroid.objTimeKroid;
import com.kroid.utils.ViewUtilsKroid;
import com.objectK.objDealMasterK;
import com.offersplit.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Kinchit .
 */
public class adptrMaster_OSK extends BaseAdapter {

    Context c;
    ArrayList<objDealMasterK> data;
    int from;

    Typeface tf;

    public static adptrMaster_OSK tempAdp;

    DateFormatSymbols dfs;
    static String[] months;
    SimpleDateFormat dateFormat;

    public adptrMaster_OSK(Context context, ArrayList<objDealMasterK> d, int from) {
        data = new ArrayList<objDealMasterK>();
        data = d;
        c = context;
        this.from = from;
        tempAdp = this;

        dfs = new DateFormatSymbols(Locale.US);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        months = dfs.getMonths();

        //tf = Typeface.createFromAsset(c.getAssets(), appSetter_XK.fontPath);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = null;
        viewHolder holder = null;

        if (vi==null) {
            LayoutInflater inflater = (LayoutInflater)
                    c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_deal_xk, null);
            holder = new viewHolder();

            holder.txtTitle = (TextView) vi.findViewById(R.id.txtTitle);
            holder.txtDate = (TextView) vi.findViewById(R.id.txtDate);
            holder.txtDes = (TextView) vi.findViewById(R.id.txtDes);
            holder.txtPrice = (TextView) vi.findViewById(R.id.txtPrice);
            holder.txtRightCorner = (TextView) vi.findViewById(R.id.txtRightCorner);

            ViewUtilsKroid.changeFontsKroid((ViewGroup) vi, tf);
            vi.setTag(holder);
        } else {
            holder = (viewHolder) vi.getTag();
        }

        holder.txtTitle.setText(data.get(position).getDeal());
        holder.txtDes.setText(data.get(position).getShopName());
        holder.txtPrice.setText("Price : Rs. " +data.get(position).getPrice()+ "/person");//Price : Rs. 500/person

        if (data.get(position).getStartCur().length()!=0) {
            holder.txtDate.setText(getDateStringK(data.get(position).getStartCur().substring(0, 10)));
        } else {
            holder.txtDate.setText("");
        }

        if (from==1) {
            //String exDate = data.get(position).getExpiry().substring(0, 19);
            String exDate = data.get(position).getEndCur();//.substring(0, 19)
            exDate = exDate.replace("T", " ");
            objTimeKroid oTime = new objTimeKroid();
            oTime = MasterAct_OSK.tempMaster.getTimeDifferenceKroid(exDate);

            if (Integer.parseInt(oTime.getHour()) > 0) {
                if (Integer.parseInt(oTime.getHour())==1) {
                    holder.txtRightCorner.setText("Expires in " + oTime.getHour() + " hour");
                } else {
                    holder.txtRightCorner.setText("Expires in " + oTime.getHour() + " hours");
                }
            } else {
                holder.txtRightCorner.setText(Integer.parseInt(oTime.getMinute()) > 0 ?
                        "Expires in " +  oTime.getMinute()+" min" : "Expired");
            }
            /*if (Integer.parseInt(oTime.getDay()) > 0) {
                if (oTime.getTotal_week() > 3) {
                    holder.txtRightCorner.setText("Expires in " + oTime.getTotal_week() + " weeks");
                } else {
                    if (Integer.parseInt(oTime.getDay())==1) {
                        holder.txtRightCorner.setText("Expires in " + oTime.getDay() + " day");
                    } else {
                        holder.txtRightCorner.setText("Expires in " + oTime.getDay() + " days");
                    }
                }
            } else {
                if (Integer.parseInt(oTime.getHour()) > 0) {
                    if (Integer.parseInt(oTime.getHour())==1) {
                        holder.txtRightCorner.setText("Expires in " + oTime.getHour() + " hour");
                    } else {
                        holder.txtRightCorner.setText("Expires in " + oTime.getHour() + " hours");
                    }
                } else {
                    holder.txtRightCorner.setText(Integer.parseInt(oTime.getMinute()) > 0 ?
                            "Expires in " +  oTime.getMinute()+" min" : "Expired");
                }
            }*/
            //holder.txtRightCorner.setText("Expire in");
        } else if (from!=1) {
            holder.txtRightCorner.setText(data.get(position).getName());
        } else {
            holder.txtRightCorner.setText("");
        }

        return vi;
    }//getView

    class viewHolder {
        TextView txtTitle, txtDate, txtDes, txtPrice, txtRightCorner;
    }

    public String getDateStringK(String date) {
        //Log.i("osk", date+"");
        String strYear = date.substring(0,4);
        String strMonth = date.substring(5,7);
        String strDay = date.substring(8,10);
        String dt = strDay +" "+ getMonthForIntKroid3(Integer.parseInt(strMonth)-1);
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

    private String getDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mmaa");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        return dt;
    }//getDate

}
