package it.giuggi.iotremote.ifttt.implementations.context;

import android.view.View;

import com.google.android.gms.location.DetectedActivity;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;

/**
 * Created by Federico Giuggioloni on 14/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class ActivityContext extends IFTTTContext
{
    /**
     * Activity types to look for. One having confidence > CONFIDENCE_THRESHOLD will trigger true
     * @return int array containing Activity types to look for
     */
    protected abstract int[] getActivityTypes();

    /**
     * Confidence levels required to successfully apply this component
     * @return array of integers from 0 to 100 that represent the confidence percentage required
     */
    protected abstract int[] getActivityConfidences();

    @Override
    public boolean apply(IFTTTCurrentSituation.CurrentSituation context)
    {
        int[] types = getActivityTypes();
        int[] confidences = getActivityConfidences();
        for(DetectedActivity activity : context.currentActivities)
        {
            for (int i = 0; i < types.length; i++)
            {
                int type = types[i];
                if (activity.getType() == type && activity.getConfidence() > confidences[i])
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_empty;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.detail_empty;
    }

    @Override
    protected void populateView(View view)
    {

    }

    @Override
    protected void populateEditView(View view)
    {

    }
}
