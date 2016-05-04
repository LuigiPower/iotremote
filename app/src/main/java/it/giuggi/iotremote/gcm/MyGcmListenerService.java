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

import java.util.ArrayList;

import it.giuggi.iotremote.MainActivity;
import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;

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


        IFTTTDatabase database = IFTTTDatabase.getHelper(getBaseContext());
        ArrayList<IFTTTRule> rules = null;

        try
        {
            rules = (ArrayList<IFTTTRule>) database.getRuleList();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        final ArrayList<IFTTTRule> finalRules = rules;
        final JSONObject finalJsonData = jsonData;

        IFTTTCurrentSituation.acquireSnapshot(getBaseContext(), new IFTTTCurrentSituation.OnSnapshotReadyListener()
        {
            @Override
            public void onSnapshotReady(IFTTTCurrentSituation.CurrentSituation situation)
            {
                Log.i(TAG, "Starting rule check with " + situation);

                for(IFTTTRule rule : finalRules)
                {
                    Log.i(TAG, "checking rule " + rule);

                    try
                    {
                        rule.apply(situation, finalJsonData, MyGcmListenerService.this);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
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
        //sendNotification(message);
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