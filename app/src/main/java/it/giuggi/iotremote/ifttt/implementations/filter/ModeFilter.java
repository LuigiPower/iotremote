package it.giuggi.iotremote.ifttt.implementations.filter;

import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.ifttt.ui.adapter.TextSpinnerAdapter;
import it.giuggi.iotremote.iot.node.IOTNode;
import it.giuggi.iotremote.iot.mode.IOperatingMode;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class ModeFilter extends IFTTTFilter
{
    private String name;

    public ModeFilter()
    {
        this("empty_mode");
    }

    public ModeFilter(String name)
    {
        this.name = name;
    }

    @Override
    public boolean apply(IOTNode node)
    {
        return node.hasMode(this.name);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_mode;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_mode;
    }

    @Override
    protected void populateView(View view)
    {
        TextView modename = (TextView) view.findViewById(R.id.mode_name);

        ArrayList<Pair<String, String>> list = IOperatingMode.getModeValueMatching(view.getResources());
        for(Pair<String, String> pair : list)
        {
            if(pair.first.equalsIgnoreCase(this.name))
            {
                modename.setText(pair.second);
                break;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void populateEditView(View view)
    {
        Spinner modename = (Spinner) view.findViewById(R.id.mode_name);
        ArrayList<Pair<String, String>> list = IOperatingMode.getModeValueMatching(view.getResources());

        TextSpinnerAdapter adapter = new TextSpinnerAdapter(view.getContext(), R.layout.autocompletetextentry, list);
        modename.setAdapter(adapter);

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).first.equalsIgnoreCase(name))
            {
                modename.setSelection(i);
                break;
            }
        }

        modename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Pair<String, String> selected = (Pair<String, String>) parent.getItemAtPosition(position);
                name = selected.first;
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
        return R.string.mode_filter;
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_extension_24dp;
    }
}
