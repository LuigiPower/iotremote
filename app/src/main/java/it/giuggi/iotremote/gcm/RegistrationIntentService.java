package it.giuggi.iotremote.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 14/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class RegistrationIntentService extends IntentService
{

    private static final String TAG = "REGISTRATION_INTENT_SERVICE";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RegistrationIntentService()
    {
        super(TAG);
    }

    @Override
    public void onHandleIntent(Intent intent) {

        InstanceID instanceID = InstanceID.getInstance(this);
        try
        {
            String token = instanceID.getToken(getString(R.string.sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Bundle dati = new Bundle();
            dati.putStringArray(WebRequestTask.DATA, new String[]{ token });

            WebRequestTask.perform(WebRequestTask.Azione.SEND_GCM_ID)
                    .with(dati)
                    .listen(new WebRequestTask.OnResponseListener()
                    {
                        @Override
                        public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
                        {
                            Log.i("it.giuggi.iotremote", "TOKEN SENT! " + ris);
                        }
                    })
                    .send();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}