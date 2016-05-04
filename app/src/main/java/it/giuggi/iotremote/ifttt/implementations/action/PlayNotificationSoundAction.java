package it.giuggi.iotremote.ifttt.implementations.action;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTAction;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class PlayNotificationSoundAction extends IFTTTAction
{

    public PlayNotificationSoundAction()
    {

    }

    @Override
    public void doAction(Context context)
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_notification_sound;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_notification_sound;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.play_notification_sound_action;
    }

    @Override
    protected void populateView(View view)
    {

    }

    @Override
    protected void populateEditView(View view)
    {

    }
}
