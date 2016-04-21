package it.giuggi.iotremote.ifttt.implementations.event;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Only accepts VALUE_CHANGED events
 * @see it.giuggi.iotremote.ifttt.structure.IFTTTEvent.IFTTTEventType
 */
public class ValueChangedEvent extends TypeEvent
{
    public ValueChangedEvent()
    {
        super(IFTTTEventType.VALUE_CHANGED);
    }
}
