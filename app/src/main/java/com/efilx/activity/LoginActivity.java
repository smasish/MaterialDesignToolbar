package com.efilx.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
* Created by Dinesh on 4/19/2015.
*/
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    Context context;
    static InternetConnection internetConnection;
    static Boolean isConected = false;
    TextView   txt_create_account;
    Button btn_twitter, btn_login, btn_facebook;
    EditText etx_email, etx_password;



    /* Shared preference keys */
    private static final String PREF_NAME = "sample_twitter_pref";
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
    private static final String PREF_USER_NAME = "twitter_user_name";

    /* Any number for uniquely distinguish your request */
    public static final int WEBVIEW_REQUEST_CODE = 100;

    private ProgressDialog pDialog;

    private static Twitter twitter;
    private static RequestToken requestToken;

    private static SharedPreferences mSharedPreferences;


    private String consumerKey = null;
    private String consumerSecret = null;
    private String callbackUrl = null;
    private String oAuthVerifier = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);
        context = this;
        initWidget();

    }

    private void initWidget(){

        etx_email = (EditText)findViewById(R.id.etx_email);
        etx_password = (EditText)findViewById(R.id.etx_password);

        txt_create_account = (TextView)findViewById(R.id.create_account);
        txt_create_account.setOnClickListener(this);

        btn_twitter = (Button)findViewById(R.id.btn_twitter);
        btn_twitter.setOnClickListener(this);

        btn_facebook = (Button)findViewById(R.id.btn_facebook);
        btn_facebook.setOnClickListener(this);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);


        initTwitterConfigs();




		/* Check if required twitter keys are set */
        if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
            Toast.makeText(this, "Twitter key and secret not configured",
                    Toast.LENGTH_SHORT).show();
            return;
        }

		/* Initialize application preferences */
        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);

        boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

		/*  if already logged in, then hide login layout and show share layout */
//        if (isLoggedIn) {
//            loginLayout.setVisibility(View.GONE);
//            shareLayout.setVisibility(View.VISIBLE);
//
//            String username = mSharedPreferences.getString(PREF_USER_NAME, "");
//            userName.setText(getResources ().getString(R.string.hello)
//                    + username);
//
//        } else {
//            loginLayout.setVisibility(View.VISIBLE);
//            shareLayout.setVisibility(View.GONE);

            Uri uri = getIntent().getData();

            if (uri != null && uri.toString().startsWith(callbackUrl)) {

                String verifier = uri.getQueryParameter(oAuthVerifier);

                try {

					/* Getting oAuth authentication token */
                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

					/* Getting user id form access token */
                    long userID = accessToken.getUserId();
                    final User user = twitter.showUser(userID);
                    final String username = user.getName();

					/* save updated token */
                    saveTwitterInfo(accessToken);

//                    loginLayout.setVisibility(View.GONE);
//                    shareLayout.setVisibility(View.VISIBLE);
//                    userName.setText(getString(R.string.hello) + username);

                } catch (Exception e) {
                    Log.e("Failed to login Twitter", e.getMessage());
                }
            }

//        }
    }

    @Override
    public void onClick(View v) {
   switch (v.getId()){

       case R.id.create_account:
           startActivity(new Intent(context, RegistrationActivity.class));
           overridePendingTransition(0,0);
           finish();
           break;

       case R.id.btn_login:
//           startActivity(new Intent(context, MainActivity.class));
//           overridePendingTransition(0,0);
//           finish();
           internetConnection = new InternetConnection(context);
           isConected = internetConnection.isConnectingToInternet();
           if(isConected){
               makeHttpRequest();
           }else{
               Utils.AlertDialog(LoginActivity.this);
               return;
           }
           break;

       case R.id.btn_facebook:
           startActivity(new Intent(context, MainActivity.class));
           overridePendingTransition(0,0);
           finish();
           break;

       case R.id.btn_twitter:
           loginToTwitter();
           break;

       default:
           return;
      }
    }

    private void makeHttpRequest() {

        if (etx_email.getText().toString().isEmpty()) {
            etx_email.setError("Email field is required to fill-up!");
        } else if (etx_password.getText().toString().isEmpty()) {
            etx_password.setError("Password field is required to fill-up!");
        } else {

            RequestQueue mVolleyQueue = Volley.newRequestQueue(context);
            JSONObject params = new JSONObject();

            try {

                params.put("Email", etx_email.getText().toString());
                params.put("Password ", etx_password.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

              JsonObjectRequest jsonObjReq = new JsonObjectRequest(Api.login, params, //Not null.
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("-----"+response.toString(),".....");
                                if(response.getString("email").equals(etx_email.getText().toString())){
                                    Toast.makeText(context, "Successful to Login ", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, " Failed to Login ", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //pDialog.hide();
                    Log.e("Exception", error.toString());
                }
            });

// Adding request to request queue

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mVolleyQueue.add(jsonObjReq);
        }
    }


    /**
     * Saving user information, after user is authenticated for the first time.
     * You don't need to show user to login, until user has a valid access toen
     */
    private void saveTwitterInfo(AccessToken accessToken) {

        long userID = accessToken.getUserId();

        User user;
        try {
            user = twitter.showUser(userID);

            String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.putString(PREF_USER_NAME, username);
            e.commit();

        } catch (TwitterException e1) {
            e1.printStackTrace();
        }
    }


    /* Reading twitter essential configuration parameters from strings.xml */
    private void initTwitterConfigs() {

        consumerKey = getString(R.string.twitter_consumer_key);
        consumerSecret = getString(R.string.twitter_consumer_secret);
        callbackUrl = getString(R.string.twitter_callback);
        oAuthVerifier = getString(R.string.twitter_oauth_verifier);
    }
    private void loginToTwitter() {

        //boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

      //  if (!isLoggedIn) {
            final ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(consumerKey);
            builder.setOAuthConsumerSecret(consumerSecret);

            final Configuration configuration = builder.build();
            final TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {

                requestToken = twitter.getOAuthRequestToken(callbackUrl);

                /**
                 *  Loading twitter login page on webview for authorization
                 *  Once authorized, results are received at onActivityResult
                 *  */
                final Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
                startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
      //  } else {

            //loginLayout.setVisibility(View.GONE);
            //shareLayout.setVisibility(View.VISIBLE);
       // }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String verifier = data.getExtras().getString(oAuthVerifier);
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                long userID = accessToken.getUserId();
                final User user = twitter.showUser(userID);
                String username = user.getName();

                saveTwitterInfo(accessToken);

               // loginLayout.setVisibility(View.GONE);
               // shareLayout.setVisibility(View.VISIBLE);
              //  userName.setText(LoginActivity.this.getResources().getString( R.string.app_name) + username);

            } catch (Exception e) {
                Log.e("Twitter Login Failed", e.getMessage());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
