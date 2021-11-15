package com.greenwich.mexpense.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.greenwich.mexpense.R;
import com.greenwich.mexpense.model.Transport;

import java.util.List;

/**
 * Package com.greenwich.mexpense.adapter in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 14/11/2021
 */
public class TransportAdapter extends BaseAdapter {

    private final Context context;
    private final List<Transport> transportList;

    public TransportAdapter(Context context, List<Transport> transportList) {
        this.context = context;
        this.transportList = transportList;
    }

    @Override
    public int getCount() {
        return transportList.size();
    }

    @Override
    public Object getItem(int i) {
        return transportList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Transport transport = transportList.get(i);
        View view;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.spinner_drop_down_list_item, parent, false);
            TextView label = (TextView) view.findViewById(R.id.name);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            //imageView.setImageDrawable(ContextCompat.getDrawable(context, transport.icon));
            imageView.setImageResource(transport.icon);
            label.setText(transport.name);
        }else{
            view = convertView;
        }
        return view;
    }
}
