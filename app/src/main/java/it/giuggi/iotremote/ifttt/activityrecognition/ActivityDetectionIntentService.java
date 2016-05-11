package it.giuggi.iotremote.ifttt.activityrecognition;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by Federico Giuggioloni on 11/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class ActivityDetectionIntentService extends IntentService
{
    public static final String TAG = "ACTIVITYDETECTION";

    public static final String BROADCAST_ACTION = "it.giuggi.iotremote.ifttt.activityrecognition.ACTIVITY_DETECTED";
    public static final String ACTIVITY_EXTRA = "it.giuggi.iotremote.ifttt.activityrecognition.DETECTED_ACTIVITIES";

    public ActivityDetectionIntentService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(BROADCAST_ACTION);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList<DetectedActivity>) result.getProbableActivities();

        // Log each activity.
        Log.i(TAG, "activities detected");
        for (DetectedActivity da: detectedActivities)
        {
            Log.i(TAG, da.getType() + " " + da.getConfidence() + "%");
        }

        // Broadcast the list of detected activities.
        localIntent.putExtra(ACTIVITY_EXTRA, detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
