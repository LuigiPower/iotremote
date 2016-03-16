package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class GPIOMode extends IOperatingMode
{
    public static final String NAME = "gpio_mode";

    private int gpio = 0;

    public GPIOMode()
    {
        super();
    }

    public GPIOMode(JSONObject params)
    {
        super(params);

        try
        {
            gpio = params.getInt("gpio");
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
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        return inflater.inflate(R.layout.gpio_mode, container, false);
    }
}
