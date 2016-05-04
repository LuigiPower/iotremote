package it.giuggi.iotremote.ifttt.implementations.dummy;

import android.view.View;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;

/**
 * Created by Federico Giuggioloni on 04/05/16.
 * To allow last element in componentlist to be an "Add component" button
 */
public class DummyComponent extends IFTTTComponent
{
    private String type;

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
        return R.layout.add_component;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return 0;
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

    }
}
