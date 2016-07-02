package com.adptrK;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.RelativeLayout;

import com.kroid.utils.ViewUtilsKroid;
import com.objectK.objChatK;
import com.offersplit.R;

import java.util.ArrayList;

/**
 * Created by Kinchit .
 */
public class adptrChat_OSK extends BaseAdapter {

    Context c;
    ArrayList<objChatK> data;

    Typeface tf;

    public static adptrChat_OSK tempAdp;

    public adptrChat_OSK(Context context, ArrayList<objChatK> d) {
        data = new ArrayList<objChatK>();
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
            vi = inflater.inflate(R.layout.list_chat_wmk3, null);
            holder = new viewHolder();

            holder.txtMainIncoming = (TextView) vi.findViewById(R.id.txtMainIncoming);
            holder.txtMainOutgoing = (TextView) vi.findViewById(R.id.txtMainOutgoing);
            holder.relMainIncoming = (RelativeLayout) vi.findViewById(R.id.relMainIncoming);
            holder.relMainOutgoing = (RelativeLayout) vi.findViewById(R.id.relMainOutgoing);

            ViewUtilsKroid.changeFontsKroid((ViewGroup) vi, tf);
            vi.setTag(holder);
        } else {
            holder = (viewHolder) vi.getTag();
        }


        if (data.get(position).getFrom().equals("in")) {
            holder.txtMainIncoming.setVisibility(View.VISIBLE);
            holder.txtMainOutgoing.setVisibility(View.GONE);
            holder.relMainIncoming.setVisibility(View.VISIBLE);
            holder.relMainOutgoing.setVisibility(View.GONE);

            holder.txtMainIncoming.setText(data.get(position).getText());
        } else {
            holder.txtMainIncoming.setVisibility(View.GONE);
            holder.txtMainOutgoing.setVisibility(View.VISIBLE);
            holder.relMainIncoming.setVisibility(View.GONE);
            holder.relMainOutgoing.setVisibility(View.VISIBLE);

            holder.txtMainOutgoing.setText(data.get(position).getText());
        }

        return vi;
    }//getView

    class viewHolder {
        TextView txtMainIncoming, txtMainOutgoing;
        RelativeLayout relMainIncoming, relMainOutgoing;
    }

}
