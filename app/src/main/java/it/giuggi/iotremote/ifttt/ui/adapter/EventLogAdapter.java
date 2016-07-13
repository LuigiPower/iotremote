package it.giuggi.iotremote.ifttt.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.Event;
import it.giuggi.iotremote.ui.adapter.BaseViewHolder;

/**
 * Created by Federico Giuggioloni on 03/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class EventLogAdapter extends RecyclerView.Adapter<EventLogAdapter.CustomViewHolder>
{
    private List<Event> originalRef;
    private ViewGroup container;

    public EventLogAdapter(List<Event> originalRef, ViewGroup container) {
        this.originalRef = originalRef;
        this.container = container;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.riga_event_log, container, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onViewRecycled(CustomViewHolder holder)
    {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        Event event = originalRef.get(i);

        customViewHolder.eventToolbar.setTitle(event.type.name());
        customViewHolder.eventToolbar.setTitleTextColor(Color.WHITE);

        customViewHolder.senderName.setText(event.sender_name);
        customViewHolder.senderIp.setText(event.ip_address);
        customViewHolder.modeName.setText(event.mode_name);

        customViewHolder.parameters.setText(TextUtils.join(",", event.parameters));
        //customViewHolder.newvalues.setText(TextUtils.join(",", event.getNewValues()));
        //customViewHolder.oldvalues.setText(TextUtils.join(",", event.getOldValues()));
        customViewHolder.timestamp.setText(event.timestamp);
    }

    @Override
    public int getItemCount() {
        return (null != originalRef ? originalRef.size() : 0);
    }

    public class CustomViewHolder extends BaseViewHolder implements View.OnClickListener
    {
        protected CardView card;

        protected Toolbar eventToolbar;

        protected TextView senderName;
        protected TextView senderIp;
        protected TextView modeName;
        protected TextView parameters;
        protected TextView newvalues;
        protected TextView oldvalues;
        protected TextView timestamp;

        public CustomViewHolder(View view) {
            super(view);

            this.card = (CardView) view.findViewById(R.id.card_view);
            this.eventToolbar = (Toolbar) view.findViewById(R.id.event_summary);
            this.senderName = (TextView) view.findViewById(R.id.node_name);
            this.senderIp = (TextView) view.findViewById(R.id.node_ip);
            this.modeName = (TextView) view.findViewById(R.id.mode_name);
            this.parameters = (TextView) view.findViewById(R.id.parameters);
            //this.newvalues = (TextView) view.findViewById(R.id.newvalues);
            //this.oldvalues = (TextView) view.findViewById(R.id.oldvalues);
            this.timestamp = (TextView) view.findViewById(R.id.timestamp);
        }

        @Override
        public void onClick(View v)
        {
            Event component = (Event) v.getTag();

            //TODO show details?
        }
    }
}
