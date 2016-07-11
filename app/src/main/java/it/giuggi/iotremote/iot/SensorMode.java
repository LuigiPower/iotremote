package it.giuggi.iotremote.iot;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.TaskHandler;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 05/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class SensorMode extends IOperatingMode
{
    public static final String NAME = "sensor_mode";
    public static final int LOCALIZED_STRING = R.string.mode_sensor;

    private static final long UPDATE_TIME = 5000;
    private boolean visible = true;
    private boolean updating = false;

    private String id;
    private ArrayList<Entry> entries;
    private LineChart chart;
    private LineData lineData;
    private boolean chartInitialized = false;

    public SensorMode(JSONObject parameters)
    {
        entries = new ArrayList<>(10);
        try
        {
            valueUpdate(parameters);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private Runnable realTimeUpdate = new Runnable()
    {
        @Override
        public void run()
        {
            if(!visible)
            {
                //Cleaning up
                return;
            }

            owner.sendCommand("sensor" + id + "/value", new WebRequestTask.OnResponseListener()
            {
                @Override
                public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
                {
                    Log.i("SensorMode", "Done sending test command, response is: " + ris);
                    if(ris == null)
                    {
                        return;
                    }

                    try
                    {
                        valueUpdate((JSONObject) ris);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public int getLocalizedNameId()
    {
        return LOCALIZED_STRING;
    }

    @Override
    public View loadDashboard(LayoutInflater inflater, ViewGroup container)
    {
        View v = super.loadDashboard(inflater, container);
        LineChart chart = (LineChart) v.findViewById(R.id.sensor_chart);
        chart.setTouchEnabled(false);
        return v;
    }

    @Override
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        View v = inflater.inflate(R.layout.sensor_mode, container, false);
        chart = (LineChart) v.findViewById(R.id.sensor_chart);
        loadChart(chart);
        visible = true;

        if(!updating)
        {
            //TaskHandler.getInstance().runPeriodically(realTimeUpdate, UPDATE_TIME);
            updating = true;
        }
        return v;
    }

    @Override
    public void destroyDashboardLayout(ViewGroup container)
    {
        visible = false;
        updating = false;
    }

    private void loadChart(LineChart chart)
    {
        LineDataSet set = new LineDataSet(entries, "Sensor values");
        lineData = new LineData(set);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(0f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        //xAxis.setAxisMinValue(entries.get(0).getX()); //TODO in base al periodo da visualizzare
        /*
        xAxis.setValueFormatter(new AxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return ""; //TODO Mettici la data corrente
            }

            @Override
            public int getDecimalDigits()
            {
                return 0;
            }
        });
        */

        chartInitialized = true;
    }

    /**
     * Value update here should provide the latest value for this node
     * @param newParameters
     * @throws JSONException
     */
    @Override
    public void valueUpdate(JSONObject newParameters) throws JSONException
    {
        /* TODO decide whether or not to use this (this should be useful for the node's "history" (data saved on a database on the server)
        JSONArray array = newParameters.getJSONArray(CURRENT_VALUE);
        for(int i = 0; i < array.length(); i++) //TODO either expect the values to be in order, or reorder the array after this
        {
            JSONObject obj = array.getJSONObject(i);
            float value = (float) obj.getDouble("value");
            long millis = obj.getLong("timemillis");
            Entry data = new Entry(millis, value);
            entries.add(data);
        }8
        */

        this.id = newParameters.getString(IOperatingMode.Parameters.ID);
        float value = (float) newParameters.getDouble(IOperatingMode.Parameters.CURRENT_VALUE);
        long millis = newParameters.getLong(IOperatingMode.Parameters.TIME_MILLIS)/1000;

        Entry data = new Entry(entries.size(), value);
        //entries.add(data);

        if(!chartInitialized && chart != null)
        {
            loadChart(chart);
        }
        else if(chart != null)
        {
            lineData.addEntry(data, 0);
            lineData.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        }
    }
}
