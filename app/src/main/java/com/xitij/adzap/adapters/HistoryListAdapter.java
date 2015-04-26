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
import com.xitij.adzap.model.History;
import com.xitij.adzap.model.HistoryItem;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.widget.CircleTransform;

public class HistoryListAdapter extends BaseAdapter {

    LayoutInflater layoutInflator;
    private Context ctx;
    private History histItem;


    public HistoryListAdapter(Context ctx,History items) {
        this.histItem = items;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return histItem.Transcation.size();
    }

    @Override
    public Object getItem(int position) {
        return histItem.Transcation.get(position);
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


        TextView txtAdname = (TextView)view.findViewById(R.id.txtAdname);
        TextView txtCoins = (TextView)view.findViewById(R.id.txtCoins);
        TextView txtDate = (TextView)view.findViewById(R.id.txtDate);

        if(histItem.Transcation.get(position).DisplayName ==null || histItem.Transcation.get(position).DisplayName.equalsIgnoreCase("")||histItem.Transcation.get(position).DisplayName.toString().length()==0 ) {
            txtAdname.setText("Earned from Referral Code");
        }else{
            txtAdname.setText(histItem.Transcation.get(position).DisplayName);
        }

        double coin = Double.valueOf(histItem.Transcation.get(position).CreaditedBal);
        int c = (int) coin;
        txtCoins.setText(""+c+" Coins");
        txtDate.setText(histItem.Transcation.get(position).TransDate);

        return view;
    }
}
