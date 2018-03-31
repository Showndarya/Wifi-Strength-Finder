package com.example.mushu.wifimanager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mushu on 30/3/18.
 */

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Log.i("in","Graph");
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.rgb(255,255,255));

        ArrayList<String> x = getIntent().getStringArrayListExtra("x");
        ArrayList<String> y = getIntent().getStringArrayListExtra("y");

        GraphView gv  = findViewById(R.id.graph);
        gv.setBackgroundColor(Color.rgb(255,255,255));

        gv.removeAllSeries();


        DataPoint[] values = new DataPoint[x.size()];
        for(int i=0;i<x.size();i++) {

            DataPoint v = new DataPoint((double) i, Double.parseDouble(y.get(i)));
            values[i] = v;
        }

        BarGraphSeries<DataPoint> bar = new BarGraphSeries<>(values);
        gv.addSeries(bar);
        bar.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(118, 215, 196);
            }
        });

        bar.setSpacing(20);



        StaticLabelsFormatter lab = new StaticLabelsFormatter(gv);
        //String []ylab = y.toArray(new String[y.size()]);
        //lab.setVerticalLabels(ylab);
        String []xlab = x.toArray(new String[x.size()]);
        lab.setHorizontalLabels(xlab);
        gv.getGridLabelRenderer().setLabelFormatter(lab);
        gv.getGridLabelRenderer().setGridColor(Color.rgb(0,0,0));
    }

}