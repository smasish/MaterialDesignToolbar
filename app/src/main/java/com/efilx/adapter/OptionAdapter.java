package com.efilx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.efilx.activity.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Dinesh on 4/27/2015.
 */
public class OptionAdapter extends BaseAdapter {

    Context mContext;
    private LayoutInflater inflater;
    JSONArray jsonArray;
     int option;

    public OptionAdapter(Context context, JSONArray jsonArray, int opt) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        option = opt;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView heading, details;

        if (v == null) {
            v = inflater.inflate(R.layout.list_option, viewGroup, false);
        }
        try {
            heading = (TextView) v.findViewById(R.id.heading);
            details = (TextView) v.findViewById(R.id.details);

            if(option==1){
                //Faq
                heading.setText(jsonArray.getJSONObject(position).getString("ptitle").toString());
                details.setText(jsonArray.getJSONObject(position).getString("details").toString());
            }else if(option==2){
                //About
                heading.setText(jsonArray.getJSONObject(position).getString("title").toString());
                details.setText(jsonArray.getJSONObject(position).getString("details").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
