package com.efilx.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.adapter.OptionAdapter;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;

import org.json.JSONArray;

/**
 * Created by Dinesh on 4/27/2015.
 */
public class AboutActivity  extends ActionBarActivity {

    Context context;
    static InternetConnection internetConnection;
    static Boolean isConected = false;
    ListView listView;
    LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        context = this;

        listView = (ListView)findViewById(R.id.list_option);
        loading = (LinearLayout)findViewById(R.id.loading);

        internetConnection = new InternetConnection(context);
        isConected = internetConnection.isConnectingToInternet();
        if(isConected){
            makeHttpRequest();
        }else{
            Utils.AlertDialog(AboutActivity.this);
            return;
        }
    }

    private void makeHttpRequest() {

        RequestQueue mVolleyQueue = Volley.newRequestQueue(context);
        String url = Api.about;

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Utils.LoadingControl(false, loading);
                listView.setAdapter(new OptionAdapter(context,response,2));
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

