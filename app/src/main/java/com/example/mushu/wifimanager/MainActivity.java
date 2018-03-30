package com.example.mushu.wifimanager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    WifiManager wm;
    WifiReceiver wr;
    ToggleButton toggle;
    Button graph,conn;
    List<ScanResult> wl;
    ArrayList<String> listOfProvider,x,y;
    List_Adapter adapter;
    ListView lp;

    //toggle wifi and check permission
    public void wifitoggle(WifiManager wm, TextView toggle)
    {
        if(wm.isWifiEnabled() == true)
        {
            wm.setWifiEnabled(false);
            toggle.setText("OFF");
            toggle.setBackgroundColor(Color.rgb(241, 148, 138));
            if(!listOfProvider.isEmpty()) listOfProvider.clear();
            if(!x.isEmpty()) x.clear();
            if(!y.isEmpty()) y.clear();
        }
        else
        {
            wm.setWifiEnabled(true);
            toggle.setText("ON");
            toggle.setBackgroundColor(Color.rgb(130, 224, 170));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Give Location permission to this application",Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
            else{
                scanning();
            }
        }
    }

    //start scanning once permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanning();
        }
    }
    //start scan
    public void scanning()
    {
            wr = new WifiReceiver();
            registerReceiver(wr, new IntentFilter(wm.SCAN_RESULTS_AVAILABLE_ACTION));
            wm.startScan();
    }
    //display keywords based on DBm strength
    public String returnlevel(int DBm)
    {
        if(DBm >= -50) return DBm+" - Excellent";
        else if (DBm < -50 && DBm >= -60) return DBm+" - Good";
        else if (DBm < -60 && DBm >= -70) return DBm+" - Fair";
        else return DBm+" - Poor";
    }
    // comparator for sort
    public static Comparator<ScanResult> sortcomp = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult a, ScanResult b) {
            return (a.level > b.level ? -1 : (a.level == b.level ? 0 : 1));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.rgb(189, 195, 199));

        toggle = findViewById(R.id.wifitoggle);
        graph = findViewById(R.id.viewGraph);
        conn = findViewById(R.id.conn);
        wm = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        listOfProvider = new ArrayList<>();
        x = new ArrayList<>();
        y = new ArrayList<>();
        lp = findViewById(R.id.list_view_wifi);
        wifitoggle(wm,toggle);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifitoggle(wm,toggle);
            }
        });
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,GraphActivity.class);
                i.putStringArrayListExtra("x",x);
                i.putStringArrayListExtra("y",y);
                Log.i("click","click");
                startActivity(i);
            }
        });

    }

    class WifiReceiver extends BroadcastReceiver{

        public void onReceive(Context c, Intent intent) {
            //Log.i("in","onReceive");

            //get the results
            wl = wm.getScanResults();

            WifiInfo info = wm.getConnectionInfo();
            if(info.getSSID() != null) conn.setText(info.getSSID());
            final String ip = Integer.toString(info.getIpAddress());
            final String ls = Integer.toString(info.getLinkSpeed());
            conn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"IP Address: "+ip+"\n Link Speed: "+ls,Toast.LENGTH_SHORT).show();
                }
            });

            // sort on the basis of DBm strength in level attribute
            Collections.sort(wl, sortcomp);

            // refresh list
            if(!listOfProvider.isEmpty()) listOfProvider.clear();
            if(!x.isEmpty()) x.clear();
            if(!y.isEmpty()) y.clear();

            String providerName;
            //Log.i("Size",Integer.toString(wl.size()));

            // print details
            for (int i = 0; i < wl.size(); i++) {
                providerName = "Name: "+(wl.get(i).SSID)+"\nBSSID: "+(wl.get(i).BSSID)+"\n Frequency: "+(wl.get(i).frequency)+"\n Level: "+returnlevel(wl.get(i).level);
                listOfProvider.add(providerName);
                x.add((wl.get(i).SSID));
                y.add(Integer.toString(wl.get(i).level));
                Log.i("pn",providerName);
            }
            //Log.i("in","adpaterset");
            // add the list to adapter
            adapter = new List_Adapter(MainActivity.this, listOfProvider);
            //set adapter to list view
            lp.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
    }
}
