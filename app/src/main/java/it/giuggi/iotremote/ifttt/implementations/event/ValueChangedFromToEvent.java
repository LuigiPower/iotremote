package it.giuggi.iotremote.ifttt.implementations.event;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * First checks if all new values are equal to the one passed in at construction time
 * @see it.giuggi.iotremote.ifttt.implementations.event.ValueChangedFromEvent Uses this after checking new values
 */
public class ValueChangedFromToEvent extends ValueChangedFromEvent
{
    private String valueTo;

    public ValueChangedFromToEvent(String valueFrom, String valueTo)
    {
        super(valueFrom);
        this.valueTo = valueTo;
    }

    @Override
    public boolean apply(Event event)
    {
        boolean ok = true;

        for(String value : event.getNewValues())
        {
            if(!value.equals(this.valueTo))
            {
                ok = false;
            }
        }

        return super.apply(event) && ok;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.value_changed_from_to_event;
    }
}
