package it.giuggi.iotremote.ifttt.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.implementations.dummy.DummyComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTComponentDetail;
import it.giuggi.iotremote.ui.adapter.BaseViewHolder;

/**
 * Created by Federico Giuggioloni on 03/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.CustomViewHolder>
{
    private List<IFTTTComponent> componentList;
    private ViewGroup container;

    public ComponentAdapter(List<IFTTTComponent> ruleList, ViewGroup container) {
        this.componentList = ruleList;
        this.container = container;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_row, container, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        IFTTTComponent component = componentList.get(i);

        customViewHolder.card.setTag(component);
        customViewHolder.card.setOnClickListener(customViewHolder);

        View view = component.loadView(customViewHolder.componentDetails);
        //customViewHolder.componentDetails.addView(view);
    }

    @Override
    public int getItemCount() {
        return (null != componentList ? componentList.size() : 0);
    }

    public class CustomViewHolder extends BaseViewHolder implements View.OnClickListener
    {
        protected CardView card;
        protected ViewGroup componentDetails;

        public CustomViewHolder(View view) {
            super(view);

            this.card = (CardView) view.findViewById(R.id.card_view);
            this.componentDetails = (ViewGroup) view.findViewById(R.id.component_details);
        }

        @Override
        public void onClick(View v)
        {
            //TODO Maybe just a dialog?
            IFTTTComponent component = (IFTTTComponent) v.getTag();

            controller.go(IFTTTComponentDetail.newInstance(component));
        }
    }
}
