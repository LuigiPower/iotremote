package it.giuggi.iotremote.ifttt.implementations.event;

import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Checks if event is of specified type
 */
public class TypeEvent extends IFTTTEvent
{
    private IFTTTEventType type;

    public TypeEvent(IFTTTEventType type)
    {
        this.type = type;
    }

    @Override
    public boolean apply(Event event)
    {
        return event.type == this.type;
    }
}
