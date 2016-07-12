package it.giuggi.iotremote.ifttt.structure;

import android.content.res.Resources;
import android.support.v4.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.iot.IOperatingMode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IFTTTEvent extends IFTTTComponent
{
    public static final String TYPE = "EVENT";

    public IFTTTEvent()
    {

    }

    @Override
    public int getColorId()
    {
        return R.color.colorEvent;
    }

    @Override
    protected String getType()
    {
        return TYPE;
    }

    /**
     *          "event": {
     *              "type": "VALUE_CHANGED",            //One of the event types
     *              "mode": {
     *                  "name": "gpio_read_mode",      //Mode that made this event happen (copy of the mode)
     *                  "status": 1
     *               },
     *              "params": ["status"],           //Parameters that made this event happen
     *              "oldvalues": [0],                   //Old values of said parameters
     *              "newvalues": [1],                   //New values of said parameters
     *          }
     */
    public static class Event
    {
        public IFTTTEventType type;
        public String mode_name;
        public String[] parameters;
        private String[] oldvalues;
        private String[] newvalues;

        public Event(JSONObject obj) throws JSONException
        {
            String type = obj.getString(IOperatingMode.Parameters.TYPE);
            this.type = typeToEvent(type);
            JSONObject target_mode = obj.getJSONObject(IOperatingMode.Parameters.MODE);
            this.mode_name = target_mode.getString(IOperatingMode.Parameters.NAME);

            JSONArray params = obj.getJSONArray(IOperatingMode.Parameters.PARAMS);
            this.parameters = new String[params.length()];
            for(int i = 0; i < params.length(); i++)
            {
                this.parameters[i] = params.getString(i);
            }

            JSONArray oldvalues = obj.getJSONArray(IOperatingMode.Parameters.OLD_VALUES);
            JSONArray newvalues = obj.getJSONArray(IOperatingMode.Parameters.NEW_VALUES);

            this.oldvalues = new String[oldvalues.length()];
            this.newvalues = new String[newvalues.length()];

            //Should be fine, newvalues and oldvalues MUST have the same length
            for(int i = 0; i < oldvalues.length(); i++)
            {
                this.oldvalues[i] = oldvalues.getString(i);
                this.newvalues[i] = newvalues.getString(i);
            }
        }

        public static IFTTTEventType typeToEvent(String type)
        {
            IFTTTEventType toReturn = IFTTTEventType.UNKNOWN;
            try
            {
                toReturn = IFTTTEventType.valueOf(type.toUpperCase());
            }
            catch(IllegalArgumentException ex)
            {
                ex.printStackTrace();
            }

            return toReturn;
        }

        public String[] getOldValues()
        {
            return this.oldvalues;
        }

        public String[] getNewValues()
        {
            return this.newvalues;
        }
    }

    public enum IFTTTEventType {
        ANY,
        UNKNOWN,
        DISCONNECTED,
        CONNECTED,
        VALUE_CHANGED }

    public static ArrayList<Pair<String, String>> getListOfEvents(Resources resources)
    {
        String[] events = resources.getStringArray(R.array.events_localized);
        ArrayList<Pair<String, String>> list = new ArrayList<>(5);
        IFTTTEventType[] values = IFTTTEventType.values();

        for(int i = 0; i < events.length; i++)
        {
            list.add(new Pair<>(values[i].name(), events[i]));
        }
        return list;
    }

    /**
     * Applies this IFTTTEvent to given event string
     * @param event String containing a description of the event
     * @return true if the event matches, false otherwise
     */
    public abstract boolean apply(Event event);

}
