package com.offersplit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.actK.MasterAct_OSK;
import com.appKroid.appSetter_OSK;
import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMIntentService extends GCMBaseIntentService {
	//SharedPreferences sp;
	private static final String TAG = "GCMIntentService";
	public static final String TAGK = "\n\t\t*    *\n*   *\n		*  *\n		* *\n		** *\n		*   *\n		*    * ";
	private final static int NOTIFICATION_ID = 333;
	private static NotificationManager mNotificationManager;
    private static final String appName = "Offer Split";
    private static final String keyNameOfPref = "Shared_OfferSplitK";
    private static final String keyNotiItems = "keynotidata";

	/**
	 * Method called on device registered
	 **/
    /*** DVL-Up-er.Kroid ***/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
        //MainActivity.tempMain.GCMkroid(registrationId);
		Log.i("call", "***   ***\n***  ***\n*** ***\n******\n***  ***\n***   ***\n***    ***");

	}

	/**
	 * Method called on device un registred
	 * */
    /*** DVL-Up-er.Kroid ***/
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
	//	ServerUtilities.unregister(context, registrationId);
		//****
		Log.i("call", TAGK);
	}

	/**
	 * Method called on Receiving a new message
	 * */
    /*** DVL-Up-er.Kroid ***/
	@Override
	protected void onMessage(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        Log.i(TAG, TAGK + "Received message");
        //Log.i("osk", b.toString());
        /*
        status=accepted,
        from=456823986802,
        title=OfferSplit,
        message=Salenewnewisacceptedbysona,
        collapse_key=do_not_collapse
        */
        String status="", from="",title="",message="";
        if (b.containsKey("status")) {
            status = b.getString("status");
        } else {
            return;
        }
        if (b.containsKey("from")) {
            from = b.getString("from");
        } else {
            return;
        }
        if (b.containsKey("title")) {
            title = b.getString("title");
        } else {
            return;
        }
        if (b.containsKey("message")) {
            message = b.getString("message");
        } else {
            return;
        }

        String dataJSON = "{ status:\""+status+"\", from:\""+from+"\", title:\""+title+"\", message:\""+message+"\" }";
        Log.i("osk", dataJSON);

        final SharedPreferences spk = getSharedPreferences(keyNameOfPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor edtr = spk.edit();
        if (spk.getString(keyNotiItems,"").length()==0) {
            edtr.putString(keyNotiItems, dataJSON);
            edtr.commit();
        } else {
            edtr.putString(keyNotiItems, spk.getString(keyNotiItems,"")+","+dataJSON);
            edtr.commit();
            Log.i("osk", ",");
        }

        if (MasterAct_OSK.isAppRunning) {
            generateNotificationCurFrag(context, title, message);
        } else {
            generateNotificationNew(context, title, message);
        }

	}//onMeassage


	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");

	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		/*
		 * displayMessage(context, getString(R.string.gcm_recoverable_error,
		 * errorId));
		 */
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */

    private static void generateNotificationNew(Context context, String title, String message ) {
        long when = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

			// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MainActivity.class);
            mBuilder.setColor(Color.parseColor("#00ADEF"));
            mBuilder.setSmallIcon(R.drawable.niticon);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setContentTitle(title.length()==0?appName:title);
            mBuilder.setContentText(message);
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(message);
            bigText.setBigContentTitle(title.length() == 0 ? appName : title);
            //bigText.setSummaryText("Por: GORIO Engenharia");
            mBuilder.setStyle(bigText);

            resultIntent.putExtra("fromnoti", true);

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
            mNotificationManager.notify(0, notification);
        }
        else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context);

			// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MainActivity.class);

            mBuilder.setContentTitle(title.length()==0?appName:title);
            mBuilder.setContentText(message);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);

            resultIntent.putExtra("fromnoti", true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);

			// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);
            Notification notification = mBuilder.getNotification();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

			// notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, notification);

        }
    }

    private static void generateNotificationCurFrag(Context context, String title, String message ) {
        long when = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
			// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MasterAct_OSK.class);
            mBuilder.setColor(Color.parseColor("#00ADEF"));
            mBuilder.setSmallIcon(R.drawable.niticon);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setContentTitle(title.length()==0?appName:title);
            mBuilder.setContentText(message);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(message);
            bigText.setBigContentTitle(title.length() == 0 ? appName : title);
            //bigText.setSummaryText("Por: GORIO Engenharia");
            mBuilder.setStyle(bigText);


            resultIntent.putExtra("fromnoti", true);

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
            mNotificationManager.notify(0, notification);
        }
        else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context);

			// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MasterAct_OSK.class);

            mBuilder.setContentTitle(title.length()==0?appName:title);
            mBuilder.setContentText(message);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);

            resultIntent.putExtra("fromnoti", true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);

			// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);
            Notification notification = mBuilder.getNotification();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

			// notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, notification);
        }
    }

}
