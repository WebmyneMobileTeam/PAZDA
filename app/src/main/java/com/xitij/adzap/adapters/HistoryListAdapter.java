package com.xitij.adzap.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xitij.adzap.R;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.widget.CircleTransform;

public class HistoryListAdapter extends BaseAdapter {

    LayoutInflater layoutInflator;
    private Context ctx;
    private Offers offerItems;


    public HistoryListAdapter(Context ctx) {
        //this.offerItems = items;
        this.ctx = ctx;
      //  this.offerItems = offerObj;

    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        view = layoutInflator.inflate(R.layout.history_item_view, parent, false);

/*
        TextView txtAdName = (TextView)view.findViewById(R.id.txtAdName);
        TextView txtCoins = (TextView)view.findViewById(R.id.txtCoins);
        ImageView imgIcon = (ImageView)view.findViewById(R.id.imgIcon);*/




        return view;
    }
}
