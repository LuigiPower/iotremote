package it.giuggi.iotremote.ifttt.implementations.context;

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
}
