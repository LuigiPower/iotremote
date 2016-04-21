package it.giuggi.iotremote.ifttt.implementations.filter;

import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class ModeFilter extends IFTTTFilter
{
    private String name;

    public ModeFilter(String name)
    {
        this.name = name;
    }

    @Override
    public boolean apply(IOTNode node)
    {
        return node.hasMode(this.name);
    }
}
