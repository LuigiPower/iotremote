package it.giuggi.iotremote.ifttt.implementations.action;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    private int notificationId;
    private String message;

    public NotificationAction()
    {
        this("");
    }

    public NotificationAction(String message)
    {
        this.notificationId = notification_id;
        notification_id = notification_id + 1;
        if(notification_id >= base_notification_id + max_notifications)
        {
            notification_id = base_notification_id;
        }

        this.message = message;
    }

    @Override
    public void doAction(Context context)
    {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification n = new NotificationCompat.Builder(context)
                .setContentTitle("IoTRemote")
                .setContentText(this.message)
                .setSmallIcon(R.drawable.ic_stat_generic)
                .setSound(notification)
                .build();
        manager.notify(this.notificationId, n);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_notification;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_notification;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.notification_action;
    }

    @Override
    protected void populateView(View view)
    {
        TextView tx = (TextView) view.findViewById(R.id.notification_message);
        tx.setText(message);
    }

    @Override
    protected void populateEditView(View view)
    {
        EditText tx = (EditText) view.findViewById(R.id.notification_message);
        tx.setText(message);

        tx.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                message = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_notifications_24dp;
    }
}
