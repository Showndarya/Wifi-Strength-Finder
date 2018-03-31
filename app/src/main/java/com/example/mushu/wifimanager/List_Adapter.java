package com.example.mushu.wifimanager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mushu on 29/3/18.
 */

public class List_Adapter extends ArrayAdapter<String> {

    Activity act;
    List<String> wifinames;
    private LayoutInflater inflater;

    public List_Adapter(Activity a, List<String> wifiName) {
        super(a, R.layout.list_style, wifiName);
        act = a;
        inflater = LayoutInflater.from(act);
        this.wifinames = wifiName;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_style, parent, false);
        TextView wifiProvider = convertView.findViewById(R.id.wifiname);
        ImageView img = convertView.findViewById(R.id.img);
        //Log.i("inadapter",wifinames.get(position).toString());

        Integer[] image = {R.drawable.w4,R.drawable.w3, R.drawable.w2,R.drawable.w1};

        if(wifinames.get(position).contains("Excellent")) img.setImageResource(image[0]);
        else if(wifinames.get(position).contains("Good")) img.setImageResource(image[1]);
        else if(wifinames.get(position).contains("Fair")) img.setImageResource(image[2]);
        else if(wifinames.get(position).contains("Poor")) img.setImageResource(image[3]);

        wifiProvider.setText(wifinames.get(position));
        return convertView;
    }
}
