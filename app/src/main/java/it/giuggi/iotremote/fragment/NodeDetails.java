package it.giuggi.iotremote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class NodeDetails extends BaseFragment
{
    public static final String TAG = "NODE_DETAILS";
    private IOTNode myNode;

    public static NodeDetails newInstance(IOTNode node)
    {
        NodeDetails details = new NodeDetails();
        details.myNode = node;
        return details;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = myNode.mode.loadDashboardLayout(inflater, container);

        return v;
    }

    @Override
    public String generateTag()
    {
        return TAG;
    }
}
