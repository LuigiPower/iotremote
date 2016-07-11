package it.giuggi.iotremote.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 05/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class BasicMode extends IOperatingMode
{
    public static final String NAME = "basic_mode";
    public static final int LOCALIZED_STRING = R.string.mode_basic;

    public BasicMode()
    {
        super();
    }

    public BasicMode(JSONObject params)
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
        //TODO this should be 'settings' kind of mode (change name and such)
        return new View(container.getContext());
    }

    @Override
    public void destroyDashboardLayout(ViewGroup container)
    {

    }

    @Override
    public void valueUpdate(JSONObject newParameters) throws JSONException
    {
        //TODO this shouldn't be needed for basicmode, or use this to register configuration changes
    }
}
