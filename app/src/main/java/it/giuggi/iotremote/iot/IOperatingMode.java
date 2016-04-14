package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * IOperatingMode
 * Abstraction for any IoTNode operating mode
 */
public abstract class IOperatingMode
{
    public static final String NAME = "operating_mode";
    private JSONObject parameters;

    /**
     * Standard constructor without params
     */
    protected IOperatingMode()
    {
        this.parameters = new JSONObject();
    }

    /**
     * Constructor with params
     * @param parameters JSONObject with init params
     */
    protected IOperatingMode(JSONObject parameters)
    {
        this.parameters = parameters;
    }

    /**
     * Get specified parameter of this mode
     * @param parameter parameter name as string
     * @return an Object containing the parameter asked for (mostly String or JSONObject/Array)
     * @throws JSONException if the parameter doesn't exist
     */
    public Object getParameter(String parameter) throws JSONException
    {
        return this.parameters.get(parameter);
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
