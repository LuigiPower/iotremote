package it.giuggi.iotremote.ifttt.structure;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.util.Pair;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.database.Databasable;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.iot.mode.IOperatingMode;
import it.giuggi.iotremote.iot.node.IOTNode;

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
