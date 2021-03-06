package it.giuggi.iotremote.iot.node;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.iot.mode.EmptyMode;
import it.giuggi.iotremote.iot.mode.IOperatingMode;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 10/03/16.
 * Class that contains all iotnode relevant data
 * Also allows a simple way to send commands to nodes
 */
public class IOTNode
{
    public String ipAddress;
    public String name;
    transient public IOperatingMode mode;

    public static IOTNode fromJSON(JSONObject obj) throws JSONException
    {
        String name = obj.getString("name");
        String ip = obj.getString("ip");
        JSONObject mode = obj.getJSONObject("mode");
        String modeName = mode.getString("name");

        JSONObject modeParams = mode.getJSONObject("params");

        return new IOTNode(ip, name, IOperatingMode.stringToMode(modeName, modeParams));
    }

    public IOTNode(String ipAddress, String name)
    {
        this.ipAddress = ipAddress;
        this.name = name;
        setMode(new EmptyMode());
    }

    public IOTNode(String ipAddress, String name, IOperatingMode mode)
    {
        this(ipAddress, name);
        setMode(mode);
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
        this.mode.setOwner(this);
    }

    public boolean hasMode(String modename)
    {
        return mode.has(modename);
    }

    public void sendCommand(String action, WebRequestTask.OnResponseListener responseListener)
    {
        Bundle data = new Bundle();
        data.putStringArray(WebRequestTask.DATA, new String[]{
                action,
                name
        });

        WebRequestTask.perform(WebRequestTask.Azione.SEND_TEST_COMMAND)
                .with(data)
                .listen(responseListener)
                .send();
    }
}
