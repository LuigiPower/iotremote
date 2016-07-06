package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

/**
 * Created by Federico Giuggioloni on 05/07/16.
 * GPIOReadMode does the same stuff as GPIOMode, but disables
 * interaction with the GPIO in question
 */
public class GPIOReadMode extends GPIOMode
{
    public static final String NAME = "gpio_read_mode";

    public GPIOReadMode()
    {
        super();
    }

    public GPIOReadMode(JSONObject params)
    {
        super(params);
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        View v = super.loadDashboardLayout(inflater, container);
        onStatusChange = null;
        gpioStatus.setOnCheckedChangeListener(null);
        gpioStatus.setEnabled(false);
        return v;
    }
}
