package it.giuggi.iotremote.ifttt.implementations.context;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * WifiNameContext checks if the phone is currently connected to the specified WiFi network
 */
public class WifiNameContext extends IFTTTContext
{
    private String ssid;

    public WifiNameContext(String ssid)
    {
        this.ssid = ssid;
    }

    @Override
    public boolean apply(IFTTTCurrentSituation.CurrentSituation context)
    {
        return context.isConnectedTo(this.ssid);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_wifi_name;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_wifi_name;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.wifi_name_context;
    }

    @Override
    protected void populateView(View view)
    {
        TextView wifiname = (TextView) view.findViewById(R.id.wifi_name);
        wifiname.setText(this.ssid);
    }

    @Override
    protected void populateEditView(View view)
    {
        EditText wifiname = (EditText) view.findViewById(R.id.wifi_name);
        wifiname.setText(this.ssid);
    }
}
