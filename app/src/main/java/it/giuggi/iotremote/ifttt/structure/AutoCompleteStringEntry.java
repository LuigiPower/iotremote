package it.giuggi.iotremote.ifttt.structure;

import android.util.Log;
import android.widget.Filter;

import java.util.ArrayList;

import it.giuggi.iotremote.ifttt.ui.adapter.AutoCompleteAdapter;

/**
 * Created by Federico Giuggioloni on 11/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class AutoCompleteStringEntry extends AutoCompleteEntry
{
    private CharSequence text;

    public AutoCompleteStringEntry(CharSequence text)
    {
        super();
        this.text = text;
    }

    @Override
    public void loadView(AutoCompleteAdapter.AutoCompleteEntryViewHolder holder)
    {
        holder.text.setText(this.text);
    }

    @Override
    public Filter getFilter(AutoCompleteAdapter autoCompleteAdapter, ArrayList<AutoCompleteEntry> data)
    {
        return new StringFilter(autoCompleteAdapter, data);
    }

    public String getString()
    {
        return text.toString();
    }

    public CharSequence getText()
    {
        return text;
    }

    private class StringFilter extends Filter {
        private final Object lock = new Object();
        private ArrayList<AutoCompleteEntry> originalValues;
        private ArrayList<AutoCompleteEntry> fullList;
        private AutoCompleteAdapter adapter;

        public StringFilter(AutoCompleteAdapter adapter, ArrayList<AutoCompleteEntry> entries)
        {
            this.adapter = adapter;
            this.fullList = entries;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (originalValues == null)
            {
                synchronized (lock)
                {
                    originalValues = new ArrayList<>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0)
            {
                synchronized (lock) {
                    fullList.addAll(originalValues);
                }
            }
            else
            {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<AutoCompleteEntry> values = originalValues;
                int count = values.size();

                fullList.clear();
                for (int i = 0; i < count; i++)
                {
                    AutoCompleteStringEntry item = (AutoCompleteStringEntry) values.get(i);
                    if (item.getString().toLowerCase().contains(prefixString))
                    {
                        fullList.add(item);
                    }

                }
            }

            results.values = fullList;
            results.count = fullList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0)
            {
                adapter.notifyDataSetChanged();
            }
            else
            {
                adapter.notifyDataSetInvalidated();
            }
        }
    }
}
