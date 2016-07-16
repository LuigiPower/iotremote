package it.giuggi.iotremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;

/**
 * Created by Federico Giuggioloni on 14/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        IFTTTDatabase database = IFTTTDatabase.getHelper(context);
        ArrayList<IFTTTRule> rules;

        try
        {
            rules = (ArrayList<IFTTTRule>) database.getRuleList(IFTTTRule.RULE_TYPE_PASSIVE);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return; //Return, we got bigger problems than an app crashing if it doesn't find the required class...
        }

        final ArrayList<IFTTTRule> finalRules = rules;

        // Acquire the current context and apply the rules
        IFTTTCurrentSituation.acquireSnapshot(context, new IFTTTCurrentSituation.OnSnapshotReadyListener()
        {
            @Override
            public void onSnapshotReady(IFTTTCurrentSituation.CurrentSituation situation)
            {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean enabled = preferences.getBoolean("passive_rules_enabled", true);

                if(!enabled)
                {
                    return;
                }

                for(IFTTTRule rule : finalRules)
                {
                    try
                    {
                        rule.apply(situation, null, context);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
