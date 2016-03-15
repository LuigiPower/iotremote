package it.giuggi.iotremote.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class IOTNodeAdapter extends RecyclerView.Adapter<IOTNodeAdapter.CustomViewHolder> {
    private List<IOTNode> iotNodeList;
    private ViewGroup container;

    public IOTNodeAdapter(List<IOTNode> iotNodeList, ViewGroup container) {
        this.iotNodeList = iotNodeList;
        this.container = container;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.riga_iot_node, container, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        IOTNode iotNode = iotNodeList.get(i);

        customViewHolder.nodeName.setText(iotNode.name);
        customViewHolder.nodeMode.setText(iotNode.mode.name);

        LayoutInflater inflater = LayoutInflater.from(customViewHolder.nodeName.getContext());

        if(customViewHolder.dashboardContainer.getChildCount() > 0)
        {
            customViewHolder.dashboardContainer.removeAllViews();
        }

        customViewHolder.dashboardContainer.addView(inflater.inflate(iotNode.mode.loadDashboardLayout(), customViewHolder.dashboardContainer, false));
    }

    @Override
    public int getItemCount() {
        return (null != iotNodeList ? iotNodeList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView nodeName;
        protected TextView nodeMode;
        protected ViewGroup dashboardContainer;

        public CustomViewHolder(View view) {
            super(view);

            this.nodeName = (TextView) view.findViewById(R.id.node_name);
            this.nodeMode = (TextView) view.findViewById(R.id.current_mode);
            this.dashboardContainer = (ViewGroup) view.findViewById(R.id.dashboard_container);
        }
    }
}