package it.giuggi.iotremote.ifttt;

import java.util.LinkedList;

import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IFTTTFilter
{
    public String type = null;
    public String name = null;
    public String mode = null;

    IFTTTFilter ofType(String type)
    {
        this.type = type;
        return this;
    }

    IFTTTFilter ofName(String name)
    {
        this.name = name;
        return this;
    }

    IFTTTFilter ofMode(String mode)
    {
        this.mode = mode;
        return this;
    }

    /**
     * Apply this IFTTTFilter to given node
     * @param node Node on which to apply this filter
     * @return true if node passes this filter, false otherwise
     */
    public abstract boolean apply(IOTNode node);
}
