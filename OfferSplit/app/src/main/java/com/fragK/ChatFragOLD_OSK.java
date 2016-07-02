package com.fragK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actK.MasterAct_OSK;
import com.adptrK.adptrChat_OSK;
import com.appKroid.appSetter_OSK;
import com.kroid.menudrawer.MenuDrawer;
import com.objectK.objChatK;
import com.objectK.objDealMasterK;
import com.offersplit.R;

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

import java.util.ArrayList;
import java.util.Collection;

public class ChatFragOLD_OSK extends Fragment {

    private View rootView;
    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static ChatFragOLD_OSK tempP;

    ImageView imgHomeBtn;
    EditText edChat;
    RelativeLayout relChatBtmSend;
    TextView txtTop;
    ListView listViewChat;
    adptrChat_OSK adp;
    public static ArrayList<objChatK> item;

    String lorem = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

    int pos;
    ArrayList<objDealMasterK> list;
    objDealMasterK curObj;
    String toID="";

    public static boolean isChatOpen;

    /* --- XMPP --- XMPP --- XMPP --- XMPP --- */
    public static final String HOST = appSetter_OSK.openFireHOST;//acra.dowhistle.com
    public static final int PORT = 5222;
    public static final String SERVICE = appSetter_OSK.openFireSERVICE;//dowhistle.com
    public static String USERNAME = "";//admin
    public static final String PASSWORD = appSetter_OSK.openFirePassword;//Password
    private XMPPConnection connection;
    private Handler mHandler = new Handler();
    /* --- XMPP --- XMPP --- XMPP --- XMPP --- */

    @Override
    public void onStart() {
        super.onStart();
        MasterAct_OSK.mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        isChatOpen = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isChatOpen = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chat_xk, container, false);

        spk = getActivity().getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempP = this;
        MasterAct_OSK.curFragNum = appSetter_OSK.fragNumChat;

        imgHomeBtn = (ImageView) rootView.findViewById(R.id.imgHomeBtn);

        edChat = (EditText) rootView.findViewById(R.id.edChat);

        relChatBtmSend = (RelativeLayout) rootView.findViewById(R.id.relChatBtmSend);

        listViewChat = (ListView) rootView.findViewById(R.id.listViewChat);

        txtTop = (TextView) rootView.findViewById(R.id.txtTop);

        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup) rootView, tf);*/

        // setups -------------------------------------------

        list = new ArrayList<objDealMasterK>();
        curObj = new objDealMasterK();
        Bundle b = getArguments();
        pos = b.getInt("pos");
        list = b.getParcelableArrayList("list");
        curObj = list.get(pos);

        if (curObj.get_idUser().equals(spk.getString(appSetter_OSK.keyID_, "noid"))) {
            toID = curObj.getAccepted();
            txtTop.setText(curObj.getAcceptedBy());
        } else {
            toID = curObj.get_idUser();
            txtTop.setText(curObj.getName());
        }

        USERNAME = spk.getString(appSetter_OSK.keyID_, "");
        Log.i("osk", "my username " + USERNAME);


        item = new ArrayList<objChatK>();
        adp = new adptrChat_OSK(getActivity(), item);
        listViewChat.setAdapter(adp);

        connectToXMPPK();

        // onclick events ----------------------------------

        imgHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        relChatBtmSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edChat.getText().toString().length()!=0) {
                    Log.i("osk", "Sending text " + edChat.getText().toString() + " to " + toID);
                    Message msg = new Message(toID +"@"+ appSetter_OSK.openFireHOST,
                            Message.Type.chat);
                    msg.setBody(edChat.getText().toString());
                    if (connection != null) {
                        connection.sendPacket(msg);
                        item.add(new objChatK("out", edChat.getText().toString()));
                        adp.notifyDataSetChanged();
                        listViewChat.setSelection(adp.getCount());
                        edChat.setText("");
                    }
                }
            }
        });

        return rootView;
    }//oncreate =================================================

    public void outMessageToChatKroid() {

    }//outMessageToChatKroid

    public void connectToXMPPK() {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Connecting...",
                "Please wait...", false);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a connection
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

                    // Set the status to available
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    setConnectionK(connection);

                    Roster roster = connection.getRoster();
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

                    }
                } catch (XMPPException ex) {
                    Log.e("osk", "Failed to log in as " + USERNAME);
                    Log.e("osk", ex.toString());
                    setConnectionK(null);
                }

                dialog.dismiss();
            }
        });
        t.start();
        dialog.show();
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
                        Log.i("osk", "Text Recieved " + message.getBody() + " from " + fromName);
                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {
                                item.add(new objChatK("in", message.getBody()));
                                adp.notifyDataSetChanged();
                                listViewChat.setSelection(adp.getCount());
                            }
                        });
                    } else {
                        Log.i("osk", "Recieved message body null" );
                    }
                }
            }, filter);
        } else {
            Log.i("osk", "connection null" );
        }
    }//setConnectionK

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Exception e) {
            Log.i("osk", "Exception onDestroy" );
        }
    }

}
