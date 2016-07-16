package it.giuggi.iotremote;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled = preferences.getBoolean("passive_rules_enabled", true);

        if(!enabled)
        {
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent nintent = new Intent(context, AlarmReceiver.class);
            PendingIntent operation = PendingIntent.getBroadcast(context, 0, nintent, PendingIntent.FLAG_NO_CREATE);
            if(operation == null)
            {
                operation = PendingIntent.getBroadcast(context, 0, nintent, 0);
                manager.cancel(operation);
            }
            return;
        }

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
