package it.giuggi.iotremote.ifttt;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import it.giuggi.iotremote.iot.IOTNode;
import it.giuggi.iotremote.iot.IOperatingMode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Generic IFTTTRule
 * Contains a list of filters, contexts and events, and a list of actions to run
 * if all these conditions are met
 */
public class IFTTTRule
{
    LinkedList<IFTTTFilter> iftttFilters;
    LinkedList<IFTTTContext> iftttContexts;
    LinkedList<IFTTTEvent> iftttEvents;
    LinkedList<IFTTTAction> iftttActions;

    /**
     * Creates a new IFTTTRule
     * @param iftttFilters An optional list of filters. if null, an empty list is created
     * @param iftttContexts An optional list of contexts. if null, an empty list is created
     * @param iftttEvents An optional list of events. if null, an empty list is created
     * @param iftttActions An optional list of actions. if null, an empty list is created
     */
    public IFTTTRule(@Nullable LinkedList<IFTTTFilter> iftttFilters,
                     @Nullable LinkedList<IFTTTContext> iftttContexts,
                     @Nullable LinkedList<IFTTTEvent> iftttEvents,
                     @Nullable LinkedList<IFTTTAction> iftttActions)
    {
        if(iftttFilters == null)
        {
            this.iftttFilters = new LinkedList<>();
        }
        else
        {
            this.iftttFilters = iftttFilters;
        }

        if(iftttContexts == null)
        {
            this.iftttContexts = new LinkedList<>();
        }
        else
        {
            this.iftttContexts = iftttContexts;
        }

        if(iftttEvents == null)
        {
            this.iftttEvents = new LinkedList<>();
        }
        else
        {
            this.iftttEvents = iftttEvents;
        }

        if(iftttActions == null)
        {
            this.iftttActions = new LinkedList<>();
        }
        else
        {
            this.iftttActions = iftttActions;
        }
    }

    /**
     * Applies this rule to given data (a gcm message)
     * data format:
     *      {
     *          "node": { node_data as usual },         //This is the node that made this event happen
     *          "event": {
     *              "type": "VALUE_CHANGED",            //One of the event types
     *              "mode_name": "gpio_read_mode",      //Mode that made this event happen TODO consider changing this into a copy of said mode taken from the node part
     *              "params": ["status"],           //Parameters that made this event happen
     *              "oldvalues": [0],                   //Old values of said parameters
     *              "newvalues": [1],                   //New values of said parameters
     *          }
     *      }
     * @return true if it is applicable, false otherwise
     */
    public boolean apply(JSONObject gcmMessage) throws JSONException
    {
        /**
         * Initialize IoTNode from node part of the GCM message
         */
        JSONObject nodeJson = gcmMessage.getJSONObject("node");
        JSONObject modeJson = nodeJson.getJSONObject("mode");
        IOperatingMode mode = IOperatingMode.stringToMode(modeJson.getString("name"), modeJson.getJSONObject("params"));
        IOTNode node = new IOTNode(nodeJson.getString("ip"), nodeJson.getString("name"), mode);

        /**
         * Initialize Event from event part of the GCM message
         */
        JSONObject eventJson = gcmMessage.getJSONObject("event");
        IFTTTEvent.Event event = new IFTTTEvent.Event(eventJson);

        boolean result = false;
        for(IFTTTFilter iftttFilter : iftttFilters)
        {
            result = iftttFilter.apply(node);
            if(!result) return false;
        }

        for(IFTTTEvent iftttEvent : iftttEvents)
        {
            result = iftttEvent.apply(event);
            if(!result) return false;
        }

        IFTTTCurrentSituation currentSituation = IFTTTCurrentSituation.acquireSnapshot();

        for(IFTTTContext iftttContext : iftttContexts)
        {
            result = iftttContext.apply(currentSituation);
            if(!result) return false;
        }

        //All done, run the actions and return true
        for(IFTTTAction action : iftttActions)
        {
            action.doAction();
        }
        return true;
    }
}
