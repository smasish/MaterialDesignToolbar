package com.efilx.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.activity.R;
import com.efilx.adapter.ShowAdapter;
import com.efilx.adapter.ViewInPagerAdapter;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ABC on 4/9/2015.
 */
public class TVShowActivity extends ActionBarActivity {

    public static final String DETAILS_URL_POSITION = "url_position";
    protected static final String CATEGORIES_NAME = "categories_name";

    static InternetConnection internetConnection;
    static Boolean isConected = false;

    JSONArray jsonArray;
    Context context;
    GridView gridView;
    LinearLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow);
        context = this;

        /**Initialize the Action tool bar**/
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle(getIntent().getExtras().getString(CATEGORIES_NAME));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         gridView = (GridView)findViewById(R.id.gridview);
        loading = (LinearLayout)findViewById(R.id.loading);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // startActivity(new Intent(TVShowActivity.this,VideoPlayerActivity.class));
                String urlVideo = null;
                try {
                    urlVideo = jsonArray.getJSONObject(position).getString("pid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(DetailsActivity.DEATIL_ID, urlVideo);
                intent.putExtra(DetailsActivity.DETAILS_URL_INDEX, Api.genere_5);
                startActivity(intent);
               overridePendingTransition(0, 0);
            }
        });

        internetConnection = new InternetConnection(context);
        isConected = internetConnection.isConnectingToInternet();
        if(isConected){
            makeHttpRequest();
        }else{
            Alert_Dialog();
            //sToast("No Internet Connection!");
            return;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeHttpRequest() {

        RequestQueue mVolleyQueue = Volley.newRequestQueue(context);
        String url = Api.gnevigation_details+""+Integer.valueOf(getIntent().getExtras().getString(DETAILS_URL_POSITION));

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                jsonArray = response;

                Utils.LoadingControl(false, loading);

               // Toast.makeText(context, jsonArray.toString(), Toast.LENGTH_SHORT).show();
                gridView.setAdapter(new ShowAdapter(context, jsonArray));

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
    private void LoadingControl(boolean spinning) {


        if (spinning){
            loading.setVisibility(View.VISIBLE);
        }
        else{
            loading.setVisibility(View.GONE);
        }
    }
    public void Alert_Dialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Internet Required !");

        // set dialog message
        alertDialogBuilder.setMessage("You have no internet connection. Please connect your device to internet.")
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        //dialog.cancel();
                        TVShowActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

