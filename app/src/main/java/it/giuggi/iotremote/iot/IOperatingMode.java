package it.giuggi.iotremote.iot;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IOperatingMode
{
    public String name;

    public abstract int loadDashboardLayout();
}
