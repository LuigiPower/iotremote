package it.giuggi.iotremote.ifttt.implementations.event;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class DisconnectedEvent extends TypeEvent
{
    public DisconnectedEvent()
    {
        super(IFTTTEventType.DISCONNECTED);
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.disconnected_event;
    }
}
