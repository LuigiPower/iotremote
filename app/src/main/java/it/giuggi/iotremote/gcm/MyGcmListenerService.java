package it.giuggi.iotremote.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.GlobalBroadcastReceiver;
import it.giuggi.iotremote.MainActivity;
import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.ifttt.structure.Event;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;
import it.giuggi.iotremote.iot.node.IOTNode;

/**
 * Created by Federico Giuggioloni on 14/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class MyGcmListenerService extends GcmListenerService
{

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        JSONObject jsonData = new JSONObject();
        try
        {
            String nodestring = data.getString("node");
            String eventstring = data.getString("event");

            jsonData.put("node", new JSONObject(nodestring));
            jsonData.put("event", new JSONObject(eventstring));

            //Log.d(TAG, "Data from GCM is " + jsonData.toString(4));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }


        IFTTTDatabase database = IFTTTDatabase.getHelper(getBaseContext());
        ArrayList<IFTTTRule> rules;

        try
        {
            rules = (ArrayList<IFTTTRule>) database.getRuleList(IFTTTRule.RULE_TYPE_ACTIVE);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return; //Return, we got bigger problems than an app crashing if it doesn't find the required class...
        }

        final ArrayList<IFTTTRule> finalRules = rules;

        final Event event;

        try
        {
            JSONObject eventJson = jsonData.getJSONObject("event");
            event = new Event(jsonData);
        }
        catch(JSONException ex)
        {
            ex.printStackTrace();
            return;
        }

        // Acquire the current context and apply the rules
        IFTTTCurrentSituation.acquireSnapshot(getBaseContext(), new IFTTTCurrentSituation.OnSnapshotReadyListener()
        {
            @Override
            public void onSnapshotReady(IFTTTCurrentSituation.CurrentSituation situation)
            {
                event.snapshot = situation;
                event.save(getBaseContext());

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean enabled = preferences.getBoolean("active_rules_enabled", true);

                if(!enabled)
                {
                    //TODO first log the event, then throw the event away
                    return;
                }

                for(IFTTTRule rule : finalRules)
                {
                    try
                    {
                        rule.apply(situation, event, MyGcmListenerService.this);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        //TODO check the event type first
        Intent broadcast = new Intent(GlobalBroadcastReceiver.NODE_UPDATE);
        broadcast.putExtra("data", jsonData.toString());
        sendBroadcast(broadcast);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}