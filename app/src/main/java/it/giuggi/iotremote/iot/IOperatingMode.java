package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IOperatingMode
{
    public static final String NAME = "operating_mode";

    /**
     * Standard constructor without params
     */
    public IOperatingMode()
    {

    }

    /**
     * Constructor with params
     * @param params JSONObject with init params
     */
    public IOperatingMode(JSONObject params)
    {

    }

    public abstract String getName();

    public abstract View loadDashboardLayout(LayoutInflater inflater, ViewGroup container);

    public static IOperatingMode stringToMode(String name, JSONObject params)
    {
        if(name.equalsIgnoreCase(EmptyMode.NAME))
        {
            return new EmptyMode(params);
        }
        else if(name.equalsIgnoreCase(GPIOMode.NAME))
        {
            return new GPIOMode(params);
        }
        else if(name.equalsIgnoreCase(CompositeMode.NAME))
        {
            return new CompositeMode(params);
        }
        else return new UnknownMode(params);
    }
}
