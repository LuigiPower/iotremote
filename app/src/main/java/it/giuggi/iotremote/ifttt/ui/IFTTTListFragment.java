package it.giuggi.iotremote.ifttt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.fragment.BaseFragment;
import it.giuggi.iotremote.ifttt.implementations.action.NotificationAction;
import it.giuggi.iotremote.ifttt.implementations.context.WifiNameContext;
import it.giuggi.iotremote.ifttt.implementations.event.ValueChangedEvent;
import it.giuggi.iotremote.ifttt.implementations.event.ValueChangedFromEvent;
import it.giuggi.iotremote.ifttt.implementations.event.ValueChangedFromToEvent;
import it.giuggi.iotremote.ifttt.implementations.event.ValueChangedToEvent;
import it.giuggi.iotremote.ifttt.implementations.filter.ModeFilter;
import it.giuggi.iotremote.ifttt.implementations.filter.NameFilter;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class IFTTTListFragment extends BaseFragment
{
    public static final String TAG = "IFTTTLISTFRAGMENT";

    ArrayList<IFTTTRule> ruleList = new ArrayList<>(5);
    private RuleAdapter adapter;

    @Override
    public String generateTag()
    {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.ifttt_list_fragment, container, false);

        IFTTTRule rule = new IFTTTRule(null, null, null, null);
        rule.addFilter(new NameFilter("esp0"));
        rule.addEvent(new ValueChangedEvent());
        rule.addContext(new WifiNameContext("ALMAWIFI"));
        rule.addAction(new NotificationAction(getContext(), "I'm a notification!"));

        IFTTTRule rule2 = new IFTTTRule(null, null, null, null);
        rule2.addFilter(new ModeFilter("gpio_mode"));
        rule2.addEvent(new ValueChangedEvent());
        rule2.addContext(new WifiNameContext("Gdf van #3"));
        rule2.addAction(new NotificationAction(getContext(), "I'm a notification 2!"));

        IFTTTRule rule3 = new IFTTTRule(null, null, null, null);
        rule3.addFilter(new NameFilter("esp2"));
        rule3.addEvent(new ValueChangedToEvent("HIGH"));
        rule3.addContext(new WifiNameContext("Gdf van #3"));
        rule3.addAction(new NotificationAction(getContext(), "I'm a notification 2!"));

        IFTTTRule rule4 = new IFTTTRule(null, null, null, null);
        rule4.addFilter(new NameFilter("esp3"));
        rule4.addEvent(new ValueChangedFromEvent("LOW"));
        rule4.addContext(new WifiNameContext("Gdf van #3"));
        rule4.addAction(new NotificationAction(getContext(), "I'm a notification 2!"));

        IFTTTRule rule5 = new IFTTTRule(null, null, null, null);
        rule5.addFilter(new NameFilter("esp4"));
        rule5.addEvent(new ValueChangedFromToEvent("LOW", "HIGH"));
        rule5.addContext(new WifiNameContext("Gdf van #3"));
        rule5.addAction(new NotificationAction(getContext(), "I'm a notification 2!"));

        ruleList.add(rule);
        ruleList.add(rule2);
        ruleList.add(rule3);
        ruleList.add(rule4);
        ruleList.add(rule5);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rule_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RuleAdapter(ruleList, recyclerView);
        recyclerView.setAdapter(adapter);

        return v;
    }
}
