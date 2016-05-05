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

    public static IFTTTListFragment newInstance()
    {
        return new IFTTTListFragment();
    }

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
