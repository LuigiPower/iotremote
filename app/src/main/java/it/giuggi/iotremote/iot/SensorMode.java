package it.giuggi.iotremote.iot;

import android.graphics.Color;
import android.text.format.DateFormat;
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
        JSONArray array = null;
        try
        {
            array = newParameters.getJSONArray(Parameters.VALUE_HISTORY);
            id = newParameters.getString(Parameters.ID);
        }
        catch(JSONException e)
        {
            JSONObject node = newParameters.getJSONObject(Parameters.NODE);
            JSONObject event = newParameters.getJSONObject(Parameters.EVENT);
            JSONArray arr = event.getJSONArray(Parameters.NEW_VALUES);
            JSONArray changed_params = event.getJSONArray(Parameters.PARAMS);
            JSONObject target_mode = event.getJSONObject(Parameters.MODE);
            JSONObject params = target_mode.getJSONObject(Parameters.PARAMS);

            String node_name = node.getString(Parameters.NAME);
            String mode_name = target_mode.getString(Parameters.NAME);
            String id = params.getString(Parameters.ID);

            if(!mode_name.equalsIgnoreCase(NAME) || !node_name.equalsIgnoreCase(this.owner.name) || !id.equalsIgnoreCase(this.id))
            {
                return; //This data is not for me; throw it away
            }

            for(int i = 0; i < arr.length(); i++)
            {
                if(changed_params.getString(i).equalsIgnoreCase(Parameters.VALUE_HISTORY))
                {
                    array = arr.getJSONArray(i);
                }
            }

            if(array == null) return; //TODO check for other changes (for example descriptions...)
        }

        entries.clear();
        times.clear();
        for(int i = 0; i < array.length(); i++) // expect the values to be in order
        {
            JSONObject fullvalue = array.getJSONObject(i);
            float value = (float) fullvalue.getDouble(Parameters.CURRENT_VALUE);
            long millis = fullvalue.getLong(Parameters.TIME_MILLIS);
            Entry data = new Entry(i, value);
            entries.add(data);
            times.add(millis);
        }

        if(chart != null)
        {
            loadChart(chart);
        }
    }
}
