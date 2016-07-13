package it.giuggi.iotremote.ifttt.implementations.filter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.AutoCompleteEntry;
import it.giuggi.iotremote.ifttt.structure.AutoCompleteStringEntry;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.ifttt.ui.adapter.AutoCompleteAdapter;
import it.giuggi.iotremote.iot.node.IOTNode;
import it.giuggi.iotremote.net.WebRequestTask;

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
    protected void populateEditView(final View view)
    {
        final AutoCompleteTextView nodename = (AutoCompleteTextView) view.findViewById(R.id.node_name);
        nodename.setText(this.name);

        WebRequestTask.perform(WebRequestTask.Azione.GET_NODE_LIST)
                .listen(new WebRequestTask.OnResponseListener()
                {
                    @Override
                    public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
                    {
                        JSONArray array = (JSONArray) ris;

                        ArrayList<AutoCompleteEntry> list = new ArrayList<>(10);
                        for(int i = 0; i < array.length(); i++)
                        {
                            try
                            {
                                JSONObject obj = array.getJSONObject(i);
                                list.add(new AutoCompleteStringEntry(obj.getString("name")));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        AutoCompleteAdapter adapter = new AutoCompleteAdapter(view.getContext(), R.layout.autocompletetextentry, list);
                        nodename.setAdapter(adapter);
                    }
                })
                .send();

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
