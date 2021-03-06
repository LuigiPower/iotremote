package it.giuggi.iotremote.iot.mode;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.iot.node.IOTNode;
import it.giuggi.iotremote.ui.adapter.IOperatingModeAdapter;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class CompositeMode extends IOperatingMode
{
    transient public static final String NAME = "composite_mode";
    transient public static final int LOCALIZED_STRING = R.string.mode_composite;

    private ArrayList<IOperatingMode> modeList = new ArrayList<>(3);

    public CompositeMode()
    {
        super();
    }

    public CompositeMode(JSONObject params)
    {
        super(params);

        try
        {
            JSONArray modeArray = params.getJSONArray("modes");

            for(int i = 0; i < modeArray.length(); i++)
            {
                JSONObject mode = modeArray.getJSONObject(i);

                String modeName = mode.getString("name");
                JSONObject modeParams = mode.getJSONObject("params");

                IOperatingMode newmode = IOperatingMode.stringToMode(modeName, modeParams);
                modeList.add(newmode);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setOwner(IOTNode node)
    {
        super.setOwner(node);
        for(IOperatingMode mode : modeList)
        {
            mode.setOwner(node);
        }
    }

    @Override
    public boolean has(String modename)
    {
        for(IOperatingMode mode : modeList)
        {
            if(mode.has(modename))
            {
                return true;
            }
        }
        return false;
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
        View v = inflater.inflate(R.layout.composite_mode, container, false);

        RecyclerView list = (RecyclerView) v.findViewById(R.id.mode_list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        IOperatingModeAdapter adapter = new IOperatingModeAdapter(modeList, list);
        list.setAdapter(adapter);

        return v;
    }

    @Override
    public void destroyDashboardLayout(ViewGroup container)
    {

    }

    @Override
    public void valueUpdate(JSONObject newParameters) throws JSONException
    {
        JSONArray modeArray = newParameters.getJSONArray("modes");

        for(int i = 0; i < modeArray.length(); i++)
        {
            JSONObject mode = modeArray.getJSONObject(i);

            String modeName = mode.getString("name");
            JSONObject modeParams = mode.getJSONObject("params");

            for(IOperatingMode opmode : modeList)
            {
                if(opmode.getName().equalsIgnoreCase(modeName))
                {
                    opmode.valueUpdate(modeParams);
                }
            }

        }
        //TODO remove any mode I didn't find in the new parameters
    }

    public void addMode(IOperatingMode mode)
    {
        modeList.add(mode);
    }

    public void removeModeAt(int i)
    {
        modeList.remove(i);
    }

    public IOperatingMode getModeAt(int i)
    {
        return modeList.get(i);
    }
}
