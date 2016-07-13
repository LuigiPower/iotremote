package it.giuggi.iotremote.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.OnBroadcastEvent;
import it.giuggi.iotremote.iot.node.IOTNode;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class NodeDetails extends BaseFragment implements OnBroadcastEvent
{
    public static final String TAG = "NODE_DETAILS";
    private IOTNode myNode;

    public NodeDetails()
    {
        setRetainInstance(true);
        putRight();
    }

    public static NodeDetails newInstance(IOTNode node)
    {
        NodeDetails details = new NodeDetails();
        details.myNode = node;
        return details;
    }

    @Override
    public BaseFragment createFillin()
    {
        return NodeList.newInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        registerReceiver(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return myNode.mode.loadDashboard(inflater, container);
    }

    @Override
    public String generateTag()
    {
        return TAG;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void nodeUpdate(JSONObject newValues)
    {
        try
        {
            myNode.mode.valueUpdate(newValues);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
