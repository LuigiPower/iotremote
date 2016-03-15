package it.giuggi.iotremote.iot;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class EmptyMode extends IOperatingMode
{
    @Override
    public int loadDashboardLayout()
    {
        return R.layout.empty_mode;
    }
}
