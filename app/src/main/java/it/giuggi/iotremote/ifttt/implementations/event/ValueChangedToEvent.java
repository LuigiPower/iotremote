package it.giuggi.iotremote.ifttt.implementations.event;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Checks if all newvalues changed to specified value
 */
public class ValueChangedToEvent extends ValueChangedEvent
{
    private String valueTo;

    public ValueChangedToEvent()
    {
        this("HIGH");
    }

    public ValueChangedToEvent(String valueTo)
    {
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
        return R.string.value_changed_to_event;
    }
}
