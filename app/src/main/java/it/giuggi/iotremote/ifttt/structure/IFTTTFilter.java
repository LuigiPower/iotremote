package it.giuggi.iotremote.ifttt.structure;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * {@link IFTTTComponent} of type FILTER
 * Handles node type filtering
 */
public abstract class IFTTTFilter extends IFTTTComponent
{
    public static final String TYPE = "FILTER";

    public IFTTTFilter()
    {

    }

    @Override
    public int getColorId()
    {
        return R.color.colorFilter;
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
