package it.giuggi.iotremote.ifttt.implementations.event;

import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_empty;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.detail_empty;
    }

    @Override
    public View loadView(ViewGroup parent)
    {
        return new View(parent.getContext());
    }

    @Override
    public View loadEditView(ViewGroup parent)
    {
        return new View(parent.getContext());
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_visibility_off_24dp;
    }
}
