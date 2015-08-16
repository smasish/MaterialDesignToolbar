package com.efilx.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.efilx.adapter.ShowAdapter;
import com.efilx.utils.Api;
import com.efilx.utils.InternetConnection;
import com.efilx.utils.Utils;
import com.efilx.view.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends ActionBarActivity {

    Context context;
    ArrayAdapter<String> adapter;
    String[] nevg_array;
    JSONArray jsonArray;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView mDrawerList;
    ViewPager pager;
    private String titles[] = new String[]{"Just Added", "Popular", "TV Show", "Promos", "Music Video"};
//    String[] values = new String[]{
//            "TV Show", "Action & Adventure", "Arime", "Children & Family Movie", "Comedies", "Documentries",
//            "Dream", "Foregin Movie", "Horror Movies","Independant Moview", "Romantic Movies","Sci-Fi & Fantasy", "Thillers"
//    };
    private Toolbar toolbar;

    SlidingTabLayout slidingTabLayout;
    static InternetConnection internetConnection;
    static Boolean isConected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

        Log.d("-----","------mainactivity page");

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(context, TVShowActivity.class );
                    intent.putExtra(TVShowActivity.DETAILS_URL_POSITION, jsonArray.getJSONObject(position).getString("gen_id").toString()+"" );
                    intent.putExtra(TVShowActivity.CATEGORIES_NAME, jsonArray.getJSONObject(position).getString("gen_name").toString() );
                    startActivity(intent);
                    overridePendingTransition(0,0);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        internetConnection = new InternetConnection(context);
        isConected = internetConnection.isConnectingToInternet();
        if(isConected){
            makeHttpRequest();
        }else{
            Utils.AlertDialog(MainActivity.this);
            return;
        }

    }
    private void makeHttpRequest() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = Api.nevigation_side;

// Request a string response from the provided URL.
        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                jsonArray = response;
                nevg_array  = new String[response.length()];
                for(int i = 0;i<response.length();i++){
                    try {
                        nevg_array[i] = response.getJSONObject(i).getString("gen_name").toString();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, android.R.id.text1, nevg_array);
                mDrawerList.setAdapter(adapter);
                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
               // gen_id: "2",
                //gen_name: "Dramma"


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //sToast(error.toString());
                //Log.e("Exception",error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(jReq);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//       int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.settings) {
//            return true;
//        }

        switch (item.getItemId()) {
            case R.id.account:
                startActivity(new Intent(context,AccountActivity.class));
                 overridePendingTransition(0,0);
            break;
            case R.id.help:
                startActivity(new Intent(context,FaqActivity.class));
               overridePendingTransition(0,0);
                break;
            case R.id.settings:

                break;
            case R.id.feedback:
                startActivity(new Intent(context,FeedBackActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.about:
                startActivity(new Intent(context,AboutActivity.class));
                overridePendingTransition(0,0);
                break;
            default:
               return false;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
