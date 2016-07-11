package it.giuggi.iotremote.ifttt.ui.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 11/07/16.
 * ModeSpinnerAdapter
 * All pairs are (value, visualization name) pairs
 */
public class TextSpinnerAdapter extends ArrayAdapter<Pair<String, String>>
{
    private int resource;

    public TextSpinnerAdapter(Context context, int resource, ArrayList<Pair<String, String>> objects)
    {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        PairViewHolder holder;
        if(v == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(resource, parent, false);
            holder = new PairViewHolder(v);
            v.setTag(holder);
        }
        else
        {
            holder = (PairViewHolder) v.getTag();
        }

        Pair<String, String> entry = getItem(position);
        holder.text.setText(entry.second);
        return v;
    }

    public class PairViewHolder extends RecyclerView.ViewHolder
    {
        public TextView text;

        public PairViewHolder(View itemView)
        {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.entry_text);
        }

    }
}
