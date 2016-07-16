package it.giuggi.iotremote.ifttt.ui.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import it.giuggi.iotremote.ifttt.database.IFTTTContract;
import it.giuggi.iotremote.ifttt.structure.IFTTTAction;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTComponentList;

/**
 * Created by Federico Giuggioloni on 03/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
@SuppressLint("CommitTransaction")
public class ComponentPagerAdapter extends PagerAdapter
{
    private FragmentManager fragmentManager;
    private FragmentTransaction currentTransaction;
    private IFTTTRule rule;
    private View toShrink;
    private boolean[] options;
    private String[] defaultMapping = new String[]{
            IFTTTFilter.TYPE,
            IFTTTEvent.TYPE,
            IFTTTContext.TYPE,
            IFTTTAction.TYPE
    };
    private String[] mapping;
    private SparseArray<Fragment> fragments;

    public ComponentPagerAdapter(FragmentManager fm, IFTTTRule rule, boolean[] options, View toShrink)
    {
        super();
        this.fragmentManager = fm;
        this.rule = rule;
        this.toShrink = toShrink;
        this.options = options;
        this.fragments = new SparseArray<>();

        int count = 0;
        for(boolean bool : options)
        {
            if(bool)
            {
                count++;
            }
        }

        this.mapping = new String[count];

        int current = 0;
        for (int i = 0; i < options.length; i++)
        {
            boolean bool = options[i];
            if (bool)
            {
                mapping[current] = defaultMapping[i];
                current++;
            }
        }


        /*
        this.fragments = new Fragment[4];
        this.fragments[0] = IFTTTComponentList.newInstance(rule, IFTTTFilter.TYPE, toShrink);
        this.fragments[1] = IFTTTComponentList.newInstance(rule, IFTTTEvent.TYPE, toShrink);
        this.fragments[2] = IFTTTComponentList.newInstance(rule, IFTTTEvent.TYPE, toShrink);
        this.fragments[3] = IFTTTComponentList.newInstance(rule, IFTTTAction.TYPE, toShrink);
        */
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = getItem(position);
        if (currentTransaction == null)
        {
            currentTransaction = fragmentManager.beginTransaction();
        }
        currentTransaction.add(container.getId(), fragment, "fragment:"+position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (currentTransaction == null) {
            currentTransaction = fragmentManager.beginTransaction();
        }
        currentTransaction.detach(fragments.get(position));
        fragments.remove(position);
    }

    public Fragment getItem(int position)
    {
        return IFTTTComponentList.newInstance(rule, mapping[position], toShrink);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (currentTransaction != null) {
            currentTransaction.commitAllowingStateLoss();
            currentTransaction = null;
            fragmentManager.executePendingTransactions();
        }
    }

    @Override
    public int getCount()
    {
        int count = 0;
        for(boolean bool : options)
        {
            if(bool)
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String type;
        switch(position)
        {
            case 0:
                type = IFTTTFilter.TYPE;
                break;
            case 1:
                type = IFTTTEvent.TYPE;
                break;
            case 2:
                type = IFTTTContext.TYPE;
                break;
            case 3:
                type = IFTTTAction.TYPE;
                break;
            default:
                return null;
        }

        //TODO change title... (with string from R.string)
        return type;
    }
}
