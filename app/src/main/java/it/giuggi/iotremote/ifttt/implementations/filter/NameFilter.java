package it.giuggi.iotremote.ifttt.implementations.filter;

import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Filter that does a check on the node's name
 */
public class NameFilter extends IFTTTFilter
{
    public String name;

    public NameFilter(String name)
    {
        super();

        this.name = name;
    }

    @Override
    public boolean apply(IOTNode node)
    {
        return node.name.equalsIgnoreCase(this.name);
    }
}
