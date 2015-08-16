package com.efilx.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.efilx.activity.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ABC on 4/9/2015.
 */
public class ShowAdapter extends BaseAdapter{

    Context mContext;

    private LayoutInflater inflater;
    JSONArray jsonArray;
    String urlImage, urlVideo;

    public ShowAdapter(Context context , JSONArray jsonArray) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int i)
    {
        return i;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if(v == null)
        {
            v = inflater.inflate(R.layout.gridview_item, viewGroup, false);

            v.setTag(R.id.picture, v.findViewById(R.id.picture));

            v.setTag(R.id.text, v.findViewById(R.id.text));
        }
        try {
            urlImage = jsonArray.getJSONObject(position).getString("thumb").toString();
           // urlVideo = jsonArray.getJSONObject(position).getString("svideo").toString();

            Log.d("---------url ",""+urlImage);
        picture = (ImageView)v.getTag(R.id.picture);

        name = (TextView)v.getTag(R.id.text);
       // Item item = (Item)getItem(position);
       // picture.setImageResource(item.drawableId);


//            picture.setLayoutParams(new GridView.LayoutParams(480/3, 480/3));
//            picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            picture.setPadding(0, 0, 0, 0);
//            picture.setAdjustViewBounds(true);


            UrlImageViewHelper.setUrlDrawable(picture, urlImage);
       // name.setText(item.name);
        name.setText(jsonArray.getJSONObject(position).getString("ptitle").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}

