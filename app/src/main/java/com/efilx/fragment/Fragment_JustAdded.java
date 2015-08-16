package com.efilx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.activity.DetailsActivity;
import com.efilx.activity.R;
import com.efilx.adapter.GridViewAdapter;
import com.efilx.adapter.ShowAdapter;
import com.efilx.adapter.ViewInPagerAdapter;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;
import com.efilx.view.ViewPagerIndicator;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ABC on 4/10/2015.
 */
public class Fragment_JustAdded  extends Fragment  {

    static InternetConnection internetConnection;
    static Boolean isConected = false;

    private static final String ARG_POSITION = "position";
    Context context;
    JSONArray jsonArray,jsonArray_grid;
    ViewPager mViewPager;
    ViewPagerIndicator mViewPagerIndicator;
    private int position;
    GridView gridView;
    LinearLayout loading, loading_grid;

    public static Fragment_JustAdded newInstance(int position) {
        Fragment_JustAdded f = new Fragment_JustAdded();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.fragment_just_added, container, false);
        context = rootView.getContext();



        internetConnection = new InternetConnection(context);
        isConected = internetConnection.isConnectingToInternet();
        if(isConected){
            makeHttpRequest();
        }else{
           Utils.AlertDialog(getActivity());
            //sToast("No Internet Connection!");
        }
        if(isConected){
            HttpRequestBottom();
        }else{
            Utils.AlertDialog(getActivity());
            //sToast("No Internet Connection!");
        }

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
       // ViewInPagerAdapter adapter = new ViewInPagerAdapter(context, jsonArray);
        //mViewPager.setAdapter(adapter);

        mViewPagerIndicator = (ViewPagerIndicator) rootView.findViewById(R.id.indicator);
       // mViewPagerIndicator.setCount(adapter.getCount());

//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                mViewPagerIndicator.setCurrentPosition(position);
//            }
//        });

         gridView = (GridView) rootView.findViewById(R.id.grid);
       // gridView.setAdapter(new GridViewAdapter(rootView.getContext()));
        // gridView.setNumColumns(imageIDs.length);
        // Toast.makeText(rootView.getContext(), "Test", Toast.LENGTH_SHORT).show();
       // createHttpRequest();

        loading = (LinearLayout)rootView.findViewById(R.id.loading);
        loading_grid = (LinearLayout)rootView.findViewById(R.id.loading_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(context,DetailsActivity.class));
                //  getActivity().overridePendingTransition(0,0);
                String urlVideo = null;
                try {
                    urlVideo = jsonArray_grid.getJSONObject(position).getString("pid");
                    //Toast.makeText(context, urlVideo, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               Intent intent = new Intent(context, DetailsActivity.class);
               intent.putExtra(DetailsActivity.DEATIL_ID, urlVideo);
               intent.putExtra(DetailsActivity.DETAILS_URL_INDEX, Api.populer_2);
               getActivity().startActivity(intent);
               getActivity().overridePendingTransition(0, 0);
            }
        });

        return rootView;
    }

//    private void createHttpRequest() {
//        // Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
//        RequestQueue queue = Volley.newRequestQueue(context);
//        String url = "http://encomapps.com/eflix/sliders.php";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                // Display the first 500 characters of the response string.
//                //txtView.setText("Response is: " + response.substring(0, 500));
//                // Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // txtView.setText("That didn't work!");
//                    }
//                });
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }

    private void makeHttpRequest() {

      RequestQueue  mVolleyQueue = Volley.newRequestQueue(context);
        String url = Api.just_added;

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                jsonArray = response;
                //Toast.makeText(context, jsonArray.toString(), Toast.LENGTH_SHORT).show();
                 Utils.LoadingControl(false, loading);

                ViewInPagerAdapter adapter = new ViewInPagerAdapter(context, jsonArray);
                mViewPager.setAdapter(adapter);
                mViewPagerIndicator.setCount(adapter.getCount()-1);

                mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                       // Toast.makeText(context, jsonArray.length()+"=== "+position, Toast.LENGTH_SHORT).show();
                        if(jsonArray.length()== position) {
                            mViewPager.setCurrentItem(0);
                            mViewPagerIndicator.setCurrentPosition(0);
//                        }else if(position==0){
//                            mViewPager.setCurrentItem(jsonArray.length());
//                            mViewPagerIndicator.setCurrentPosition(jsonArray.length());
                        }else {
                            mViewPagerIndicator.setCurrentPosition(position);
                        }
                    }
                });

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
    private void HttpRequestBottom() {
        RequestQueue mVolleyQueue = Volley.newRequestQueue(context);
        String url = Api.popular_main;


        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                jsonArray_grid = response;

                    Utils.LoadingControl(false, loading_grid);

                // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                gridView.setAdapter(new ShowAdapter(context, jsonArray_grid));

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
