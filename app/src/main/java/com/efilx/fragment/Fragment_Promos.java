package com.efilx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.activity.DetailsActivity;
import com.efilx.activity.R;
import com.efilx.activity.VideoPlayerActivity;
import com.efilx.adapter.ShowAdapter;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Dinesh on 4/22/2015.
 */
public class Fragment_Promos  extends Fragment {

    Context context;
    static InternetConnection internetConnection;
    static Boolean isConected = false;
    JSONArray jsonArray;
    private static final String ARG_POSITION = "position";
    private int position;
    GridView gridView;
    LinearLayout loading;

    public static Fragment_Promos newInstance(int position) {
        Fragment_Promos f = new Fragment_Promos();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.fragment_promos, container, false);
        context = rootView.getContext();
        gridView = (GridView)rootView.findViewById(R.id.gridview);
        // gridView.setAdapter(new ShowAdapter(rootView.getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  startActivity(new Intent(context,VideoPlayerActivity.class));
                //  getActivity().overridePendingTransition(0,0);
                String urlVideo = null;
                try {
                    urlVideo = jsonArray.getJSONObject(position).getString("pid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(DetailsActivity.DEATIL_ID, urlVideo);
                intent.putExtra(DetailsActivity.DETAILS_URL_INDEX, Api.promos_3);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        internetConnection = new InternetConnection(context);
        isConected = internetConnection.isConnectingToInternet();
        if(isConected){
            makeHttpRequest();
        }else{
            Utils.AlertDialog(getActivity());
            //sToast("No Internet Connection!");
        }
        loading = (LinearLayout)rootView.findViewById(R.id.loading);

        return rootView;
    }

    private void makeHttpRequest() {
        RequestQueue mVolleyQueue = Volley.newRequestQueue(context);
        String url = Api.promos;


        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                jsonArray = response;

                Utils.LoadingControl(false, loading);

                // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
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
}
