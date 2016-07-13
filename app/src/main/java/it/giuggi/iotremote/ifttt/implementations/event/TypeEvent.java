package it.giuggi.iotremote.ifttt.implementations.event;

import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.Event;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;
import it.giuggi.iotremote.ifttt.ui.adapter.TextSpinnerAdapter;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Checks if event is of specified type
 */
public class TypeEvent extends IFTTTEvent
{
    private IFTTTEventType type;

    public TypeEvent()
    {
        this(IFTTTEventType.CONNECTED);
    }

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
        TextView type = (TextView) view.findViewById(R.id.event_type);
        ArrayList<Pair<String, String>> list = IFTTTEvent.getListOfEvents(view.getResources());

        for(Pair<String, String> pair : list)
        {
            if(pair.first.equalsIgnoreCase(this.type.name()))
            {
                type.setText(pair.second);
                break;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void populateEditView(final View view)
    {
        Spinner event = (Spinner) view.findViewById(R.id.event_type);
        ArrayList<Pair<String, String>> list = IFTTTEvent.getListOfEvents(view.getResources());

        TextSpinnerAdapter adapter = new TextSpinnerAdapter(view.getContext(), R.layout.autocompletetextentry, list);
        event.setAdapter(adapter);

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).first.equalsIgnoreCase(type.name()))
            {
                event.setSelection(i);
                break;
            }
        }

        event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Pair<String, String> selected = (Pair<String, String>) parent.getItemAtPosition(position);
                type = Event.typeToEvent(selected.first);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_info_24dp;
    }
}
