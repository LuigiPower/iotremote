package it.giuggi.iotremote;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

/**
 * Created by Federico Giuggioloni on 14/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            String strinterval = preferences.getString("passive_rule_interval", null);
            long interval = strinterval != null ? Long.parseLong(strinterval) : context.getResources().getInteger(R.integer.default_passive_rule_interval);

            Intent nintent = new Intent(context, AlarmReceiver.class);
            PendingIntent operation = PendingIntent.getBroadcast(context, 0, nintent, 0);
            manager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 1000L,
                    interval,
                    operation);
        }
    }
}