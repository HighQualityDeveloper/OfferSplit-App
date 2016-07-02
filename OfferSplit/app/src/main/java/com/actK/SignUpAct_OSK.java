package com.actK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.DialogInterface;
import android.provider.Settings;

import com.appKroid.appSetter_OSK;
import com.appKroid.serviceMasterOSK;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fragK.HomeFrag_OSK;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kroid.GPSTrackerKroid;
import android.location.Address;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.offersplit.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SignUpAct_OSK extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    SharedPreferences spk;
    SharedPreferences.Editor edtr;
    public static SignUpAct_OSK tempSiup;

    TextView txtLoginBtn, txtShow, txtSignUpBtn;
    EditText edPhoneNum, edEmail, edPassword, edFullName, edRePassword;
    ImageView imgFB, imgG;

    public static double latitudeK, longitudeK;
    public static String strLatitude="", strLongitudeK="";
    public static String cityK, countryK;
    GPSTrackerKroid gps;

    /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
    private PendingAction pendingAction = PendingAction.NONE;
    private CallbackManager callbackManager;
    LoginButton loginbutton;
    String fbID="",fbEmail="",fbName="";
    /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/

    int reqCodeFB = 40, reqCodeG = 50;

    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/

    ProgressDialog pdMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());/*--- fb sdk ----*/
        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.i("loginManager","call -in savedIns");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.v("LoginActivity", "cancel -in savedIns");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.v("LoginActivity", "error -in savedIns");
                    }
                });
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(getPackageName());
            pendingAction = PendingAction.valueOf(name);
        }
        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
        setContentView(R.layout.sign_up_xk);

        spk = getSharedPreferences(appSetter_OSK.keyNameOfPref, Context.MODE_PRIVATE);
        tempSiup = this;

        txtLoginBtn = (TextView) findViewById(R.id.txtLogoutBtn);
        txtSignUpBtn = (TextView) findViewById(R.id.txtSignUpBtn);
        txtShow = (TextView) findViewById(R.id.txtShow);

        edPhoneNum = (EditText) findViewById(R.id.edPhoneNum);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edFullName = (EditText) findViewById(R.id.edFullName);
        edRePassword = (EditText) findViewById(R.id.edRePassword);
        edEmail = (EditText) findViewById(R.id.edEmail);

        imgFB = (ImageView) findViewById(R.id.imgFB);
        imgG = (ImageView) findViewById(R.id.imgG);

        /*Typeface tf = Typeface.createFromAsset(getAssets(), appSetter_XK.fontPath);
        ViewUtilsKroid.changeFontsKroid((ViewGroup)
                getWindow().getDecorView().findViewById(android.R.id.content), tf);*/

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Log.i("osk", "logout");
                signOutToGplusKroid();
            }
        });

        //Log.i("edFullName", edFullName.getInputType()+"");
        //Log.i("edPassword", edPassword.getInputType()+"");

        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
        loginbutton = (LoginButton) findViewById(R.id.button);
        /*loginbutton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginbutton.setTextColor(getResources().getColor(R.color.transparent));
        loginbutton.setBackgroundColor(getResources().getColor(R.color.transparent));*/
        loginbutton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash:error","NameNotFoundException");
        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash:error","NoSuchAlgorithmException");
        }//**FB_SDK**//**FB_SDK**
        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/

        /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(SignUpAct_OSK.this)
                .enableAutoManage(SignUpAct_OSK.this, this)/* OnConnectionFailedListener */
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/

        // setups ------------------------------------------

        pdMain = new ProgressDialog(SignUpAct_OSK.this);
        pdMain.setMessage(getResources().getString(R.string.sLoading));
        pdMain.setCancelable(false);
        //pdMain.show();

        gps = new GPSTrackerKroid(SignUpAct_OSK.this);
        strLatitude="";
        strLongitudeK="";

        SpannableString content = new SpannableString(getResources().getString(R.string.sLogin));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtLoginBtn.setText(content);

        // onclick events ----------------------------------

        txtLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpAct_OSK.this, LogInAct_OSK.class));
                finish();
            }
        });

        txtSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboardKroid();
                if (validateK()) {
                    if (isNetworkAvailableK3(true)) {
                        kttpRegistrationK(edFullName.getText().toString(),
                                edPhoneNum.getText().toString(),
                                edEmail.getText().toString(),
                                edPassword.getText().toString(), latitudeK, longitudeK);
                    }
                }
                /*startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                finish();*/
            }
        });

        txtShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edPassword.getInputType()==129) {
                    edPassword.setInputType(1);
                } else {
                    edPassword.setInputType(129);
                }
            }
        });

        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
                                Profile profile = Profile.getCurrentProfile();
                                //Log.i("response", response.toString());
                                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                                    /*graphObject: {
                                        "id": "1580604192220613",
                                        "birthday": "08\/25\/1986",
                                        "gender": "male",
                                        "email": "carsh.android@gmail.com",
                                        "name": "NTechno India"
                                    },
                                    error: null*/
                                //Log.i("token", AccessToken.getCurrentAccessToken().toString());
                                JSONObject job = response.getJSONObject();
                                if (job.has("id") && job.has("name") && job.has("email")) {
                                    try {
                                        /*Log.i("osk", job.getString("id"));
                                        Log.i("osk", job.getString("name"));
                                        Log.i("osk", job.getString("email"));*/
                                        if (isNetworkAvailableK3(true)) {
                                            kttpSocialLoginK(appSetter_OSK.socialTypeFB,
                                                    job.getString("id"), job.getString("name"),
                                                    job.getString("email"), "");
                                        } else {
                                            LoginManager.getInstance().logOut();//signout from facebook
                                            Log.i("osk", "logout");
                                        }

                                        //getFriendsKroid(job.getString("id"), job.getString("name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        /*alertKroid("","Exception "+ e.getMessage(), true, "Ok", null,
                                                "",null);*/
                                        LoginManager.getInstance().logOut();//signout from facebook
                                        Log.i("osk", "logout");
                                    }
                                } else {
                                    /*alertKroid("","Name not found", true, "Ok", null, "",null);*/
                                    LoginManager.getInstance().logOut();//signout from facebook
                                    Log.i("osk", "logout");
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                //parameters.putString("edges", "friendlists");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
                //alertKroid("", "Login cancelled", true, "Ok", null, "", null);
                LoginManager.getInstance().logOut();//signout from facebook
                Log.i("osk", "logout");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", "error");
                //alertKroid("","Error "+ exception.getMessage(), true, "Ok", null, "",null);
                LoginManager.getInstance().logOut();//signout from facebook
                Log.i("osk", "logout");
            }
        });
        /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/

        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignUpAct_OSK.this,
                        Arrays.asList("public_profile", "user_friends", "email"));
            }
        });

        imgG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInToGplusKroid();
            }
        });


    }//onCreate ========================================

    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {
            Log.i("osk", "G+ fail" + connectionResult.toString());
        } catch (NullPointerException e) {
            Log.i("osk", "G+ fail nullpointer");
        }
    }
    private void signInToGplusKroid() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, reqCodeG);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        //Log.d("osk", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //Log.i("osk", "g+ " + acct.getId() +" "+ acct.getDisplayName() +" "+ acct.getEmail());
            if (isNetworkAvailableK3(true)) {
                kttpSocialLoginK(appSetter_OSK.socialTypeGOOGLE,
                        acct.getId(), acct.getDisplayName(), acct.getEmail(), "");
            } else {
                signOutToGplusKroid();
            }
        } else {
            // Signed out, show unauthenticated UI.
            signOutToGplusKroid();
        }
    }
    private void signOutToGplusKroid() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        try {
                            Log.i("osk", "G+ " + status.toString());
                        } catch (NullPointerException e) {
                            Log.i("osk", "G+ Sout nullpointer");
                        }
                    }
                });
    }
    /*--- g+ sdk ------ g+ sdk ------ g+ sdk ------ g+ sdk ---*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == reqCodeG) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    /*--- fb sdk ------ fb sdk ------ fb sdk ------ fb sdk ---*/
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        //AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        //AppEventsLogger.deactivateApp(this);
    }

    public boolean validateK() {
        if (edFullName.getText().toString().length()==0) {
            Toast.makeText(SignUpAct_OSK.this, "Enter a full name",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edPhoneNum.getText().toString().length()==0) {
            Toast.makeText(SignUpAct_OSK.this, "Enter a phone number",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmailKroid(edEmail.getText().toString())) {
            Toast.makeText(SignUpAct_OSK.this, "Enter a valid email",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edPassword.getText().toString().length()==0) {
            Toast.makeText(SignUpAct_OSK.this, "Enter a password",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edPassword.getText().toString().length()<6) {
            Toast.makeText(SignUpAct_OSK.this, "A minimum of 6 characters is required for the password",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edRePassword.getText().toString().length()==0) {
            Toast.makeText(SignUpAct_OSK.this, "Re-enter a password",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!edRePassword.getText().toString().equals(edPassword.getText().toString())) {
            Toast.makeText(SignUpAct_OSK.this, "Passwords don't match",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (strLatitude.length()==0 && strLongitudeK.length()==0) {
            callGPSK();
            return false;
        }
        return true;
    }//validateK

    private void callGPSK() {
        gps.getLocationKroid(new GPSTrackerKroid.onLocationCallKroid() {
            @Override
            public void onGetLocationKroid(double latitude, double longitude) {
                latitudeK = latitude;
                longitudeK = longitude;
                strLatitude = latitude + "";
                strLongitudeK = longitude + "";
                Address add = gps.getGeocoderListK(latitudeK, longitudeK);
                cityK = add.getLocality();
                countryK = add.getCountryName();
                /*AlertDialog.Builder a = new AlertDialog.Builder(SignUpAct_OSK.this);
                a.setMessage("select");
                a.setPositiveButton("one", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        latitudeK = 23.024024;
                        longitudeK = 72.559636;
                        strLatitude = 23.024024+"";
                        strLongitudeK = 72.559636+"";
                        Address add = gps.getGeocoderListK(latitudeK, longitudeK);
                        cityK = add.getLocality();
                        countryK = add.getCountryName();
                    }
                });
                a.setNegativeButton("two", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        latitudeK = 23.014749;
                        longitudeK = 72.559154;
                        strLatitude = 23.014749 + "";
                        strLongitudeK = 72.559154 + "";
                        Address add = gps.getGeocoderListK(latitudeK, longitudeK);
                        cityK = add.getLocality();
                        countryK = add.getCountryName();
                    }
                });
                a.show();*/
            }

            @Override
            public void onNotEnableKroid(String message) {
                showSettingsAlertK3();
            }
        });
    }//callGPSK

    public void showSettingsAlertK3() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpAct_OSK.this);
        alertDialog.setTitle("GPS setting");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to setting menu");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }//showSettingsAlertK3

    public void kttpRegistrationK(String name ,String phone ,String email , final String password,
                                  double lat, double lng) {
        /*{
            "user": {
                "name": "Whistler",
                "phone": "+919865983933",
                "password": "Qwerty@123",
                "email": "onewhistle@gmail.com",
                "location": [
                    76.935032,
                    11.040691
                ]
            }
        }*/
        String url = serviceMasterOSK.getSignupURL();
        //Log.i("osk", "url "+url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            JSONObject jUsr = new JSONObject();
            jUsr.put("name", name);
            jUsr.put("phone", phone);
            jUsr.put("password", password);
            jUsr.put("email", email);

            JSONArray jLoc = new JSONArray();
            jLoc.put(lng);
            jLoc.put(lat);
            // 160528 change - long lat

            jUsr.put("location", jLoc);

            jBody.put("user", jUsr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type", "application/json");

        kttp.post(SignUpAct_OSK.this, url, entity, "application/json", new JsonHttpResponseHandler() {
            //ProgressDialog pd = new ProgressDialog(SignUpAct_OSK.this);

            @Override
            public void onStart() {
                super.onStart();
                /*pd.setMessage(getResources().getString(R.string.sLoading));
                pd.setCancelable(false);
                pd.show();*/
                pdMain.setMessage("Loading.");
                if (!pdMain.isShowing()) {
                    pdMain.show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + " fail " );
                    Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                    //Toast.makeText(SignUpAct_OSK.this, statusCode + " " + errorResponse.toString(), Toast.LENGTH_SHORT).show();
                    if (errorResponse.has("message")) {
                        Toast.makeText(SignUpAct_OSK.this, errorResponse.getString("message"),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpAct_OSK.this, "Please try again after some time",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(SignUpAct_OSK.this, "Please try again after some time", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpAct_OSK.this, "Please try again after some time", Toast.LENGTH_SHORT).show();
                }
                if (pdMain.isShowing()) {
                    pdMain.dismiss();
                }
                /*if (statusCode == 400) {
                    Toast.makeText(SignUpAct_OSK.this, "Phone number or email already registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpAct_OSK.this, "Error", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("osk", statusCode + " content ");
                    JSONObject jobject = response;
                    if (statusCode == 200) {
                        /*"newUser": {
                            "_id": "571a0ee33acf49801d6bbbea",
                                    "name": "Whistler",
                                    "phone": "+919865983933",
                                    "email": "onewhistle@gmail.com",
                                    "location": {
                                "type": "Point",
                                "coordinates": [
                                    76.935032,
                                    11.040691
                                ]
                            },
                            "deals": [

                            ],
                            "settings": {
                                "whistle": true,
                                "radius": 10,
                                "notification": true,
                                "expiry": 24
                            },
                            "createdAt": "2016-04-22T11:45:39.017Z",
                            "accessToken": "1e4b018d414e1f27e690682140bdbc4e2f9ba138017d39e7cc700b1268564bb72b11694d41733715dfc"
                        }*/
                        if (jobject.has("newUser")) {
                            JSONObject joUser = jobject.getJSONObject("newUser");
                            JSONObject joLocation = joUser.getJSONObject("location");
                            JSONArray jaCoord = joLocation.getJSONArray("coordinates");
                            JSONObject joSett = joUser.getJSONObject("settings");

                            edtr = spk.edit();
                            edtr.putString(appSetter_OSK.keyID_, joUser.getString("_id"));
                            edtr.putString(appSetter_OSK.keyUName, joUser.getString("name"));
                            edtr.putString(appSetter_OSK.keyUPhone, joUser.getString("phone"));
                            edtr.putString(appSetter_OSK.keyUEmail, joUser.getString("email"));
                            edtr.putString(appSetter_OSK.keyAccessToken, joUser.getString("accessToken"));
                            edtr.putString(appSetter_OSK.keyUPassword, password);

                            double ln = (double) jaCoord.get(0);
                            double lt = (double) jaCoord.get(1);
                            // 160528 change - long lat

                            edtr.putFloat(appSetter_OSK.keyULat, (float) lt);
                            edtr.putFloat(appSetter_OSK.keyULng, (float) ln);

                            edtr.putInt(appSetter_OSK.keyRadius, joSett.getInt("radius"));
                            /*if (joSett.has("expiry")) {
                                edtr.putInt(appSetter_OSK.keyExpiry, joSett.getInt("expiry"));
                            }
                            edtr.putBoolean(appSetter_OSK.keyIsWhistle, joSett.getBoolean("whistle"));*/
                            edtr.putBoolean(appSetter_OSK.keyIsNotification, joSett.getBoolean("notification"));

                            edtr.putBoolean(appSetter_OSK.keyIsLogin, true);
                            edtr.putString(appSetter_OSK.keyLoginAs, "");

                            edtr.commit();
                            kttpRegisterToXMPPK(joUser.getString("_id"), appSetter_OSK.openFirePassword,
                                    joUser.getString("name"), joUser.getString("email"));
                            /*startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                            finish();*/
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (pdMain.isShowing()) {
                        pdMain.dismiss();
                    }
                }
            }
        });
    }//kttpRegistrationK

    public void kttpRegisterToXMPPK(final String username , final String password, final String name,
                                  final String email) {
        String url = serviceMasterOSK.getMXPregiUserURL();
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            /*{
                "username": "app_user",
                "password": "p4ssword",
                "name": "App User",
                "email": "appuser@example.com"
            }*/
            if (username.length()!=0) {jBody.put("username", username);}
            if (password.length()!=0) {jBody.put("password", password);}
            if (name.length()!=0) {jBody.put("name", name);}
            if (email.length()!=0) {jBody.put("email", email);}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type","application/json");
        kttp.addHeader("Authorization", appSetter_OSK.openFireBigSecretKey);
        //kttp.addHeader("Accept","application/json");

        kttp.post(SignUpAct_OSK.this, url, entity, "application/json", new AsyncHttpResponseHandler() {
            //ProgressDialog pd = new ProgressDialog(SignUpAct_OSK.this);

            @Override
            public void onStart() {
                super.onStart();
                /*pd.setMessage("Loading");
                pd.setCancelable(false);
                pd.show();*/
                pdMain.setMessage("Loading..");
                if (!pdMain.isShowing()) {
                    pdMain.show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //pd.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String rsp = new String(responseBody);
                Log.i("osk", statusCode +" ");
                if (statusCode == 201) {
                    Log.i("osk", "register on XMPP");
                    /*startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                    finish();*/
                    kttpAddToGrpXMPPK(username, password, name, email);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    Log.i("osk", statusCode + " fail " );
                } catch (NullPointerException e) {
                    Log.i("osk", statusCode + " fail nullp");
                }
                Log.i("osk", "fail XMPP register");
                if (pdMain.isShowing()) {
                    pdMain.dismiss();
                }
                startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                finish();
            }

        });
    }//kttpRegisterToXMPPK

    public void kttpAddToGrpXMPPK(String username ,String password, final String name,
                                    final String email) {
        String url = serviceMasterOSK.getMXPaddToGrpUserURL(username, "chat");
        //Log.i("osk", "url " + url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        /*JSONObject jBody = new JSONObject();
        try {
            *//*{
                "username": "app_user",
                "password": "p4ssword",
                "name": "App User",
                "email": "appuser@example.com"
            }*//*
            if (username.length()!=0) {jBody.put("username", username);}
            if (password.length()!=0) {jBody.put("password", password);}
            if (name.length()!=0) {jBody.put("name", name);}
            if (email.length()!=0) {jBody.put("email", email);}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");*/

        kttp.addHeader("Content-Type","application/xml");
        kttp.addHeader("Authorization", appSetter_OSK.openFireBigSecretKey);
        //kttp.addHeader("Accept","application/json");
        /*Header: Authorization: Basic YWRtaW46MTIzNDU=
        Header: Content-Type application/xml
        POST http://example.org:9090/plugins/restapi/v1/users/testuser/groups/testGroup*/

        kttp.post(SignUpAct_OSK.this, url, new RequestParams(), new AsyncHttpResponseHandler() {
            //ProgressDialog pd = new ProgressDialog(SignUpAct_OSK.this);

            @Override
            public void onStart() {
                super.onStart();
                /*pd.setMessage("Loading");
                pd.setCancelable(false);
                pd.show();*/
                pdMain.setMessage("Loading...");
                if (!pdMain.isShowing()) {
                    pdMain.show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //pd.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String rsp = new String(responseBody);
                Log.i("osk", statusCode +" ");
                if (statusCode == 201) {
                    Log.i("osk", "add to chat on XMPP");
                    if (pdMain.isShowing()) {
                        pdMain.dismiss();
                    }
                    startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    Log.i("osk", statusCode + " fail ");
                } catch (NullPointerException e) {
                    Log.i("osk", statusCode + " fail nullp");
                }
                Log.i("osk", "fail XMPP add to chat");
                if (pdMain.isShowing()) {
                    pdMain.dismiss();
                }
                startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                finish();
            }

        });
    }//kttpAddToGrpXMPPK

    public void kttpSocialLoginK(final String type, final String id, final String name,
                                 final String email, final String phone) {
        String url = serviceMasterOSK.getSocialLoginURL();
       // Log.i("osk", "url "+url);
        AsyncHttpClient kttp = new AsyncHttpClient();
        JSONObject jBody = new JSONObject();
        try {
            /*"user": {
                "type": "google",
                "id": "12345678",
                "name": "nilam",
                "email": "nilam@gmail.com"
            }*/
            /*  "phone" : "+911234567890",
                "_coordinates": [76.935032,11.040691]*/

            JSONObject jUsr = new JSONObject();
            jUsr.put("type", type);
            jUsr.put("id", id);
            jUsr.put("name", name);
            jUsr.put("email", email);
            if (phone.length()!=0) {
                jUsr.put("phone", phone);
            }
            if (strLatitude.length()!=0 && strLongitudeK.length()!=0) {
                JSONArray jLoc = new JSONArray();
                jLoc.put(longitudeK);
                jLoc.put(latitudeK);
                // 160528 change - long lat
                jUsr.put("_coordinates", jLoc);
            }

            jBody.put("user", jUsr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("osk", jBody.toString());

        StringEntity entity = new StringEntity(jBody.toString(), "UTF-8");

        kttp.addHeader("Content-Type","application/json");

        kttp.post(SignUpAct_OSK.this, url, entity, "application/json", new JsonHttpResponseHandler() {
            //ProgressDialog pd = new ProgressDialog(SignUpAct_OSK.this);

            @Override
            public void onStart() {
                super.onStart();
                /*pd.setMessage(getResources().getString(R.string.sLoading));
                pd.setCancelable(false);
                pd.show();*/
                pdMain.setMessage("Loading.");
                if (!pdMain.isShowing()) {
                    pdMain.show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //pd.dismiss();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("osk", "fail" );
                if (pdMain.isShowing()) {
                    pdMain.dismiss();
                }
                if (statusCode == 409) {
                    AlertDialog.Builder a = new AlertDialog.Builder(SignUpAct_OSK.this);
                    a.setMessage("Enter a phone number to register");
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View vi = inflater.inflate(R.layout.custom_mobile_k, null);
                    final EditText edPhone = (EditText) vi.findViewById(R.id.edPhone);
                    a.setView(vi);
                    a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (edPhone.getText().toString().length() == 0) {
                                Toast.makeText(SignUpAct_OSK.this, "Enter a phone number",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else if (strLatitude.length() == 0 && strLongitudeK.length() == 0) {
                                callGPSK(type, id, name, email, edPhone.getText().toString());
                            } else {
                                if (isNetworkAvailableK3(true)) {
                                    kttpSocialLoginK(type, id, name, email, edPhone.getText().toString());
                                }
                            }
                        }
                    });
                    a.setNegativeButton("Cancel", null);
                    a.show();
                } else {
                    LoginManager.getInstance().logOut();
                    Log.i("osk", "logout");
                    signOutToGplusKroid();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.i("osk", statusCode + " fail ");
                    //Log.i("osk", "errorResponse " + errorResponse == null ? "null" : errorResponse.toString());
                    if (errorResponse.has("message")) {
                        try {
                            Toast.makeText(SignUpAct_OSK.this, errorResponse.getString("message"),
                                    Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (pdMain.isShowing()) {
                        pdMain.dismiss();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpAct_OSK.this, "Time out. Please try again later",
                            Toast.LENGTH_LONG).show();
                }
                if (statusCode == 409) {
                    AlertDialog.Builder a = new AlertDialog.Builder(SignUpAct_OSK.this);
                    a.setMessage("Enter a phone number to register");
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View vi = inflater.inflate(R.layout.custom_mobile_k, null);
                    final EditText edPhone = (EditText) vi.findViewById(R.id.edPhone);
                    a.setView(vi);
                    a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (edPhone.getText().toString().length() == 0) {
                                Toast.makeText(SignUpAct_OSK.this, "Enter a phone number",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else if (strLatitude.length() == 0 && strLongitudeK.length() == 0) {
                                callGPSK(type, id, name, email, edPhone.getText().toString());
                            } else {
                                if (isNetworkAvailableK3(true)) {
                                    kttpSocialLoginK(type, id, name, email, edPhone.getText().toString());
                                }
                            }
                        }
                    });
                    a.setNegativeButton("Cancel", null);
                    a.show();
                } else {
                    LoginManager.getInstance().logOut();
                    Log.i("osk", "logout");
                    signOutToGplusKroid();
                }
                /*if (errorResponse.has("message")) {
                    try {
                        Toast.makeText(LogInAct_OSK.this, errorResponse.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LogInAct_OSK.this, "Error",Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("osk", statusCode + " content " );
                    JSONObject jobject = response;
                    if (statusCode == 200) {
                        if (jobject.has("newUser")) {
                            JSONObject joUser = jobject.getJSONObject("newUser");
                            JSONObject joLocation = joUser.getJSONObject("location");
                            JSONArray jaCoord = joLocation.getJSONArray("coordinates");
                            JSONObject joSett = joUser.getJSONObject("settings");

                            edtr = spk.edit();
                            edtr.putString(appSetter_OSK.keyID_, joUser.getString("_id"));
                            edtr.putString(appSetter_OSK.keyUName, joUser.getString("name"));
                            edtr.putString(appSetter_OSK.keyUPhone, joUser.getString("phone"));
                            edtr.putString(appSetter_OSK.keyUEmail, joUser.getString("email"));
                            edtr.putString(appSetter_OSK.keyAccessToken, joUser.getString("accessToken"));

                            double ln = (double) jaCoord.get(0);
                            double lt = (double) jaCoord.get(1);
                            // 160528 change - long lat
                            Log.i("osk", "lat "+ lt + ", long "+ln);

                            edtr.putFloat(appSetter_OSK.keyULat, (float) lt);
                            edtr.putFloat(appSetter_OSK.keyULng, (float) ln);

                            edtr.putInt(appSetter_OSK.keyRadius, joSett.getInt("radius"));
                            /*if (joSett.has("expiry")) {
                                edtr.putInt(appSetter_OSK.keyExpiry, joSett.getInt("expiry"));
                            }
                            edtr.putBoolean(appSetter_OSK.keyIsWhistle, joSett.getBoolean("whistle"));*/
                            edtr.putBoolean(appSetter_OSK.keyIsNotification, joSett.getBoolean("notification"));

                            edtr.putBoolean(appSetter_OSK.keyIsLogin, true);
                            edtr.putString(appSetter_OSK.keyLoginAs, type);

                            edtr.commit();

                            if (phone.length()!=0) {
                                kttpRegisterToXMPPK(joUser.getString("_id"), appSetter_OSK.openFirePassword,
                                        joUser.getString("name"), joUser.getString("email"));
                            } else {
                                startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                                finish();
                            }

                            /*startActivity(new Intent(SignUpAct_OSK.this, MasterAct_OSK.class));
                            finish();*/
                        }
                    } else if (statusCode == 409) {
                        if (pdMain.isShowing()) {
                            pdMain.dismiss();
                        }
                        AlertDialog.Builder a = new AlertDialog.Builder(SignUpAct_OSK.this);
                        a.setMessage("Enter a phone number to register");
                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View vi = inflater.inflate(R.layout.custom_mobile_k, null);
                        final EditText edPhone = (EditText) vi.findViewById(R.id.edPhone);
                        a.setView(vi);
                        a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (edPhone.getText().toString().length() == 0) {
                                    Toast.makeText(SignUpAct_OSK.this, "Enter a phone number",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (strLatitude.length() == 0 && strLongitudeK.length() == 0) {
                                    callGPSK(type, id, name, email, edPhone.getText().toString());
                                } else {
                                    if (isNetworkAvailableK3(true)) {
                                        kttpSocialLoginK(type, id, name, email, edPhone.getText().toString());
                                    }
                                }
                            }
                        });
                        a.setNegativeButton("Cancel", null);
                        a.show();
                    } else {
                        LoginManager.getInstance().logOut();
                        Log.i("osk", "logout");
                        signOutToGplusKroid();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoginManager.getInstance().logOut();
                    Log.i("osk", "logout");
                    signOutToGplusKroid();
                }
            }
        });
    }//kttpSocialLoginK

    private void callGPSK(final String type, final String id, final String name,
                          final String email, final String phone) {
        gps.getLocationKroid(new GPSTrackerKroid.onLocationCallKroid() {
            @Override
            public void onGetLocationKroid(double latitude, double longitude) {
                latitudeK = latitude;
                longitudeK = longitude;
                strLatitude = latitude + "";
                strLongitudeK = longitude + "";

                HomeFrag_OSK.latitudeK = latitude;
                HomeFrag_OSK.longitudeK = longitude;
                HomeFrag_OSK.strLatitude = latitude + "";
                HomeFrag_OSK.strLongitudeK = longitude + "";
                /*Address add = gps.getGeocoderListK(latitudeK, longitudeK);
                cityK = add.getLocality();
                countryK = add.getCountryName();*/

                if (isNetworkAvailableK3(true)) {
                    kttpSocialLoginK(type, id, name, email, phone);
                }
            }

            @Override
            public void onNotEnableKroid(String message) {
                LoginManager.getInstance().logOut();
                Log.i("osk", "logout");
                signOutToGplusKroid();
                Toast.makeText(SignUpAct_OSK.this, "Sign in fail. Try again", Toast.LENGTH_SHORT).show();
                showSettingsAlertK3();
            }
        });
    }//callGPSK




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
                Toast.makeText(SignUpAct_OSK.this,
                        getResources().getString(R.string.sInternetReq),
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }//isNetworkAvailableK3
}
