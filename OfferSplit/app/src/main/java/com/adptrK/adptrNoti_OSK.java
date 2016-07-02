package com.adptrK;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import com.kroid.utils.ViewUtilsKroid;
import com.objectK.objNotiK;
import com.offersplit.R;

import java.util.ArrayList;

/**
 * Created by Kinchit .
 */
public class adptrNoti_OSK extends BaseAdapter {

    Context c;
    ArrayList<objNotiK> data;

    Typeface tf;

    public static adptrNoti_OSK tempAdp;

    public adptrNoti_OSK(Context context, ArrayList<objNotiK> d) {
        data = new ArrayList<objNotiK>();
        data = d;
        c = context;
        tempAdp = this;

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
            vi = inflater.inflate(R.layout.list_notification_xk, null);
            holder = new viewHolder();

            holder.txtNotiTitle = (TextView) vi.findViewById(R.id.txtNotiTitle);
            holder.imgAccRej = (ImageView) vi.findViewById(R.id.imgAccRej);

            ViewUtilsKroid.changeFontsKroid((ViewGroup) vi, tf);
            vi.setTag(holder);
        } else {
            holder = (viewHolder) vi.getTag();
        }

        holder.txtNotiTitle.setText(data.get(position).getMessage());
        if (data.get(position).getStatus().equals("accepted")) {
            holder.imgAccRej.setImageResource(R.drawable.acceptbtn);
        } else {
            holder.imgAccRej.setImageResource(R.drawable.cancelbtn_);
        }

        /*if (position==2) {
            String name = "Madhu";
            String action = " " + c.getResources().getString(R.string.sNotiRej) + " ";
            String store = "Pizza Corner";
            String finalText = name + action + store;
            SpannableString ss1=  new SpannableString(finalText);
            ss1.setSpan(new RelativeSizeSpan(1.30f), 0, name.length(), 0); // set size
            ss1.setSpan(new RelativeSizeSpan(1.30f), name.length() + action.length(), finalText.length(), 0);
            //ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);// set color
            holder.txtNotiTitle.setText(ss1);
            holder.imgAccRej.setImageResource(R.drawable.cancelbtn_);
        } else {
            String name = "Suresh";
            String action = " " + c.getResources().getString(R.string.sNotiAcc) + " ";
            String store = "Zucca Pizzeria";
            String finalText = name + action + store;
            SpannableString ss1=  new SpannableString(finalText);
            ss1.setSpan(new RelativeSizeSpan(1.30f), 0, name.length(), 0); // set size
            ss1.setSpan(new RelativeSizeSpan(1.30f), name.length()+action.length(), finalText.length(), 0);
            //ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);// set color
            holder.txtNotiTitle.setText(ss1);
            holder.imgAccRej.setImageResource(R.drawable.acceptbtn);
        }*/

        return vi;
    }//getView

    class viewHolder {
        TextView txtNotiTitle;
        ImageView imgAccRej;
    }

}
