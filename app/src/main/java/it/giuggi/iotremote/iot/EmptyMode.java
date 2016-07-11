package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class EmptyMode extends IOperatingMode
{
    public static final String NAME = "empty_mode";
    public static final int LOCALIZED_STRING = R.string.mode_empty;

    public EmptyMode()
    {
        super();
    }

    public EmptyMode(JSONObject params)
    {
        super(params);
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public int getLocalizedNameId()
    {
        return LOCALIZED_STRING;
    }

    @Override
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        return inflater.inflate(R.layout.empty_mode, container, false);
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
