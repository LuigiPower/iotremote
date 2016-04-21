package it.giuggi.iotremote.ifttt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IFTTTEvent
{
    /**
     *          "event": {
     *              "type": "VALUE_CHANGED",            //One of the event types
     *              "mode_name": "gpio_read_mode",      //Mode that made this event happen TODO consider changing this into a copy of said mode taken from the node part
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
        private JSONArray oldvalues;
        private JSONArray newvalues;

        public String toLogString()
        {
            return toString() + " Type: " + this.type + " mode: " + this.mode_name + " parameters: " + this.parameters + " oldvalues: " + this.oldvalues + " newvalues: " + this.newvalues;
        }

        public Event(JSONObject obj) throws JSONException
        {
            String type = obj.getString("type");
            this.type = typeToEvent(type);
            this.mode_name = obj.getString("mode_name");

            JSONArray params = obj.getJSONArray("params");
            this.parameters = new String[params.length()];
            for(int i = 0; i < params.length(); i++)
            {
                this.parameters[i] = params.getString(i);
            }

            this.oldvalues = obj.getJSONArray("oldvalues");
            this.oldvalues = obj.getJSONArray("newvalues");
        }

        public static IFTTTEventType typeToEvent(String type)
        {
            if(type.equalsIgnoreCase("ANY"))
            {
                return IFTTTEventType.ANY;
            }
            else if(type.equalsIgnoreCase("VALUE_CHANGED"))
            {
                return IFTTTEventType.VALUE_CHANGED;
            }
            else if(type.equalsIgnoreCase("DISCONNECTED"))
            {
                return IFTTTEventType.DISCONNECTED;
            }
            else if(type.equalsIgnoreCase("CONNECTED"))
            {
                return IFTTTEventType.CONNECTED;
            }
            else return IFTTTEventType.ANY;
        }

        public JSONArray getOldvalues()
        {
            return this.oldvalues;
        }

        public JSONArray getNewvalues()
        {
            return this.newvalues;
        }
    }

    public enum IFTTTEventType { ANY, VALUE_CHANGED, DISCONNECTED, CONNECTED }

    public IFTTTEventType type;

    public IFTTTEvent ofType(IFTTTEventType type)
    {
        this.type = type;
        return this;
    }

    /**
     * Applies this IFTTTEvent to given event string
     * @param event String containing a description of the event
     * @return true if the event matches, false otherwise
     */
    public abstract boolean apply(Event event);
}