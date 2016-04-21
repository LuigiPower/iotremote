package it.giuggi.iotremote.iot;

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
import it.giuggi.iotremote.adapter.IOperatingModeAdapter;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class CompositeMode extends IOperatingMode
{
    public static final String NAME = "composite_mode";

    private ArrayList<IOperatingMode> modeList = new ArrayList<>(3);
    private IOperatingModeAdapter adapter;

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
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        View v = inflater.inflate(R.layout.composite_mode, container, false);

        RecyclerView list = (RecyclerView) v.findViewById(R.id.mode_list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        adapter = new IOperatingModeAdapter(modeList, list);
        list.setAdapter(adapter);

        return v;
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
