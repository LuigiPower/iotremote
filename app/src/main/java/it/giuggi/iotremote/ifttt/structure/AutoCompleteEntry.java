package it.giuggi.iotremote.ifttt.structure;

import it.giuggi.iotremote.ifttt.ui.adapter.AutoCompleteAdapter;
import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by Federico Giuggioloni on 11/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class AutoCompleteEntry
{
    public AutoCompleteEntry()
    {

    }

    public abstract void loadView(AutoCompleteAdapter.AutoCompleteEntryViewHolder holder);

    public abstract Filter getFilter(AutoCompleteAdapter autoCompleteAdapter, ArrayList<AutoCompleteEntry> data);
}
