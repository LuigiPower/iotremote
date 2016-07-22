package it.giuggi.iotremote.ifttt.implementations.event;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.Event;
import it.giuggi.iotremote.iot.mode.IOperatingMode;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Checks if all newvalues changed to specified value
 */
public class ValueChangedBelowEvent extends ValueChangedEvent
{
    private String threshold;

    public ValueChangedBelowEvent()
    {
        this("1");
    }

    public ValueChangedBelowEvent(String valueTo)
    {
        this.threshold = valueTo;
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_type_value_changed_below;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_type_value_changed_below;
    }

    @Override
    protected void populateView(View view)
    {
        super.populateView(view);

        TextView parameterFrom = (TextView) view.findViewById(R.id.parameter_below);
        parameterFrom.setText(threshold);
    }

    @Override
    protected void populateEditView(View view)
    {
        super.populateEditView(view);

        EditText parameterFrom = (EditText) view.findViewById(R.id.parameter_below);
        parameterFrom.setText(threshold);
        parameterFrom.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                threshold = s.toString();
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
        boolean ok = false;
        float floatThreshold = Float.parseFloat(threshold);

        for(String value : event.getNewValues())
        {
            float floatValue;
            try
            {
                floatValue = Float.parseFloat(value);
            }
            catch(NumberFormatException ex)
            {
                try
                {
                    JSONArray sensorArray = new JSONArray(value);
                    JSONObject last = sensorArray.getJSONObject(sensorArray.length() - 1);
                    floatValue = (float) last.getDouble(IOperatingMode.Parameters.CURRENT_VALUE);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    continue;
                }
            }

            if(floatValue < floatThreshold)
            {
                ok = true;
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
