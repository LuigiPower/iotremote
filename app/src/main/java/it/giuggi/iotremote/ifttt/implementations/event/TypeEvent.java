package it.giuggi.iotremote.ifttt.implementations.event;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import it.giuggi.iotremote.R;
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

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_type;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_type;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.type_event;
    }

    @Override
    protected void populateView(View view)
    {
        //TODO better layout
        TextView type = (TextView) view.findViewById(R.id.event_type);
        type.setText(this.type.toString());
    }

    @Override
    protected void populateEditView(View view)
    {
        //TODO better layout
        EditText type = (EditText) view.findViewById(R.id.event_type);
        type.setText(this.type.toString());
    }
}
