package it.giuggi.iotremote.ifttt.implementations.filter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Filter that does a check on the node's name
 */
public class NameFilter extends IFTTTFilter
{
    public String name;

    public NameFilter()
    {
        this("esp0");
    }

    public NameFilter(String name)
    {
        this.name = name;
    }

    @Override
    public boolean apply(IOTNode node)
    {
        return node.name.equalsIgnoreCase(this.name);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_name;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_name;
    }

    @Override
    protected void populateView(View view)
    {
        TextView nodename = (TextView) view.findViewById(R.id.node_name);
        nodename.setText(this.name);
    }

    @Override
    protected void populateEditView(View view)
    {
        EditText nodename = (EditText) view.findViewById(R.id.node_name);
        nodename.setText(this.name);

        nodename.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.name_filter;
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_assignment_24dp;
    }
}
