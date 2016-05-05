package it.giuggi.iotremote.ifttt.implementations.dummy;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTAction;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;

/**
 * Created by Federico Giuggioloni on 04/05/16.
 * To allow last element in componentlist to be an "Add component" button
 * TODO consider replacing with openable FAB with 4 options
 */
public class DummyComponent extends IFTTTComponent
{
    private transient String type;
    private transient IFTTTComponent workInProgress;

    public DummyComponent(String type)
    {
        this.type = type;
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.add_component;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_new_component;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.dummy;
    }

    @Override
    protected String getType()
    {
        return type;
    }

    @Override
    protected void populateView(View view)
    {
        //TODO set text to add <component_type>?
    }

    @Override
    protected void populateEditView(View view)
    {
        //TODO interface to create new Component

        final String[] data;
        final String[] values;
        final String packagename;
        final ViewGroup detail_container;

        detail_container = (ViewGroup) view.findViewById(R.id.new_component_details);

        //TODO sintetizza questi if magari
        if(type.equalsIgnoreCase(IFTTTFilter.TYPE))
        {
            data = view.getContext().getResources().getStringArray(R.array.filters);
            values = view.getContext().getResources().getStringArray(R.array.filter_classes);
            packagename = view.getContext().getResources().getString(R.string.filter_package);
        }
        else if(type.equalsIgnoreCase(IFTTTEvent.TYPE))
        {
            data = view.getContext().getResources().getStringArray(R.array.events);
            values = view.getContext().getResources().getStringArray(R.array.event_classes);
            packagename = view.getContext().getResources().getString(R.string.event_package);
        }
        else if(type.equalsIgnoreCase(IFTTTContext.TYPE))
        {
            data = view.getContext().getResources().getStringArray(R.array.contexts);
            values = view.getContext().getResources().getStringArray(R.array.context_classes);
            packagename = view.getContext().getResources().getString(R.string.context_package);
        }
        else if(type.equalsIgnoreCase(IFTTTAction.TYPE))
        {
            data = view.getContext().getResources().getStringArray(R.array.actions);
            values = view.getContext().getResources().getStringArray(R.array.action_classes);
            packagename = view.getContext().getResources().getString(R.string.action_package);
        }
        else
        {
            return; //TODO error handling, something went very wrong
        }

        Spinner spinner = (Spinner) view.findViewById(R.id.new_component_type);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                data);

        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String classname = values[position];
                String fullpackage = packagename + classname;

                //noinspection TryWithIdenticalCatches
                try
                {
                    Class<?> clazz = Class.forName(fullpackage);
                    workInProgress = (IFTTTComponent) clazz.newInstance();

                    detail_container.removeAllViews();
                    workInProgress.loadEditView(detail_container);
                } catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                } catch (InstantiationException e)
                {
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
}
