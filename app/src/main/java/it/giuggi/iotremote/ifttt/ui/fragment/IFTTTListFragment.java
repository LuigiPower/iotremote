package it.giuggi.iotremote.ifttt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private boolean[] options;
    private int type;

    public IFTTTListFragment()
    {
        putLeft();
    }

    /**
     * Create a rule list fragment choosing whether or not to show certain columns
     * @see IFTTTRule
     * @param options 4 element array, true = show i column, false = don't show i column
     * @param type one of the constant integers in IFTTTRule, specifies rule types to load
     * @return IFTTTListFragment to show
     */
    public static IFTTTListFragment newInstance(boolean[] options, int type)
    {
        IFTTTListFragment list = new IFTTTListFragment();
        list.options = options;
        list.type = type;
        return list;
    }

    public static IFTTTListFragment newInstance()
    {
        IFTTTListFragment list = new IFTTTListFragment();
        list.options = new boolean[]{true, true, true, true};
        list.type = IFTTTRule.RULE_TYPE_ACTIVE;
        return list;
    }

    @Override
    public String generateTag()
    {
        return TAG;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey("options"))
        {
            options = savedInstanceState.getBooleanArray("options");
            type = savedInstanceState.getInt("type");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray("options", options);
        outState.putInt("type", type);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.ifttt_list_fragment, container, false);

        TextView emptyView = (TextView) v.findViewById(R.id.empty_view);

        IFTTTDatabase database = IFTTTDatabase.getHelper(getContext());

        try
        {
            ruleList = (ArrayList<IFTTTRule>) database.getRuleList(type);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        if(ruleList.isEmpty())
        {
            emptyView.setVisibility(View.VISIBLE);
        }
        else
        {
            emptyView.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.element_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RuleAdapter adapter = new RuleAdapter(ruleList, recyclerView, options);
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
        controller.go(IFTTTRuleDetail.newInstance(new IFTTTRule("New Rule", type, null, null, null, null), 0, options));
    }
}
