package it.giuggi.iotremote.ifttt.implementations.event;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.Event;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * First checks if all new values are equal to the one passed in at construction time
 * @see it.giuggi.iotremote.ifttt.implementations.event.ValueChangedFromEvent Uses this after checking new values
 */
public class ValueChangedFromToEvent extends ValueChangedFromEvent
{
    private String valueTo;

    public ValueChangedFromToEvent()
    {
        this("0", "1");
    }

    public ValueChangedFromToEvent(String valueFrom, String valueTo)
    {
        super(valueFrom);
        this.valueTo = valueTo;
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_type_value_changed_from_to;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_type_value_changed_from_to;
    }

    @Override
    protected void populateView(View view)
    {
        super.populateView(view);

        TextView parameterFrom = (TextView) view.findViewById(R.id.parameter_to);
        parameterFrom.setText(valueTo);
    }

    @Override
    protected void populateEditView(View view)
    {
        super.populateEditView(view);

        EditText parameterFrom = (EditText) view.findViewById(R.id.parameter_to);
        parameterFrom.setText(valueTo);
        parameterFrom.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                valueTo = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    @Override
    public boolean apply(Event event)
    {
        boolean ok = super.apply(event);

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
