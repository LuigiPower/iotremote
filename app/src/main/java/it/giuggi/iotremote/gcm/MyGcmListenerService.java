package it.giuggi.iotremote.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.MainActivity;
import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTAction;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 14/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class MyGcmListenerService extends GcmListenerService
{

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

        JSONObject jsonData = null;
        try
        {
            jsonData = new JSONObject((String) data.get("data"));
            //TODO darlo in pasto alle IFTTTRule
            Log.d(TAG, "Data from GCM is " + jsonData.toString(4));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        final JSONObject finalJsonData = jsonData;
        IFTTTCurrentSituation.acquireSnapshot(getBaseContext(), new IFTTTCurrentSituation.OnSnapshotReadyListener()
        {
            @Override
            public void onSnapshotReady(IFTTTCurrentSituation.CurrentSituation situation)
            {
                //TODO dare in pasto le IFTTTRule gia create
                //TODO togliere questo test

                Log.e(TAG, "onSnapshotReady");
                IFTTTRule rule = new IFTTTRule("rule42", null, null, null, null);

                rule.addFilter(new IFTTTFilter()
                {
                    @Override
                    public boolean apply(IOTNode node)
                    {
                        Log.e(TAG, "TESTING Filter " + this);
                        return node.name.equalsIgnoreCase("esp0");
                    }
                });

                rule.addEvent(new IFTTTEvent()
                {
                    @Override
                    public boolean apply(Event event)
                    {
                        Log.e(TAG, "TESTING Event ");
                        return event.type == IFTTTEventType.VALUE_CHANGED;
                    }
                });

                rule.addContext(new IFTTTContext()
                {
                    @Override
                    public boolean apply(IFTTTCurrentSituation.CurrentSituation context)
                    {
                        Log.e(TAG, "TESTING Context " + context.toLogString());
                        //TODO use Geofencing
                        //return context.isLocationIn(0.0, 0.0, 0.0);
                        return true;
                    }
                });

                rule.addAction(new IFTTTAction()
                {
                    @Override
                    public void doAction()
                    {
                        Log.e(TAG, "PASSED RULE");
                    }
                });

                try
                {
                    Log.e(TAG, "TESTING RULE");
                    boolean result = rule.apply(situation, finalJsonData);
                    Log.e(TAG, "Rule result: " + result);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
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