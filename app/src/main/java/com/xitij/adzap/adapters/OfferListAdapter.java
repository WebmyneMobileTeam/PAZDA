package com.xitij.adzap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xitij.adzap.R;
import com.xitij.adzap.model.Offers;

import java.util.ArrayList;

public class OfferListAdapter extends BaseAdapter {

    LayoutInflater layoutInflator;
    private Context ctx;
    private Offers offerItems;


    public OfferListAdapter(Context ctx,Offers offerObj) {
        //this.offerItems = items;
        this.ctx = ctx;
        this.offerItems = offerObj;

    }

    @Override
    public int getCount() {
        return offerItems.ViewAdz.size();
    }

    @Override
    public Object getItem(int position) {
        return offerItems.ViewAdz.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        view = layoutInflator.inflate(R.layout.offer_item_view, parent, false);

        /*ImageView offerIcon = (ImageView)view.findViewById(R.id.offerIcon);
        TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
*/
        TextView txtAdName = (TextView)view.findViewById(R.id.txtAdName);
        TextView txtCoins = (TextView)view.findViewById(R.id.txtCoins);

        txtAdName.setText(""+offerItems.ViewAdz.get(position).DisplayName);
        txtCoins.setText("+ "+offerItems.ViewAdz.get(position).Coins);


    //    txtTitle.setText(tempitems[position]);
      /*  TextView txt_sub_Title = (TextView)view.findViewById(R.id.txt_sub_Title);
        TextView txtCounter = (TextView)view.findViewById(R.id.txtCounter);*/


        return view;
    }
}
