package it.giuggi.iotremote.ifttt.implementations.event;

import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.ui.adapter.TextSpinnerAdapter;
import it.giuggi.iotremote.iot.mode.IOperatingMode;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Only accepts VALUE_CHANGED events
 * {@link it.giuggi.iotremote.ifttt.structure.IFTTTEvent.IFTTTEventType}
 */
public class ValueChangedEvent extends TypeEvent
{
    public String parameterName;

    public ValueChangedEvent()
    {
        this(IOperatingMode.Parameters.STATUS);
    }

    public ValueChangedEvent(String parameterName)
    {
        super(IFTTTEventType.VALUE_CHANGED);
        this.parameterName = parameterName;
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_type_value_changed;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_type_value_changed;
    }

    @Override
    protected void populateView(View view)
    {
        TextView parameter = (TextView) view.findViewById(R.id.parameter_name);
        ArrayList<Pair<String, String>> list = IOperatingMode.getParameterList(view.getResources());
        for(Pair<String, String> pair : list)
        {
            if(pair.first.equalsIgnoreCase(this.parameterName))
            {
                parameter.setText(pair.second);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void populateEditView(final View view)
    {
        Spinner parameter = (Spinner) view.findViewById(R.id.parameter_name);
        ArrayList<Pair<String, String>> list = IOperatingMode.getParameterList(view.getResources());

        TextSpinnerAdapter adapter = new TextSpinnerAdapter(view.getContext(), R.layout.autocompletetextentry, list);
        parameter.setAdapter(adapter);

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).first.equalsIgnoreCase(parameterName))
            {
                parameter.setSelection(i);
                break;
            }
        }

        parameter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Pair<String, String> selected = (Pair<String, String>) parent.getItemAtPosition(position);
                parameterName = selected.first;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.value_changed_event;
    }
}
