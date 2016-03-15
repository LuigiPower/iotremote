package it.giuggi.iotremote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.adapter.IOTNodeAdapter;
import it.giuggi.iotremote.iot.GPIOMode;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 10/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class NodeList extends BaseFragment
{

    public static final String TAG = "NODE_LIST";

    ArrayList<IOTNode> nodeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.iot_list, container, false);

        nodeList = new ArrayList<IOTNode>(5);
        nodeList.add(new IOTNode("Sono un nodo base"));
        nodeList.add(new IOTNode("Sono un nodo base", new GPIOMode()));
        nodeList.add(new IOTNode("Sono un nodo base HEEEEYOOOO"));
        nodeList.add(new IOTNode("Sono un nodo base", new GPIOMode()));
        nodeList.add(new IOTNode("Sono un nodo base", new GPIOMode()));

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.iot_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        IOTNodeAdapter adapter = new IOTNodeAdapter(nodeList, recyclerView);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public String generateTag()
    {
        return TAG;
    }
}
