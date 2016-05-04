package it.giuggi.iotremote.ifttt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.ui.adapter.RuleAdapter;
import it.giuggi.iotremote.ui.fragment.BaseFragment;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class IFTTTListFragment extends BaseFragment implements View.OnClickListener
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

        IFTTTDatabase database = IFTTTDatabase.getHelper(getContext());

        /* TODO add interface to create these rules, use this schema
        IFTTTRule rule = new IFTTTRule("rule1", null, null, null, null);
        rule.addFilter(new NameFilter("esp0"));
        rule.addFilter(new NameFilter("esp1"));
        rule.addFilter(new NameFilter("esp2"));
        rule.addFilter(new NameFilter("esp3"));
        rule.addEvent(new ValueChangedEvent());
        rule.addEvent(new ValueChangedFromToEvent("HIGH", "LOW"));
        rule.addEvent(new ValueChangedToEvent("HIGH"));
        rule.addContext(new WifiNameContext("ALMAWIFI"));
        rule.addContext(new WifiNameContext("Gdf van #3"));
        rule.addAction(new NotificationAction(getContext(), "I'm a notification!"));
        rule.addAction(new NotificationAction(getContext(), "I'm a notification 5124!"));

        IFTTTRule rule2 = new IFTTTRule("rule2", null, null, null, null);
        rule2.addFilter(new ModeFilter("gpio_mode"));
        rule2.addEvent(new ValueChangedEvent());
        rule2.addContext(new WifiNameContext("Gdf van #3"));
        rule2.addAction(new NotificationAction(getContext(), "I'm a notification 2!"));

        IFTTTRule rule3 = new IFTTTRule("rule3", null, null, null, null);
        rule3.addFilter(new NameFilter("esp2"));
        rule3.addEvent(new ValueChangedToEvent("HIGH"));
        rule3.addContext(new WifiNameContext("Gdf van #3"));
        rule3.addAction(new NotificationAction(getContext(), "I'm a notification 2!"));

        IFTTTRule rule4 = new IFTTTRule("rule4", null, null, null, null);
        rule4.addFilter(new NameFilter("esp3"));
        rule4.addEvent(new ValueChangedFromEvent("LOW"));
        rule4.addContext(new WifiNameContext("Gdf van #3"));
        rule4.addAction(new NotificationAction(getContext(), "I'm a notification 2!"));

        IFTTTRule rule5 = new IFTTTRule("rule5", null, null, null, null);
        rule5.addAction(new NotificationAction("I'm a notification of an all encompassing Rule!"));
        rule5.save(getContext());
        */

        try
        {
            ruleList = (ArrayList<IFTTTRule>) database.getRuleList();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.element_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RuleAdapter(ruleList, recyclerView);
        recyclerView.setAdapter(adapter);

        View fab = v.findViewById(R.id.add_button);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v)
    {
        //TODO rule name?
        controller.go(IFTTTRuleDetail.newInstance(new IFTTTRule("New Rule", null, null, null, null)));
    }
}
