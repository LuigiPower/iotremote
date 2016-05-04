package it.giuggi.iotremote.ifttt.implementations.event;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class ConnectedEvent extends TypeEvent
{
    public ConnectedEvent()
    {
        super(IFTTTEventType.CONNECTED);
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.connected_event;
    }
}
