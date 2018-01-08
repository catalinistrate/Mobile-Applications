package com.example.otto.forum;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.view.Window;
import android.widget.TextView;

import com.example.otto.forum.Authentication.DashBoard;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GraphDialog extends Dialog {
    private final Activity context;
    private GraphView graph;
    private TextView text;

    public GraphDialog(Activity a)
    {
        super(a);
        context = a;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.graph_view);
        this.setCanceledOnTouchOutside(true);
        text = (TextView) findViewById(R.id.txt_dia);
        text.setText("Last week activity on " + ((DashBoard) context).getSelectedTopic());
        long millis = System.currentTimeMillis();
        HashMap<Integer,Integer> freq = new HashMap<>();
        for (Item i:((DashBoard) context).getMessages().get(((DashBoard) context).getSelectedTopic())) {
            if(millis-i.timestamp<604800000)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(i.timestamp);
                int day = cal.get(Calendar.DAY_OF_WEEK);
                if(freq.containsKey(day))
                {
                    freq.put(day, freq.get(day)+1);
                }
                else
                    freq.put(day, 1);
            }
        }

        graph = (GraphView) findViewById(R.id.graph);
        graph.setBackgroundColor(Color.WHITE);
        LinkedList<DataPoint> data = new LinkedList<DataPoint>();
        int max = 0;
        for (Map.Entry<Integer,Integer> o:freq.entrySet()) {
            data.add(new DataPoint(o.getKey(),o.getValue()));
            if(o.getValue()>max)
                max = o.getValue();
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(data.toArray(new DataPoint[data.size()]));
        series.setDataWidth(0.5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(max);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(7);
        graph.addSeries(series);
    }
}
