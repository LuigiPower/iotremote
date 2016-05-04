package it.giuggi.iotremote.ifttt.implementations.event;

import org.json.JSONArray;
import org.json.JSONException;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Checks if all old values are equal to the one passed in at construction time
 */
public class ValueChangedFromEvent extends ValueChangedEvent
{
    String valueFrom;

    public ValueChangedFromEvent(String valueFrom)
    {
        super();
        this.valueFrom = valueFrom;
    }

    @Override
    public boolean apply(Event event)
    {
        boolean ok = true;

        for(String value : event.getOldValues())
        {
            if(!value.equals(this.valueFrom))
            {
                ok = false;
            }
        }

        return super.apply(event) && ok;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.value_changed_event;
    }
}
