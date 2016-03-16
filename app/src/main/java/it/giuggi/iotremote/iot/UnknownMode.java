package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class UnknownMode extends IOperatingMode
{
    public UnknownMode()
    {
        super();
    }

    public UnknownMode(JSONObject params)
    {
        super(params);
    }

    @Override
    public String getName()
    {
        return "unknown";
    }

    @Override
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        return inflater.inflate(R.layout.unknown_mode, container, false);
    }
}
