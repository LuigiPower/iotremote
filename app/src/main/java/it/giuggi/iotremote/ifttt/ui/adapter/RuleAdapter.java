package it.giuggi.iotremote.ifttt.ui.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTRuleDetail;
import it.giuggi.iotremote.ui.adapter.BaseViewHolder;
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

        customViewHolder.card.setTag(R.id.complete_rule, rule);
        customViewHolder.card.setTag(R.id.location_name, 0);
        customViewHolder.card.setOnClickListener(customViewHolder);
        customViewHolder.card.setOnLongClickListener(customViewHolder);

        int filterImage = R.drawable.ic_done_black_24dp;
        int eventImage = filterImage;
        int contextImage = filterImage;
        int actionImage = filterImage;

        try
        {
            filterImage = rule.getFilterAt(0).getIcon();
        }
        catch(IndexOutOfBoundsException ignored) {}

        try
        {
            eventImage = rule.getEventAt(0).getIcon();
        }
        catch(IndexOutOfBoundsException ignored) {}

        try
        {
            contextImage = rule.getContextAt(0).getIcon();
        }
        catch(IndexOutOfBoundsException ignored) {}

        try
        {
            actionImage = rule.getActionAt(0).getIcon();
        }
        catch(IndexOutOfBoundsException ignored) {}

        customViewHolder.filter.setImageResource(filterImage);
        customViewHolder.event.setImageResource(eventImage);
        customViewHolder.context.setImageResource(contextImage);
        customViewHolder.action.setImageResource(actionImage);

        customViewHolder.filter.setOnClickListener(customViewHolder);
        customViewHolder.event.setOnClickListener(customViewHolder);
        customViewHolder.context.setOnClickListener(customViewHolder);
        customViewHolder.action.setOnClickListener(customViewHolder);

        customViewHolder.filter.setOnLongClickListener(customViewHolder);
        customViewHolder.event.setOnLongClickListener(customViewHolder);
        customViewHolder.context.setOnLongClickListener(customViewHolder);
        customViewHolder.action.setOnLongClickListener(customViewHolder);

        customViewHolder.filter.setTag(R.id.complete_rule, rule);
        customViewHolder.event.setTag(R.id.complete_rule, rule);
        customViewHolder.context.setTag(R.id.complete_rule, rule);
        customViewHolder.action.setTag(R.id.complete_rule, rule);

        customViewHolder.filter.setTag(R.id.location_name, 0);
        customViewHolder.event.setTag(R.id.location_name, 1);
        customViewHolder.context.setTag(R.id.location_name, 2);
        customViewHolder.action.setTag(R.id.location_name, 3);
    }

    @Override
    public int getItemCount() {
        return (null != ruleList ? ruleList.size() : 0);
    }

    public class CustomViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        protected CardView card;
        protected ImageView filter;
        protected ImageView event;
        protected ImageView context;
        protected ImageView action;

        public CustomViewHolder(View view) {
            super(view);

            this.card = (CardView) view.findViewById(R.id.card_view);
            this.filter = (ImageView) view.findViewById(R.id.filter);
            this.event = (ImageView) view.findViewById(R.id.event);
            this.context = (ImageView) view.findViewById(R.id.context);
            this.action = (ImageView) view.findViewById(R.id.action);
        }

        @Override
        public void onClick(View v)
        {
            IFTTTRule rule = (IFTTTRule) v.getTag(R.id.complete_rule);
            int position = (int) v.getTag(R.id.location_name);
            controller.go(IFTTTRuleDetail.newInstance(rule, position));
        }

        @Override
        public boolean onLongClick(final View v)
        {
            AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.dialog_delete_rule_title)
                    .setMessage(R.string.dialog_delete_rule_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            IFTTTRule rule = (IFTTTRule) v.getTag(R.id.complete_rule);
                            int position = 0;

                            for(int i = 0; i < ruleList.size(); i++)
                            {
                                IFTTTRule r = ruleList.get(i);
                                if(rule.getRuleid() == r.getRuleid())
                                {
                                    position = i;
                                    break;
                                }
                            }

                            ruleList.remove(position);
                            notifyItemRemoved(position);
                            rule.delete(v.getContext());
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();

            return true;
        }
    }
}
