package it.giuggi.iotremote.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ui.adapter.IOTNodeAdapter;
import it.giuggi.iotremote.iot.IOTNode;
import it.giuggi.iotremote.iot.IOperatingMode;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 10/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class NodeList extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
{

    public static final String TAG = "NODE_LIST";

    ArrayList<IOTNode> nodeList = new ArrayList<IOTNode>(5);
    private IOTNodeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static NodeList newInstance()
    {
        return new NodeList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.iot_list, container, false);

        loadData();

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.light_blue,
                R.color.light_green,
                R.color.light_orange,
                R.color.light_red);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.iot_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IOTNodeAdapter(nodeList, recyclerView);
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void loadData()
    {
        WebRequestTask.perform(WebRequestTask.Azione.GET_NODE_LIST)
                .listen(new WebRequestTask.OnResponseListener()
                {
                    @Override
                    public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
                    {
                        Log.i("Received data", "Received node list: " + ris);

                        if(ris == null)
                        {
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        nodeList.clear();

                        JSONArray arr = (JSONArray) ris;
                        for (int i = 0; i < arr.length(); i++)
                        {
                            try
                            {
                                JSONObject obj = arr.getJSONObject(i);

                                String name = obj.getString("name");
                                String ip = obj.getString("ip");
                                JSONObject mode = obj.getJSONObject("mode");
                                String modeName = mode.getString("name");

                                JSONObject modeParams = mode.getJSONObject("params");

                                IOTNode node = new IOTNode(ip, name, IOperatingMode.stringToMode(modeName, modeParams));
                                nodeList.add(node);
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        swipeRefreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                })
                .send();
    }

    @Override
    public String generateTag()
    {
        return TAG;
    }

    @Override
    public void onRefresh()
    {
        loadData();
    }
}
