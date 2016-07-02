package com.actK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.kroid.AlertAppFragKroid;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.offersplit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ForgotPassAct_OSK extends AppCompatActivity {

    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static ForgotPassAct_OSK tempf;

    TextView txtSendBtn, textView3;
    EditText edPhoneNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass_xk);

        txtSendBtn = (TextView) findViewById(R.id.txtSendBtn);
        textView3 = (TextView) findViewById(R.id.textView3);

        edPhoneNum = (EditText) findViewById(R.id.edPhoneNum);

        /*Typeface tf = Typeface.createFromAsset(getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup)
                getWindow().getDecorView().findViewById(android.R.id.content), tf);*/

        // setups ------------------------------------------

        edPhoneNum.setText("");

        SpannableString content = new SpannableString(getResources().getString(R.string.sRemembUrPass));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView3.setText(content);


        // onclick events ----------------------------------

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboardKroid();
                if (edPhoneNum.getText().toString().length()!=0) {
                    if (isNetworkAvailableK3(true)) {
                        kttpForgotPassK(edPhoneNum.getText().toString());
                    }
                } else {
                    Toast.makeText(ForgotPassAct_OSK.this, "Enter a phone number",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }//onCreate ========================================


    public void kttpForgotPassK(final String phone) {
        String url = serviceMasterOSK.getForgotPassReqURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type", "application/json");

        kttp.post(ForgotPassAct_OSK.this, url, entity, "application/json", new JsonHttpResponseHandler() {
            ProgressDialog pd = new ProgressDialog(ForgotPassAct_OSK.this);

            @Override
            public void onStart() {
                super.onStart();
                pd.setMessage(getResources().getString(R.string.sLoading));
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("osk", statusCode + "fail");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + " errorResponse " + errorResponse.toString());
                    if (errorResponse.has("message")) {
                        Toast.makeText(ForgotPassAct_OSK.this, errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPassAct_OSK.this, "Failed. Try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPassAct_OSK.this, "Error Try again.", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPassAct_OSK.this, "Time out. Try again", Toast.LENGTH_SHORT).show();
                }

                /*if (errorResponse.has("message")) {
                    try {
                        Toast.makeText(ForgotPassAct_OSK.this, errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ForgotPassAct_OSK.this, "Error", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("osk", statusCode + " content " + response.toString());
                    JSONObject jobject = response;
                    if (statusCode == 200) {
                        /*final AlertAppFragKroid alert = new AlertAppFragKroid();
                        Bundle b = new Bundle();
                        if (jobject.has("message")) {
                            Log.i("osk", jobject.getString("message"));
                            b.putString("message", jobject.getString("message"));
                        } else {
                            b.putString("message", getResources().getString(R.string.popForgotSuc));
                        }

                        alert.setArguments(b);
                        alert.setOnButtonClick(new AlertAppFragKroid.onAlertButtonKroid() {
                            @Override
                            public void onButtonOkClick() {
                                alert.dismiss();
                                onBackPressed();
                            }
                        });
                        alert.setCancelable(false);
                        alert.show(getSupportFragmentManager(), "Alert One");
                        if (jobject.has("message")) {
                            Log.i("osk", jobject.getString("message"));
                        }*/

                        if (jobject.has("key")) {
                            final String keyReceiv = jobject.getString("key");
                            AlertDialog.Builder a = new AlertDialog.Builder(ForgotPassAct_OSK.this);
                            a.setMessage("Enter a new password");
                            LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View vi = inflater.inflate(R.layout.custom_password_k, null);
                            final EditText edPassword = (EditText) vi.findViewById(R.id.edPassword);
                            a.setView(vi);
                            a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (edPassword.getText().toString().length() == 0) {
                                        Toast.makeText(ForgotPassAct_OSK.this, "Enter a new password",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (edPassword.getText().toString().length() < 6) {
                                        Toast.makeText(ForgotPassAct_OSK.this, "A minimum of 6 characters is required for the password",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (isNetworkAvailableK3(true)) {
                                            kttpResetPasswordK(phone,
                                                    edPassword.getText().toString(), keyReceiv);
                                        }
                                    }
                                }
                            });
                            a.setNegativeButton("Cancel", null);
                            a.show();
                        }

                    } else {
                        try {
                            Log.i("osk", statusCode + " errorResponse ");
                            if (response.has("message")) {
                                Toast.makeText(ForgotPassAct_OSK.this, response.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPassAct_OSK.this, "Fail. Try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotPassAct_OSK.this, "Error.", Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotPassAct_OSK.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }//kttpForgotPassK

    public void kttpResetPasswordK(String phone, String password, String passwordResetKey) {
        String url = serviceMasterOSK.getPasswordResetURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            /*"passwordResetKey": "3IXT",
            "password": "Qwerty",
            "phone": "+919865983933"*/
            jBody.put("phone", phone);
            jBody.put("password", password);
            jBody.put("passwordResetKey", passwordResetKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type", "application/json");

        kttp.post(ForgotPassAct_OSK.this, url, entity, "application/json", new JsonHttpResponseHandler() {
            ProgressDialog pd = new ProgressDialog(ForgotPassAct_OSK.this);

            @Override
            public void onStart() {
                super.onStart();
                pd.setMessage(getResources().getString(R.string.sLoading));
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + " errorResponse " + errorResponse.toString());
                    if (errorResponse.has("message")) {
                        Toast.makeText(ForgotPassAct_OSK.this, errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPassAct_OSK.this, "Failed. Try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPassAct_OSK.this, "Error Try again.", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPassAct_OSK.this, "Time out. Try again", Toast.LENGTH_SHORT).show();
                }

                /*if (errorResponse.has("message")) {
                    try {
                        Toast.makeText(ForgotPassAct_OSK.this, errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ForgotPassAct_OSK.this, "Error", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("osk", statusCode + " content " + response.toString());
                    JSONObject jobject = response;
                    if (statusCode == 200) {
                        final AlertAppFragKroid alert = new AlertAppFragKroid();
                        Bundle b = new Bundle();
                        b.putString("message", "Password changed successfully");
                        alert.setArguments(b);
                        alert.setOnButtonClick(new AlertAppFragKroid.onAlertButtonKroid() {
                            @Override
                            public void onButtonOkClick() {
                                alert.dismiss();
                                onBackPressed();
                            }
                        });
                        alert.setCancelable(false);
                        alert.show(getSupportFragmentManager(), "Alert One");
                        if (jobject.has("ok")) {
                            Log.i("osk", jobject.getBoolean("ok")+"");
                        }

                    } else {
                        try {
                            Log.i("osk", statusCode + " errorResponse ");
                            if (response.has("message")) {
                                Toast.makeText(ForgotPassAct_OSK.this, response.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPassAct_OSK.this, "Fail. Try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotPassAct_OSK.this, "Error.", Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotPassAct_OSK.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }//kttpResetPasswordK

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Hides the soft keyboard DVL-Up-er.Kroid
     */
    public void hideSoftKeyboardKroid() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboardKroid(View view) {
        /* DVL-Up-er.Kroid */
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public final static boolean isValidEmailKroid(CharSequence target) {
        /* DVL-Up-er.Kroid */
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }//isValidEmailK3

    public boolean isNetworkAvailableK3(boolean isShow) {
        /* DVL-Up-er.Kroid */
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return true;
        } else {
            if (isShow) {
                Toast.makeText(ForgotPassAct_OSK.this,
                        getResources().getString(R.string.sInternetReq),
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }//isNetworkAvailableK3

}
