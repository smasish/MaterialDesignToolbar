package com.efilx.activity;

/**
* Created by Dinesh on 4/19/2015.
*/

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.adapter.OptionAdapter;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends ActionBarActivity implements View.OnClickListener {

    Context context;
    static InternetConnection internetConnection;
    static Boolean isConected = false;
    EditText etx_name, etx_email, etx_password, etx_mobile_no;
    Button btn_sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        context = this;

        etx_name =(EditText)findViewById(R.id.etx_name);
        etx_email =(EditText)findViewById(R.id.etx_email);
        etx_password =(EditText)findViewById(R.id.etx_password);
        etx_mobile_no =(EditText)findViewById(R.id.etx_mobile_no);
        btn_sign_up =(Button)findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(this);

    }


//    private void makeHttpRequest() {
//
//
//        if (etx_name.getText().toString().isEmpty()) {
//            etx_name.setError("Name field is required to fill-up!");
//        } else if (etx_email.getText().toString().isEmpty()) {
//            etx_email.setError("Email field is required to fill-up!");
//        } else if (etx_password.getText().toString().isEmpty()) {
//            etx_password.setError("Password field is required to fill-up!");
//        } else if (etx_mobile_no.getText().toString().isEmpty()) {
//            etx_mobile_no.setError("Mobile field is required to fill-up!");
//        } else {
//
//            RequestQueue mVolleyQueue = Volley.newRequestQueue(context);
//            JSONObject params = new JSONObject();
//
//            try {
//                params.put("Name ", etx_name.getText().toString());
//                params.put("Email", etx_email.getText().toString());
//                params.put("Password ", etx_password.getText().toString());
//                params.put("Mobile", etx_mobile_no.getText().toString());
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Api.update_account, params, //Not null.
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                Toast.makeText(context, response.getString("ACK").toString(), Toast.LENGTH_SHORT).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
////                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                    //pDialog.hide();
//                    Log.e("Exception", error.toString());
//                }
//            });
//
//// Adding request to request queue
//
//            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            mVolleyQueue.add(jsonObjReq);
//        }
//    }

    private void makeHttpRequest() {

        if (etx_email.getText().toString().isEmpty()) {
            etx_email.setError("Email field is required to fill-up!");
        } else if (etx_password.getText().toString().isEmpty()) {
            etx_password.setError("Password field is required to fill-up!");
        } else {
            RequestQueue mVolleyQueue = Volley.newRequestQueue(context);

            String url = "http://encomapps.com/eflix/user-change-password.php?email=" + etx_email.getText().toString() +
                    "&pass=" + etx_password.getText().toString();
            JsonObjectRequest jReq = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        try {

                            Toast.makeText(context, response.getString("ACK").toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //sToast(error.toString());
                    //Log.e("Exception",error.toString());
                }
            });

            jReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mVolleyQueue.add(jReq);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_up:
                internetConnection = new InternetConnection(context);
                isConected = internetConnection.isConnectingToInternet();
                if(isConected){

                    makeHttpRequest();
                }else{
                    Utils.AlertDialog(AccountActivity.this);
                    return;
                }
                break;


            default:
                return;
        }
    }
}