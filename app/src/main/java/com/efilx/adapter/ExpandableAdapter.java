package com.efilx.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.efilx.activity.R;
import com.efilx.activity.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dinesh on 4/21/2015.
 */
public class ExpandableAdapter  extends BaseExpandableListAdapter {

//    public ArrayList<String> groupItem, tempChild;
//    public ArrayList<Object> Childtem = new ArrayList<Object>();
    public LayoutInflater minflater;
    public Activity activity;
    JSONArray jsonArray;
//    public ExpandableAdapter(ArrayList<String> grList, ArrayList<Object> childItem) {
//        groupItem = grList;
//        this.Childtem = childItem;
//    }
    public ExpandableAdapter(Activity activity, JSONArray jsonArray) {
        this.activity = activity;
        this.jsonArray = jsonArray;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       // tempChild = (ArrayList<String>) Childtem.get(groupPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.childrow, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        try {
            text.setText(jsonArray.getJSONObject(groupPosition).getJSONArray("episode").getJSONObject(childPosition).getString("epi_title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // text.setText(tempChild.get(childPosition));
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                try {
                    String url = jsonArray.getJSONObject(groupPosition).getJSONArray("episode").getJSONObject(childPosition).getString("epi_video");
                    Intent intent = new Intent(activity, VideoPlayerActivity.class );
                    intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, url );
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0,0);
                    //Toast.makeText(activity, url, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int lenght=0;
        try {
            lenght = jsonArray.getJSONObject(groupPosition).getJSONArray("episode").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lenght;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return jsonArray.length();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.grouprow, null);
        }
        try {
            ((CheckedTextView) convertView).setText(jsonArray.getJSONObject(groupPosition).getString("season_title"));
            ((CheckedTextView) convertView).setChecked(isExpanded);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

