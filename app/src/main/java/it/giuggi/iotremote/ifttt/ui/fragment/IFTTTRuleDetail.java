package it.giuggi.iotremote.ifttt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
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
    private int start_page;

    private boolean[] options;

    public IFTTTRuleDetail()
    {
        putRight();
    }

    public static IFTTTRuleDetail newInstance(IFTTTRule rule, int start_page, boolean options[])
    {
        IFTTTRuleDetail fragment = new IFTTTRuleDetail();
        fragment.rule = rule;
        fragment.start_page = start_page;
        fragment.options = options;
        return fragment;
    }

    @Override
    public BaseFragment createFillin()
    {
        return IFTTTListFragment.newInstance(options, rule.getType());
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

        if(savedInstanceState != null)
        {
            if(savedInstanceState.containsKey("options"))
            {
                options = savedInstanceState.getBooleanArray("options");
            }

            long savedRule = savedInstanceState.getLong("saved_rule", -1L);
            if (savedRule != -1L)
            {
                //TODO load data from database...
                IFTTTDatabase database = IFTTTDatabase.getHelper(getContext());
                try
                {
                    rule = database.findRuleById(savedRule);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                    //Something went very wrong
                }
                finally
                {
                    if(rule == null)
                    {
                        getActivity().finish();
                    }
                }

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.ifttt_rule_detail, container, false);

        /**
         * TODO Setting images (maybe make a method inside IFTTTRule?
         * as this is needed in the adapter as well
         */
        //TODO set default (not-set) icon
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

        ImageView filter = (ImageView) v.findViewById(R.id.filter);
        ImageView event = (ImageView) v.findViewById(R.id.event);
        ImageView context = (ImageView) v.findViewById(R.id.context);
        ImageView action = (ImageView) v.findViewById(R.id.action);

        filter.setImageResource(filterImage);
        event.setImageResource(eventImage);
        context.setImageResource(contextImage);
        action.setImageResource(actionImage);

        if(!options[0])
        {
            ((View) filter.getParent()).setVisibility(View.GONE);
        }
        else
        {
            ((View) filter.getParent()).setVisibility(View.VISIBLE);
        }

        if(!options[1])
        {
            ((View) event.getParent()).setVisibility(View.GONE);
        }
        else
        {
            ((View) event.getParent()).setVisibility(View.VISIBLE);
        }

        if(!options[2])
        {
            ((View) context.getParent()).setVisibility(View.GONE);
        }
        else
        {
            ((View) context.getParent()).setVisibility(View.VISIBLE);
        }

        if(!options[3])
        {
            ((View) action.getParent()).setVisibility(View.GONE);
        }
        else
        {
            ((View) action.getParent()).setVisibility(View.VISIBLE);
        }
        /**
         * Done setting images
         ********************************************************************/

        View toShrink = v.findViewById(R.id.rule_description_container);

        pager = (ViewPager) v.findViewById(R.id.component_pager);
        pager.setAdapter(new ComponentPagerAdapter(getChildFragmentManager(), rule, options, toShrink));
        pager.setCurrentItem(start_page, true);

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
        if(rule.isValid())
        {
            if (rule.update(getContext()) < 0)
            {
                rule.save(getContext());
            }
            controller.goBack();
        }
        else
        {
            //TODO toast or something to show error (Add at least one Action!)
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong("saved_rule", rule.getRuleid());
        outState.putBooleanArray("options", options);
        outState.putInt("type", rule.getType());
    }
}
