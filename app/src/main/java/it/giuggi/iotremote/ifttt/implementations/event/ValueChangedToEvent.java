package it.giuggi.iotremote.ifttt.implementations.event;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        this("1");
    }

    public ValueChangedToEvent(String valueTo)
    {
        this.valueTo = valueTo;
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_type_value_changed_to;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_type_value_changed_to;
    }

    @Override
    protected void populateView(View view)
    {
        super.populateView(view);

        TextView parameterFrom = (TextView) view.findViewById(R.id.parameter_from);
        parameterFrom.setText(valueTo);
    }

    @Override
    protected void populateEditView(View view)
    {
        super.populateEditView(view);

        EditText parameterFrom = (EditText) view.findViewById(R.id.parameter_from);
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
