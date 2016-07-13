package it.giuggi.iotremote.ifttt.implementations.event;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.Event;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Checks if all old values are equal to the one passed in at construction time
 */
public class ValueChangedFromEvent extends ValueChangedEvent
{
    String valueFrom;

    public ValueChangedFromEvent()
    {
        this("1");
    }

    public ValueChangedFromEvent(String valueFrom)
    {
        super();
        this.valueFrom = valueFrom;
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_type_value_changed_from;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_type_value_changed_from;
    }

    @Override
    protected void populateView(View view)
    {
        super.populateView(view);

        TextView parameterFrom = (TextView) view.findViewById(R.id.parameter_from);
        parameterFrom.setText(valueFrom);
    }

    @Override
    protected void populateEditView(View view)
    {
        super.populateEditView(view);

        EditText parameterFrom = (EditText) view.findViewById(R.id.parameter_from);
        parameterFrom.setText(valueFrom);
        parameterFrom.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                valueFrom = s.toString();
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
