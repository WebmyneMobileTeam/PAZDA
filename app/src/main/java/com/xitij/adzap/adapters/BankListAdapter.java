package com.xitij.adzap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xitij.adzap.R;
import com.xitij.adzap.model.BankList;
import com.xitij.adzap.model.History;

public class BankListAdapter extends BaseAdapter {

    LayoutInflater layoutInflator;
    private Context ctx;
    private BankList BANKItem;


    public BankListAdapter(Context ctx, BankList items) {
        this.BANKItem = items;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return BANKItem.Bank.size();
    }

    @Override
    public Object getItem(int position) {
        return BANKItem.Bank.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        view = layoutInflator.inflate(R.layout.banklist_item_view, parent, false);


        TextView txtPersonName = (TextView)view.findViewById(R.id.txtPersonName);
        TextView txtBankName = (TextView)view.findViewById(R.id.txtBankName);
        TextView txtBranch = (TextView)view.findViewById(R.id.txtBranch);

        TextView txtAccNo = (TextView)view.findViewById(R.id.txtAccNo);
        TextView txtACCType = (TextView)view.findViewById(R.id.txtACCType);
        TextView txtIFSC = (TextView)view.findViewById(R.id.txtIFSC);

        TextView txtAdd = (TextView)view.findViewById(R.id.txtAdd);



        txtPersonName.setText(BANKItem.Bank.get(position).AccountPersonName.toString().trim());
        txtBankName.setText(BANKItem.Bank.get(position).BankName.toString().trim());
        txtBranch.setText(BANKItem.Bank.get(position).BankBranch.toString().trim());
        txtAccNo.setText(BANKItem.Bank.get(position).ACNO.toString().trim());
        txtACCType.setText(BANKItem.Bank.get(position).AccountType.toString().trim());
        txtIFSC.setText(BANKItem.Bank.get(position).IFSCNo.toString().trim());
        txtAdd.setText(BANKItem.Bank.get(position).Address.toString().trim());




        return view;
    }
}
