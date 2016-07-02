package com.kroid.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Point;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Pattern;

/** A collection of static methods useful for working with Android applications. **/
public class UtilsKroid {

	/**
	 * Returns a String with the formatted, localized date and with slashes
	 * replaced by periods.
	 **/
	public static String getLocalizedDate(Context context, Date date) {
		java.text.DateFormat dateForm = android.text.format.DateFormat
				.getDateFormat(context);
		String localizedDate = dateForm.format(date);
		return localizedDate.replace("/", ".");
	}

	/** A simple (ish) regex for checking valid emails **/
	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public final static boolean isValidEmailKroid(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
				.matches();
	}

	/** Returns screen size as a point in pixels **/
	public static Point getScreenSize(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return new Point(metrics.widthPixels, metrics.heightPixels);
	}

	/**
	 * Returns a point representing the xy coordinates of the (I think it's
	 * top-left) corner of the view
	 **/
	public static Point getWindowLocation(Context context, View v) {
		int[] locInWindow = { 0, 0 };
		v.getLocationInWindow(locInWindow);
		return new Point(locInWindow[0], locInWindow[1]);
	}

	/** Returns the pixel size of the status bar **/
	public static int getStatusBarHeight(Context context) {
		return (int) Math
				.ceil(25 * context.getResources().getDisplayMetrics().density);
	}

	public static int dpToPixels(Context context, float dp) {
		return (int) Math.ceil(dp
				* context.getResources().getDisplayMetrics().density);
	}

	public static float pixelsToSp(Context context, Float px) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return px / scaledDensity;
	}

	public static float spToPixels(Context context, Float sp) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return sp * scaledDensity;
	}

	/** Converts a Point representing pixels into a PointF representing inches **/
	public static PointF pixelsToInches(Context context, Point point) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return new PointF(point.x / dm.xdpi, point.y / dm.ydpi);
	}

	/** Retrieves a single resourceId from the context and attribute id. **/
	public static int getThemedResourceId(Context context, int attrId) {
		TypedValue tv = new TypedValue();
		context.getTheme().resolveAttribute(attrId, tv, true);
		return tv.resourceId;
	}

	public static void displayAlert(final Context c, String texttitle,
			String msg, String buttontitle, final Intent intent,
			final boolean isshow) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

		// set title
		alertDialogBuilder.setTitle(texttitle);

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(buttontitle,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								if (isshow) {
									if (intent != null)
										c.startActivity(intent);
									else
										((Activity) c).onBackPressed();
								}
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	// Showing Alert Message
	/**
	 * Print hash key
	 */
	public static void printHashKey(Context context) {
		try {
			String TAG = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				Log.d(TAG, "keyHash: " + keyHash);
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}
}
