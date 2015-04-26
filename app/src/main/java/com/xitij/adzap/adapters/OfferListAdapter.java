package com.xitij.adzap.adapters;

import android.content.Context;
import android.graphics.Color;
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
        ImageView imgIcon = (ImageView)view.findViewById(R.id.imgIcon);


        Log.e("Image Path", "" + offerItems.ViewAdz.get(position).Icon);
        String tempPath = offerItems.ViewAdz.get(position).Icon.toString();
        String subPath = tempPath.substring(tempPath.lastIndexOf("/")+1,tempPath.length());
        Log.e("Sub Path",subPath);


        Glide.with(ctx).load(AppConstants.BASE_URL_IMAGE + subPath).transform(new CircleTransform(ctx)).thumbnail(0.1f).into(imgIcon);

   /*     txtAdName.setText(""+offerItems.ViewAdz.get(position).DisplayName);
        double coin = Double.valueOf(offerItems.ViewAdz.get(position).Coins);
        int c = (int) coin;*/

        txtCoins.setText("+ "+String.valueOf(offerItems.ViewAdz.get(position).Coins));


        return view;
    }
}
