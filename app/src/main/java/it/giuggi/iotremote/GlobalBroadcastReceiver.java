package it.giuggi.iotremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import it.giuggi.iotremote.ui.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 11/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class GlobalBroadcastReceiver extends BroadcastReceiver
{
    public static final String NODE_UPDATE = "it.giuggi.iotremote.action.NODE_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        Log.d("onReceive", "RECEIVED A BROADCAST " + action);

        if (NODE_UPDATE.equalsIgnoreCase(action))
        {
            Bundle extras = intent.getExtras();
            JSONObject jsonData = new JSONObject();

            try
            {
                JSONObject data = new JSONObject(extras.getString("data"));

                String nodestring = data.getString("node");
                String eventstring = data.getString("event");

                jsonData.put("node", new JSONObject(nodestring));
                jsonData.put("event", new JSONObject(eventstring));

                Log.d("GlobalBroadcast", "Data from Broadcast is " + jsonData.toString(4));

                Collection<OnBroadcastEvent> list = BaseFragment.getCurrentReceivers();
                for(OnBroadcastEvent event : list)
                {
                    event.nodeUpdate(data);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}