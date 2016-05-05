package it.giuggi.iotremote.ifttt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.implementations.dummy.DummyComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;
import it.giuggi.iotremote.ifttt.ui.adapter.ComponentAdapter;
import it.giuggi.iotremote.ui.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 03/05/16.
 * Shows a list of components of the given rule and type
 */
public class IFTTTComponentList extends BaseFragment
{
    public static final String TAG = "IFTTTCOMPONENTLIST";

    private IFTTTRule rule;
    private LinkedList<IFTTTComponent> originalRef = new LinkedList<>();
    private LinkedList<IFTTTComponent> componentList = new LinkedList<>();

    private ComponentAdapter adapter;

    /**
     * newInstance: Creates a new IFTTTComponentList instance
     * @param rule Rule of which to show components
     * @param componentType type of components to show
     * @return IFTTTComponentList instance ready to be put in layout
     */
    public static IFTTTComponentList newInstance(IFTTTRule rule, String componentType)
    {
        DummyComponent dummy = new DummyComponent(componentType);

        IFTTTComponentList fragment = new IFTTTComponentList();
        fragment.originalRef = rule.getComponentsOfType(componentType);
        fragment.componentList.addAll(fragment.originalRef);
        fragment.componentList.add(dummy);
        fragment.rule = rule;
        return fragment;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public String generateTag()
    {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.ifttt_list_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.element_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ComponentAdapter(rule, originalRef, componentList, recyclerView);
        recyclerView.setAdapter(adapter);

        return v;
    }
}
