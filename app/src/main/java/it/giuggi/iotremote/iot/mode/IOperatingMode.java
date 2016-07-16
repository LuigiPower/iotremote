package it.giuggi.iotremote.iot.mode;

import android.content.res.Resources;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.iot.node.IOTNode;

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
    public class Parameters
    {
        /**
         * Generic parameters
         */
        public static final String STATUS = "status";
        /**********************************************************/

        /**
         * GPIO Modes parameters
         */
        public static final String GPIO = "gpio";
        /**********************************************************/

        /**
         * Sensor Modes parameters
         */
        protected static final String ID = "id";
        protected static final String CURRENT_VALUE = "current_value";
        protected static final String VALUE_HISTORY = "value_history";
        protected static final String TIME_MILLIS = "time_millis";
        protected static final String SENSOR_DESCRIPTION = "sensor_description";
        protected static final String UNIT_TYPE = "unit_type";
        protected static final String UNIT_SYMBOL = "unit_symbol";
        /**********************************************************/

        /**
         * Value Update Parameters
         */
        public static final String NODE = "node";
        public static final String MODE = "mode";
        public static final String IP = "ip";
        public static final String NAME = "name";
        public static final String EVENT = "event";
        public static final String NEW_VALUES = "newvalues";
        public static final String OLD_VALUES = "oldvalues";
        public static final String MODE_NAME = "mode_name";
        public static final String TYPE = "type";
        public static final String PARAMS = "params";
        /**********************************************************/
    }

    public static final String NAME = "operating_mode";
    public static final String DASHBOARD_VIEW = "DASHBOARD_VIEW";

    transient protected IOTNode owner;

    transient private JSONObject parameters;

    /**
     * Gets a list of (parameter_value, parameter_name) to be used in any adapter (for example, spinners)
     * @param resources resources from which to load the localized strings
     * @return ArrayList of Pair(String, String), containing (parameter_value, parameter_name) pairs
     */
    public static ArrayList<Pair<String, String>> getParameterList(Resources resources)
    {
        ArrayList<Pair<String, String>> list = new ArrayList<>(10);
        list.add(new Pair<>(Parameters.STATUS, resources.getString(R.string.parameter_status)));
        list.add(new Pair<>(Parameters.GPIO, resources.getString(R.string.parameter_gpio)));
        list.add(new Pair<>(Parameters.ID, resources.getString(R.string.parameter_id)));
        list.add(new Pair<>(Parameters.VALUE_HISTORY, resources.getString(R.string.parameter_current_value)));  //TODO change this or the other
        //list.add(new Pair<>(Parameters.CURRENT_VALUE, resources.getString(R.string.parameter_current_value))); //TODO change this or the other
        list.add(new Pair<>(Parameters.TIME_MILLIS, resources.getString(R.string.parameter_time_millis)));
        return list;
    }

    /**
     * Gets a list of (mode_value, mode_name) to be used in any adapter (for example, spinners)
     * @param resources resources from which to load the localized strings
     * @return ArrayList of Pair(String, String), containing (mode_value, mode_name) pairs
     */
    public static ArrayList<Pair<String, String>> getModeValueMatching(Resources resources)
    {
        ArrayList<Pair<String, String>> list = new ArrayList<>(10);
        list.add(new Pair<>(BasicMode.NAME, resources.getString(BasicMode.LOCALIZED_STRING)));
        list.add(new Pair<>(EmptyMode.NAME, resources.getString(EmptyMode.LOCALIZED_STRING)));
        list.add(new Pair<>(GPIOReadMode.NAME, resources.getString(GPIOReadMode.LOCALIZED_STRING)));
        list.add(new Pair<>(GPIOMode.NAME, resources.getString(GPIOMode.LOCALIZED_STRING)));
        list.add(new Pair<>(SensorMode.NAME, resources.getString(SensorMode.LOCALIZED_STRING)));
        list.add(new Pair<>(UnknownMode.NAME, resources.getString(UnknownMode.LOCALIZED_STRING)));
        return list;
    }

    /**
     * Standard constructor without params
     */
    public IOperatingMode()
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

    public JSONObject getParameters()
    {
        return parameters;
    }

    /**
     * Sets the Mode's owner
     * @param node IOTNode that owns this mode instance
     */
    public void setOwner(IOTNode node)
    {
        this.owner = node;
    }

    public boolean has(String modename)
    {
        return getName().equalsIgnoreCase(modename);
    }

    public abstract String getName();

    public abstract int getLocalizedNameId();

    public View loadPreview(LayoutInflater inflater, ViewGroup container)
    {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.riga_iot_mode, container, false);
        Toolbar modeToolbar = (Toolbar) v.findViewById(R.id.mode_toolbar);

        modeToolbar.setTitle(getLocalizedNameId());
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
        modeToolbar.setTitle(getLocalizedNameId());
        modeToolbar.setBackgroundResource(getColorId());
        View toAdd = loadDashboardLayout(inflater, modeDetails);
        toAdd.setTag(DASHBOARD_VIEW);
        modeDetails.addView(toAdd);
        return v;
    }

    public static Pair<String, String> getModeName(Resources resources)
    {
        return new Pair<>("none", "none");
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
