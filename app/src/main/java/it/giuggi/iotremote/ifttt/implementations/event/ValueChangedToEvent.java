package it.giuggi.iotremote.ifttt.implementations.event;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Checks if all newvalues changed to specified value
 */
public class ValueChangedToEvent extends ValueChangedEvent
{
    private String valueTo;

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
}
