package it.giuggi.iotremote.ifttt.implementations.context;

import android.view.View;

import com.google.android.gms.location.DetectedActivity;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 14/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class StandStillContext extends ActivityContext
{

    @Override
    protected int[] getActivityTypes()
    {
        return new int[]{DetectedActivity.STILL};
    }

    @Override
    protected int[] getActivityConfidences()
    {
        return new int[]{DEFAULT_CONFIDENCE_THRESHOLD};
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.context_activity_still;
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_accessibility_black_24dp;
    }
}
