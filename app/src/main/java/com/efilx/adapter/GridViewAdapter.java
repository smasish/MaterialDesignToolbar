package com.efilx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.efilx.activity.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABC on 4/13/2015.
 */
public class GridViewAdapter  extends BaseAdapter{

    Context mContext;
    private List<Item> items = new ArrayList<Item>();
    private LayoutInflater inflater;


    public GridViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items.add(new Item("Image 1", R.drawable.nature1));
        items.add(new Item("Image 2", R.drawable.nature2));
        items.add(new Item("Image 3", R.drawable.tree1));
        items.add(new Item("Image 4", R.drawable.nature3));
        items.add(new Item("Image 5", R.drawable.tree2));
        items.add(new Item("Image 1", R.drawable.nature1));
        items.add(new Item("Image 2", R.drawable.nature2));
        items.add(new Item("Image 3", R.drawable.tree1));
        items.add(new Item("Image 4", R.drawable.nature3));
        items.add(new Item("Image 4", R.drawable.nature3));
        items.add(new Item("Image 5", R.drawable.tree2));
        items.add(new Item("Image 5", R.drawable.nature1));

        mContext = context;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i)
    {
        return items.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return items.get(i).drawableId;
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



            picture = (ImageView)v.getTag(R.id.picture);
            name = (TextView)v.getTag(R.id.text);

             Item item = (Item)getItem(position);

             picture.setImageResource(item.drawableId);

            //name.setText(item.name);
            name.setText("Show  "+position);

        return v;
    }

    private class Item
    {
        final String name;
        final int drawableId;

        Item(String name, int drawableId)
        {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}