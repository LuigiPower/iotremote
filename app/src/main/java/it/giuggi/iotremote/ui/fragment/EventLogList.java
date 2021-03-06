package it.giuggi.iotremote.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.OnBroadcastEvent;
import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.ifttt.structure.Event;
import it.giuggi.iotremote.ifttt.ui.adapter.EventLogAdapter;

/**
 * Created by Federico Giuggioloni on 10/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class EventLogList extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnBroadcastEvent
{

    public static final String TAG = "EVENT_LOG_LIST";

    ArrayList<Event> eventList = new ArrayList<>(5);
    private EventLogAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public EventLogList()
    {
        putLeft();
    }

    public static EventLogList newInstance()
    {
        return new EventLogList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.event_log_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorFilter,
                R.color.colorEvent,
                R.color.colorContext,
                R.color.colorAction);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.event_log_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventLogAdapter(eventList, recyclerView);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        registerReceiver(this);
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.event_log, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.clear_event_log)
        {
            IFTTTDatabase database = IFTTTDatabase.getHelper(getContext());
            database.clearEventLog();
            this.eventList.clear();
            this.adapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadData()
    {
        IFTTTDatabase database = IFTTTDatabase.getHelper(getContext());

        this.eventList.clear();
        try
        {
            this.eventList.addAll(database.getEventLogList(null));
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
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

    @Override
    public void nodeUpdate(JSONObject newValues)
    {
        //onRefresh(); //Refreshing on each event can be annoying... and slow
    }
}
