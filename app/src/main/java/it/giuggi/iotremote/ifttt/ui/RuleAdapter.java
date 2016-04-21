package it.giuggi.iotremote.ifttt.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.adapter.BaseViewHolder;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.CustomViewHolder>
{
    private List<IFTTTRule> ruleList;
    private ViewGroup container;

    public RuleAdapter(List<IFTTTRule> ruleList, ViewGroup container) {
        this.ruleList = ruleList;
        this.container = container;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rule_row, container, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        IFTTTRule rule = ruleList.get(i);

        customViewHolder.card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO things about this rule (dialog to edit it or something (I want another fragment to edit it, to allow master-detail flow))
            }
        });

        String filterName = rule.getFilterAt(0).getClass().getName();
        String eventName = rule.getEventAt(0).getClass().getName();
        String contextName = rule.getContextAt(0).getClass().getName();
        String actionName = rule.getActionAt(0).getClass().getName();

        //TODO change this, icons would be nice looking
        customViewHolder.filter.setText(filterName.substring(filterName.lastIndexOf('.') + 1));
        customViewHolder.event.setText(eventName.substring(eventName.lastIndexOf('.') + 1));
        customViewHolder.context.setText(contextName.substring(contextName.lastIndexOf('.') + 1));
        customViewHolder.action.setText(actionName.substring(actionName.lastIndexOf('.') + 1));
    }

    @Override
    public int getItemCount() {
        return (null != ruleList ? ruleList.size() : 0);
    }

    public class CustomViewHolder extends BaseViewHolder
    {
        protected CardView card;
        protected TextView filter;
        protected TextView event;
        protected TextView context;
        protected TextView action;

        public CustomViewHolder(View view) {
            super(view);

            this.card = (CardView) view.findViewById(R.id.card_view);
            this.filter = (TextView) view.findViewById(R.id.filter);
            this.event = (TextView) view.findViewById(R.id.event);
            this.context = (TextView) view.findViewById(R.id.context);
            this.action = (TextView) view.findViewById(R.id.action);
        }
    }
}
