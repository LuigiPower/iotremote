package it.giuggi.iotremote.ifttt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;
import it.giuggi.iotremote.ifttt.ui.adapter.ComponentPagerAdapter;
import it.giuggi.iotremote.ui.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 03/05/16.
 * Fragment that holds all info about a rule
 * Should have 4 tabs with a viewpager, with these titles (translated):
 * FILTERS  EVENTS  CONTEXTS    ACTIONS
 * Each of which has an IFTTTComponentList, which allows adding/removing of components to this rule
 */
public class IFTTTRuleDetail extends BaseFragment implements View.OnClickListener
{
    public static final String TAG = "IFTTTRULEDETAIL";

    private IFTTTRule rule;
    private ViewPager pager;

    public static IFTTTRuleDetail newInstance(IFTTTRule rule)
    {
        IFTTTRuleDetail fragment = new IFTTTRuleDetail();
        fragment.rule = rule;
        return fragment;
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
        View v = inflater.inflate(R.layout.ifttt_rule_detail, container, false);

        pager = (ViewPager) v.findViewById(R.id.component_pager);
        pager.setAdapter(new ComponentPagerAdapter(getChildFragmentManager(), rule));

        View completeRule = v.findViewById(R.id.complete_rule);
        completeRule.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v)
    {
        //Simply saves this rule to permanent storage
        //if id was already set, overwrites the already existing one
        //else it saves a new rule
        if(this.rule.update(getContext()) < 0)
        {
            this.rule.save(getContext());
        }
    }
}
