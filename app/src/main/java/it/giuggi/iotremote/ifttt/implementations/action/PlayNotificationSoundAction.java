package it.giuggi.iotremote.ifttt.implementations.action;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import it.giuggi.iotremote.ifttt.structure.IFTTTAction;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class PlayNotificationSoundAction extends IFTTTAction
{
    private Context context;

    public PlayNotificationSoundAction(Context context)
    {

    }

    @Override
    public void doAction()
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }
}
