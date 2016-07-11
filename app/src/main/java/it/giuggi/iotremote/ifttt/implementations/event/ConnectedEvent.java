package it.giuggi.iotremote.ifttt.implementations.event;

import android.view.View;
import android.view.ViewGroup;

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
        return R.drawable.ic_visibility_24dp;
    }
}
