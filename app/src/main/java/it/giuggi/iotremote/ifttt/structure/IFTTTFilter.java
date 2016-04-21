package it.giuggi.iotremote.ifttt.structure;

import java.util.LinkedList;

import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IFTTTFilter
{
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
