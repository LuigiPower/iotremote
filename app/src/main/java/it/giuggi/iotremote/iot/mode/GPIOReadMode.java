package it.giuggi.iotremote.iot.mode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 05/07/16.
 * GPIOReadMode does the same stuff as GPIOMode, but disables
 * interaction with the GPIO in question
 */
public class GPIOReadMode extends GPIOMode
{
    transient public static final String NAME = "gpio_read_mode";
    transient public static final int LOCALIZED_STRING = R.string.mode_gpio_read;

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
    public int getLocalizedNameId()
    {
        return LOCALIZED_STRING;
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
