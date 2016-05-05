package it.giuggi.iotremote.ifttt.implementations.filter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.iot.IOTNode;

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
        //TODO better layout?
        TextView modename = (TextView) view.findViewById(R.id.mode_name);
        modename.setText(this.name);
    }

    @Override
    protected void populateEditView(View view)
    {
        //TODO better layout?
        EditText modename = (EditText) view.findViewById(R.id.mode_name);
        modename.setText(this.name);
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
