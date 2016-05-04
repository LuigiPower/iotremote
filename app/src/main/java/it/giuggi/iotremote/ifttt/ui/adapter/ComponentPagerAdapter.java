package it.giuggi.iotremote.ifttt.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
public class ComponentPagerAdapter extends FragmentStatePagerAdapter
{
    IFTTTRule rule;

    public ComponentPagerAdapter(FragmentManager fm, IFTTTRule rule)
    {
        super(fm);
        this.rule = rule;
    }

    @Override
    public Fragment getItem(int position)
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

        return IFTTTComponentList.newInstance(rule, type);
    }

    @Override
    public int getCount()
    {
        return 4;
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
