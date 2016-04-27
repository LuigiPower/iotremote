package it.giuggi.iotremote.ifttt.implementations.action;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTAction;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class NotificationAction extends IFTTTAction
{
    private static final int base_notification_id = 777;
    private static final int max_notifications = 5;
    private static int notification_id = base_notification_id;

    private transient Context context;
    private transient NotificationManager manager;

    private int notificationId;
    private String message;

    public NotificationAction(Context context, String message)
    {
        this.notificationId = notification_id;
        notification_id = notification_id + 1;
        if(notification_id >= base_notification_id + max_notifications)
        {
            notification_id = base_notification_id;
        }

        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.message = message;
    }

    @Override
    public void doAction()
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification n = new NotificationCompat.Builder(context)
                .setContentTitle("IoTRemote")
                .setContentText(this.message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(notification)
                .build();
        this.manager.notify(this.notificationId, n);
    }
}
