package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class UnknownMode extends IOperatingMode
{
    public static final String NAME = "unknown";
    public static final int LOCALIZED_STRING = R.string.mode_unknown;

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
    public int getLocalizedNameId()
    {
        return LOCALIZED_STRING;
    }

    @Override
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        return inflater.inflate(R.layout.unknown_mode, container, false);
    }

    @Override
    public void destroyDashboardLayout(ViewGroup container)
    {

    }

    @Override
    public void valueUpdate(JSONObject newParameters) throws JSONException
    {

    }
}
