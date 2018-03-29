package com.example.mushu.wifimanager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        //Log.i("inadapter",wifinames.get(position).toString());

        if(wifinames.get(position).contains("Excellent")) wifiProvider.setBackgroundColor(Color.rgb(35, 155, 86  ));
        else if(wifinames.get(position).contains("Good")) wifiProvider.setBackgroundColor(Color.rgb(212, 172, 13 ));
        else if(wifinames.get(position).contains("Fair")) wifiProvider.setBackgroundColor(Color.rgb(186, 74, 0 ));
        else if(wifinames.get(position).contains("Poor")) wifiProvider.setBackgroundColor(Color.rgb(169, 50, 38  ));

        wifiProvider.setText(wifinames.get(position));
        return convertView;
    }
}
