package it.giuggi.iotremote.iot;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import org.json.JSONArray;
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

    private String id;
    private ArrayList<Entry> entries;
    private ArrayList<Long> times;
    private LineChart chart;
    private LineData lineData;
    private boolean chartInitialized = false;

    public SensorMode(JSONObject parameters)
    {
        entries = new ArrayList<>(10);
        times = new ArrayList<>(10);

        try
        {
            valueUpdate(parameters);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

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
        return v;
    }

    @Override
    public void destroyDashboardLayout(ViewGroup container)
    {

    }

    private void loadChart(final LineChart chart)
    {
        chart.clear();
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

        xAxis.setValueFormatter(new AxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                long millis = times.get((int) value);
                return DateFormat.getTimeFormat(chart.getContext()).format(millis);
            }

            @Override
            public int getDecimalDigits()
            {
                return 0;
            }
        });


        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    /**
     * Value update here should provide the latest value for this node
     * @param newParameters
     * @throws JSONException
     */
    @Override
    public void valueUpdate(JSONObject newParameters) throws JSONException
    {
        JSONArray array;
        try
        {
            array = newParameters.getJSONArray(Parameters.VALUE_HISTORY);
            id = newParameters.getString(Parameters.ID);
        }
        catch(JSONException e)
        {
            JSONObject node = newParameters.getJSONObject(Parameters.NODE);
            JSONObject mode = node.getJSONObject(Parameters.MODE);
            JSONObject params = mode.getJSONObject(Parameters.PARAMS);
            JSONObject object = newParameters.getJSONObject(Parameters.EVENT);
            JSONArray arr = object.getJSONArray(Parameters.NEW_VALUES);
            String id = params.getString(Parameters.ID); //TODO need the full mode inside the event, to avoid navigating a compositemode tree

            String node_name = object.getString(Parameters.MODE_NAME);
            String mode_name = mode.getString(Parameters.NAME);

            Log.i("SensorMode", "SensorMode Checking incoming data: " + mode_name + " " + node_name + " " + this.owner.name + " and " + id + " " + this.id);
            if(!node_name.equalsIgnoreCase(this.owner.name) || !id.equalsIgnoreCase(this.id))
            {
                return; //This data is not for me; throw it away
            }

            array = arr.getJSONArray(0);
        }

        entries.clear();
        times.clear();
        for(int i = 0; i < array.length(); i++) // expect the values to be in order
        {
            float value = (float) array.getDouble(i);
            //long millis = obj.getLong("timemillis");
            Entry data = new Entry(i, value);
            entries.add(data);
            times.add(System.currentTimeMillis());
        }

        if(chart != null)
        {
            loadChart(chart);
        }

        /*
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
        */
    }
}
