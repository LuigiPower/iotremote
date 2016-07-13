package it.giuggi.iotremote;

import org.json.JSONObject;

import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;

/**
 * Created by Federico Giuggioloni on 11/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public interface OnBroadcastEvent
{
    void nodeUpdate(JSONObject newValues);
}
