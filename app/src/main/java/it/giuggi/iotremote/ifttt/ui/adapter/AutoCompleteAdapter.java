package it.giuggi.iotremote.ifttt.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.AutoCompleteEntry;

/**
 * Created by Federico Giuggioloni on 11/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class AutoCompleteAdapter extends ArrayAdapter<AutoCompleteEntry>
{
    private ArrayList<AutoCompleteEntry> data;
    private int resource;

    public AutoCompleteAdapter(Context context, int resource, ArrayList<AutoCompleteEntry> objects)
    {
        super(context, resource, objects);
        this.resource = resource;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        AutoCompleteEntryViewHolder holder;
        if(v == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(resource, parent, false);
            holder = new AutoCompleteEntryViewHolder(v);
            v.setTag(holder);
        }
        else
        {
            holder = (AutoCompleteEntryViewHolder) v.getTag();
        }

        AutoCompleteEntry entry = getItem(position);
        entry.loadView(holder);
        return v;
    }

    @Override
    public Filter getFilter()
    {
        try
        {
            AutoCompleteEntry entry = getItem(0);
            return entry.getFilter(this, data);
        }
        catch(IndexOutOfBoundsException e)
        {
            return super.getFilter();
        }
    }

    public class AutoCompleteEntryViewHolder extends RecyclerView.ViewHolder
    {
        public TextView text;

        public AutoCompleteEntryViewHolder(View itemView)
        {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.entry_text);
        }
    }
}
