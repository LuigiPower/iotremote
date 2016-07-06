package it.giuggi.iotremote.iot;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * IOperatingMode
 * Abstraction for any IoTNode operating mode
 * Possible modes (for now):
 *      - EmptyMode and UnknownMode
 *      - BaseMode is the standard Mode of any Node, allows Node configuration and general setup (methods to send configuration commands to Node)
 *      - GPIOReadMode to READ on a GPIO (non clickable checkbox shows current status)
 *      - GPIOMode to READ and WRITE on a GPIO (clickable checkbox sends action to node)
 *      - SensorMode (Show a graph with the values over time, server will store those values) (TODO check physically how this could be done)
 *          - Temperature
 *          - Humidity
 *          - Light
 *          - Pressure
 */
public abstract class IOperatingMode
{
    public static final String NAME = "operating_mode";

    protected IOTNode owner;

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

    /**
     * Sets the Mode's owner
     * @param node IOTNode that owns this mode instance
     */
    protected void setOwner(IOTNode node)
    {
        this.owner = node;
    }

    public boolean has(String modename)
    {
        return getName().equalsIgnoreCase(modename);
    }

    public abstract String getName();

    public View loadPreview(LayoutInflater inflater, ViewGroup container)
    {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.riga_iot_mode, container, false);
        Toolbar modeToolbar = (Toolbar) v.findViewById(R.id.mode_toolbar);

        modeToolbar.setTitle(getName());
        modeToolbar.setBackgroundResource(getColorId());
        return v;
    }

    public View loadDashboard(LayoutInflater inflater, ViewGroup container)
    {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.riga_iot_mode, container, false);
        ViewGroup modeDetails = (ViewGroup) v.findViewById(R.id.mode_details);
        Toolbar modeToolbar = (Toolbar) v.findViewById(R.id.mode_toolbar);
        ScrollView modeScrollbar = (ScrollView) v.findViewById(R.id.mode_scroll);

        modeScrollbar.setVerticalScrollBarEnabled(false);
        modeToolbar.setTitle(getName());
        modeToolbar.setBackgroundResource(getColorId());
        View toAdd = loadDashboardLayout(inflater, modeDetails);
        modeDetails.addView(toAdd);
        return v;
    }

    public abstract View loadDashboardLayout(LayoutInflater inflater, ViewGroup container);

    public abstract void destroyDashboardLayout(ViewGroup container);

    public abstract void valueUpdate(JSONObject newParameters) throws JSONException;

    public int getColorId()
    {
        return R.color.colorPrimaryDark;
    }

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
        else if(name.equalsIgnoreCase(BasicMode.NAME))
        {
            return new BasicMode(params);
        }
        else if(name.equalsIgnoreCase(GPIOReadMode.NAME))
        {
            return new GPIOReadMode(params);
        }
        else if(name.equalsIgnoreCase(SensorMode.NAME))
        {
            return new SensorMode(params);
        }
        else return new UnknownMode(params);
    }
}
