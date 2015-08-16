package com.efilx.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efilx.activity.DetailsActivity;
import com.efilx.activity.VideoPlayerActivity;
import com.efilx.utils.Api;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.efilx.activity.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ABC on 4/13/2015.
 */
public class ViewInPagerAdapter  extends PagerAdapter {

  //  private static int[] mViewColor = {R.drawable.nature1, R.drawable.nature2, R.drawable.nature3, R.drawable.tree1, R.drawable.tree2};

    Context mContext;
    JSONArray jsonArray;
    String urlImage, urlVideo, pid;
    public ViewInPagerAdapter(Context context, JSONArray jsonArray) {

        mContext = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length()+1;
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return ((View) o).equals(v);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView tv = new ImageView(mContext);
        try {
        urlImage = jsonArray.getJSONObject(position).getString("thumb").toString();
        // urlVideo = jsonArray.getJSONObject(position).getString("trailer").toString();
        pid = jsonArray.getJSONObject(position).getString("pid").toString();

       //tv.setBackgroundResource(mViewColor[position]);
        // Toast.makeText(mContext, urlImage, Toast.LENGTH_SHORT).show();
        container.addView(tv);
        UrlImageViewHelper.setUrlDrawable(tv, urlImage);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class );
                intent.putExtra(DetailsActivity.DEATIL_ID, pid );
                intent.putExtra(DetailsActivity.DETAILS_URL_INDEX, Api.justadded_0 );
                mContext.startActivity(intent);
                //Toast.makeText(mContext, position+"", Toast.LENGTH_SHORT).show();
            }
        });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = (View) object;
        container.removeView(v);
    }
}