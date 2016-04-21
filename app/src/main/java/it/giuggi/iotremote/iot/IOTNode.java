package it.giuggi.iotremote.iot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 10/03/16.
 * Class that contains all iotnode relevant data
 * Also allows a simple way to send commands to nodes
 * TODO sendCommand Method: Send command to this node on the server
 */
public class IOTNode
{
    public String ipAddress;
    public String name;
    public IOperatingMode mode;

    public IOTNode(String ipAddress, String name)
    {
        this.ipAddress = ipAddress;
        this.name = name;
        this.mode = new EmptyMode();
    }

    public IOTNode(String ipAddress, String name, IOperatingMode mode)
    {
        this(ipAddress, name);
        this.mode = mode;
    }

    public View loadView(LayoutInflater inflater, ViewGroup container)
    {
        View view = inflater.inflate(R.layout.riga_iot_node_no_card, container, false);

        TextView nodeName = (TextView) view.findViewById(R.id.node_name);
        TextView nodeMode = (TextView) view.findViewById(R.id.current_mode);
        ViewGroup dashboardContainer = (ViewGroup) view.findViewById(R.id.dashboard_container);

        nodeName.setText(name);
        nodeMode.setText(mode.getName());
        dashboardContainer.addView(mode.loadDashboardLayout(inflater, dashboardContainer));

        return view;
    }

    public void setMode(IOperatingMode newMode)
    {
        this.mode = newMode;
    }

    public boolean hasMode(String modename)
    {
        return mode.has(modename);
    }

    public void sendCommand(String action)
    {
        Bundle data = new Bundle();
        data.putStringArray(WebRequestTask.DATA, new String[]{
                action,
                name
        });

        WebRequestTask.perform(WebRequestTask.Azione.SEND_COMMAND)
                .with(data)
                .listen(new WebRequestTask.OnResponseListener()
                {
                    @Override
                    public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
                    {
                        Log.i("IOTNode", "Sent action to node");
                    }
                })
                .send();
    }
}
