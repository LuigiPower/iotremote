package it.giuggi.iotremote.ifttt.structure;

import android.content.Context;

import com.google.gson.Gson;

import java.util.LinkedList;

import it.giuggi.iotremote.ifttt.database.Databasable;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IFTTTFilter extends IFTTTComponent
{
    public static final String TYPE = "FILTER";

    public IFTTTFilter()
    {

    }

    @Override
    protected String getType()
    {
        return TYPE;
    }

    /**
     * Apply this IFTTTFilter to given node
     * @param node Node on which to apply this filter
     * @return true if node passes this filter, false otherwise
     */
    public abstract boolean apply(IOTNode node);

    public String toLogString()
    {
        return toString();
    }
}
