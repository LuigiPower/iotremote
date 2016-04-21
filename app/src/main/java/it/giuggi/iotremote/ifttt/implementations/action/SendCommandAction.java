package it.giuggi.iotremote.ifttt.implementations.action;

import it.giuggi.iotremote.ifttt.structure.IFTTTAction;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class SendCommandAction extends IFTTTAction
{
    private IOTNode target;
    private String action;

    public SendCommandAction(IOTNode target, String action)
    {
        this.target = target;
        this.action = action;
    }

    @Override
    public void doAction()
    {
        this.target.sendCommand(action);
    }
}
