package it.giuggi.iotremote.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.fragment.NodeDetails;
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
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        IOTNode iotNode = iotNodeList.get(i);

        customViewHolder.nodeName.setText(iotNode.name);
        customViewHolder.nodeMode.setText(iotNode.mode.getName());

        LayoutInflater inflater = LayoutInflater.from(customViewHolder.nodeName.getContext());

        if(customViewHolder.dashboardContainer.getChildCount() > 0)
        {
            customViewHolder.dashboardContainer.removeAllViews();
        }

        customViewHolder.dashboardContainer.addView(iotNode.mode.loadDashboardLayout(inflater, customViewHolder.dashboardContainer));

        customViewHolder.card.setTag(iotNode);
        customViewHolder.card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customViewHolder.showDetailsDialog((IOTNode) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != iotNodeList ? iotNodeList.size() : 0);
    }

    public class CustomViewHolder extends BaseViewHolder {
        protected TextView nodeName;
        protected TextView nodeMode;
        protected ViewGroup dashboardContainer;
        protected CardView card;

        public CustomViewHolder(View view) {
            super(view);

            this.nodeName = (TextView) view.findViewById(R.id.node_name);
            this.nodeMode = (TextView) view.findViewById(R.id.current_mode);
            this.dashboardContainer = (ViewGroup) view.findViewById(R.id.dashboard_container);
            this.card = (CardView) view.findViewById(R.id.card_view);
        }

        public void goDetails(IOTNode node)
        {
            controller.go(NodeDetails.newInstance(node));
        }

        public void showDetailsDialog(IOTNode node)
        {
            controller.showDialog(node.loadView(LayoutInflater.from(card.getContext()), null));
        }
    }
}