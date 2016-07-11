package it.giuggi.iotremote.gcm;

import com.google.android.gms.common.api.GoogleApiClient;

import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;

/**
 * Created by Federico Giuggioloni on 11/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public interface ActivityRecognitionHandler
{
    GoogleApiClient getActivityRecognition(IFTTTCurrentSituation situation);
    void connect();
    void registerCallbacks(GoogleApiClient.ConnectionCallbacks callbacks);
    void registerFailureCallbacks(GoogleApiClient.OnConnectionFailedListener callbacks);
    void unregisterCallbacks(GoogleApiClient.ConnectionCallbacks callbacks);
    void unregisterFailureCallbacks(GoogleApiClient.OnConnectionFailedListener callbacks);
}
