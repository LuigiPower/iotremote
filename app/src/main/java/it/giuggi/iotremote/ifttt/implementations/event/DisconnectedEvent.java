package it.giuggi.iotremote.ifttt.implementations.event;

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
}
