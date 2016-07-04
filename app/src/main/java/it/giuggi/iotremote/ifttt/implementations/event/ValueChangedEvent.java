package it.giuggi.iotremote.ifttt.implementations.event;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Only accepts VALUE_CHANGED events
 * {@link it.giuggi.iotremote.ifttt.structure.IFTTTEvent.IFTTTEventType}
 */
public class ValueChangedEvent extends TypeEvent
{
    public ValueChangedEvent()
    {
        super(IFTTTEventType.VALUE_CHANGED);
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.value_changed_event;
    }
}
