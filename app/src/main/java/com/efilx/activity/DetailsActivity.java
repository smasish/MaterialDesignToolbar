package com.efilx.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.adapter.ExpandableAdapter;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dinesh on 4/21/2015.
 */
public class DetailsActivity extends ActionBarActivity implements View.OnClickListener {

    Context context;
    public static final String DEATIL_ID = "id";
    public static final String DETAILS_URL_INDEX = "url_index";
    static InternetConnection internetConnection;
    static Boolean isConected = false;
    JSONObject jsonObject;
    ExpandableListView expandbleLis;

    LinearLayout loading;
    ImageView img_video, img_album_thumb;
    TextView  txt_name, txt_details, txt_director, txt_witers, txt_stars, txt_release;
    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = this;
        initWidget();

         expandbleLis = (ExpandableListView)findViewById(R.id.expand_litview);
         expandbleLis.setDividerHeight(2);
        expandbleLis.setGroupIndicator(null);
        //expandbleLis.setClickable(true
        // );

//        setGroupData();
//        setChildGroupData();

//        ExpandableAdapter mNewAdapter = new ExpandableAdapter(groupItem, childItem);
//        mNewAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
//        expandbleLis.setAdapter(mNewAdapter);
//        expandbleLis.setOnChildClickListener(new OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(DetailsActivity.this, "Clicked On Child", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

    private void initWidget(){

        loading = (LinearLayout)findViewById(R.id.loading);
        img_video = (ImageView)findViewById(R.id.img_video);
        img_video.setOnClickListener(this);
        img_album_thumb = (ImageView)findViewById(R.id.img_album_thumb);

        txt_name = (TextView)findViewById(R.id.txt_name);
        txt_details = (TextView)findViewById(R.id.txt_details);
        txt_director = (TextView)findViewById(R.id.txt_director);
        txt_witers = (TextView)findViewById(R.id.txt_witers);
        txt_stars = (TextView)findViewById(R.id.txt_stars);
        txt_release = (TextView)findViewById(R.id.txt_release);

        internetConnection = new InternetConnection(context);
        isConected = internetConnection.isConnectingToInternet();
        if(isConected){
            makeHttpRequest();
        }else{
            Utils.AlertDialog(DetailsActivity.this);
            return;
        }
    }
    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.img_video:
         String urlVideo = null;
        try {

            setVideoUrl(jsonObject.getString("trailer"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

              break;
      }
    }



    private void makeHttpRequest() {
//        Toast.makeText(context, Integer.valueOf(getIntent().getExtras().getString(DETAILS_URL_INDEX))+""
//                +Integer.valueOf(getIntent().getExtras().getString(DEATIL_ID)), Toast.LENGTH_SHORT).show();
//

        RequestQueue mVolleyQueue = Volley.newRequestQueue(context);
        String url = Api.video_details[Integer.valueOf(getIntent().getExtras().getString(DETAILS_URL_INDEX))]+""+Integer.valueOf(getIntent().getExtras().getString(DEATIL_ID));

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonObject = response;
                Utils.LoadingControl(false, loading);
                setValue(response);
                try {
                    JSONArray jsonArray = response.getJSONArray("season");
                    ExpandableAdapter mNewAdapter = new ExpandableAdapter(DetailsActivity.this, jsonArray);
                    mNewAdapter.setInflater((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE), DetailsActivity.this);
                    expandbleLis.setAdapter(mNewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                ArrayList<String> child = new ArrayList<String>();
//                childItem.add(child);
               // Toast.makeText(context, response.toString()+" Data", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("error", error.toString());
                //Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mVolleyQueue.add(jsonObjRequest);
    }
    private void setValue(JSONObject jsonObject){

        try {

            UrlImageViewHelper.setUrlDrawable(img_video, jsonObject.getString("banner"));
            UrlImageViewHelper.setUrlDrawable(img_album_thumb, jsonObject.getString("thumb"));
            txt_name.setText(jsonObject.getString("ptitle"));
            txt_details.setText(jsonObject.getString("details"));
            txt_director.setText("Director: "+jsonObject.getString("director"));
            txt_witers.setText("Writer: "+jsonObject.getString("writer"));
            txt_stars.setText("Stars: "+jsonObject.getString("stars"));
            txt_release.setText("Release Date: "+jsonObject.getString("release_date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private  void setVideoUrl( String videoUrl){

//        //String urlVideo = null;
//        try {
//            videoUrl = jsonArray.getJSONObject(position).getString("svideo");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(context, VideoPlayerActivity.class );
        intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL,  videoUrl );
       startActivity(intent);
       overridePendingTransition(0,0);
    }
}
