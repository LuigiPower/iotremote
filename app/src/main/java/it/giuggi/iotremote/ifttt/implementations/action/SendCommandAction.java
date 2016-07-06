package it.giuggi.iotremote.ifttt.implementations.action;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTAction;
import it.giuggi.iotremote.iot.IOTNode;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class SendCommandAction extends IFTTTAction
{
    private IOTNode target;
    private String action;

    public SendCommandAction()
    {
        this(new IOTNode("", ""), "");
    }

    public SendCommandAction(IOTNode target, String action)
    {
        this.target = target;
        this.action = action;
    }

    @Override
    public void doAction(Context context)
    {
        this.target.sendCommand(action, new WebRequestTask.OnResponseListener()
        {
            @Override
            public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
            {
                //Noop?
            }
        });
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_send_command;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_send_command;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.send_command_action;
    }

    @Override
    protected void populateView(View view)
    {
        //TODO better layout
        TextView tx = (TextView) view.findViewById(R.id.command_name);
        tx.setText(this.action);
    }

    @Override
    protected void populateEditView(View view)
    {
        //TODO better layout
        EditText tx = (EditText) view.findViewById(R.id.command_name);
        tx.setText(this.action);
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_backup_24dp;
    }
}
