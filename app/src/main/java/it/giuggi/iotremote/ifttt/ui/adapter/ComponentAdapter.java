package it.giuggi.iotremote.ifttt.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.implementations.dummy.DummyComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTComponentDetail;
import it.giuggi.iotremote.ui.adapter.BaseViewHolder;

/**
 * Created by Federico Giuggioloni on 03/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.CustomViewHolder>
{
    private IFTTTRule owner;
    private List<IFTTTComponent> originalRef;
    private List<IFTTTComponent> componentList;
    private ViewGroup container;

    public ComponentAdapter(IFTTTRule owner, List<IFTTTComponent> originalRef, List<IFTTTComponent> componentList, ViewGroup container) {
        this.owner = owner;
        this.componentList = componentList;
        this.originalRef = originalRef;
        this.container = container;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_row, container, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onViewRecycled(CustomViewHolder holder)
    {
        super.onViewRecycled(holder);

        int adapterPosition = holder.getAdapterPosition();
        // This check is required in case a component is deleted (index would be out of range)
        if(adapterPosition > 0 && adapterPosition < componentList.size())
        {
            IFTTTComponent component = componentList.get(adapterPosition);
            component.doCleanup(holder.componentDetails);
        }
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        IFTTTComponent component = componentList.get(i);

        customViewHolder.editButton.setTag(component);
        customViewHolder.editButton.setOnClickListener(customViewHolder);

        customViewHolder.deleteButton.setTag(component);
        customViewHolder.deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                IFTTTComponent component = (IFTTTComponent) v.getTag();
                componentList.remove(component);
                originalRef.remove(component);
                component.delete(v.getContext());

                notifyDataSetChanged();
            }
        });

        customViewHolder.componentToolbar.setTitle(component.getComponentName(customViewHolder.componentToolbar.getContext()));
        customViewHolder.componentToolbar.setBackgroundResource(component.getColorId());

        if(component instanceof DummyComponent)
        {
            customViewHolder.deleteButton.setVisibility(View.GONE);
            customViewHolder.scroll.setVisibility(View.GONE);
            customViewHolder.editButton.setText(R.string.add_component);
        }
        else
        {
            customViewHolder.deleteButton.setVisibility(View.VISIBLE);
            customViewHolder.scroll.setVisibility(View.VISIBLE);
            customViewHolder.editButton.setText(R.string.edit);
        }

        component.loadView(customViewHolder.componentDetails);
    }

    @Override
    public int getItemCount() {
        return (null != componentList ? componentList.size() : 0);
    }

    public class CustomViewHolder extends BaseViewHolder implements View.OnClickListener
    {
        protected CardView card;
        protected ScrollView scroll;
        protected Button editButton;
        protected Button deleteButton;
        //protected TextView componentName;
        protected Toolbar componentToolbar;
        protected ViewGroup componentDetails;

        public CustomViewHolder(View view) {
            super(view);

            this.card = (CardView) view.findViewById(R.id.card_view);
            this.scroll = (ScrollView) view.findViewById(R.id.component_scroll);
            this.editButton = (Button) view.findViewById(R.id.edit_button);
            this.deleteButton = (Button) view.findViewById(R.id.delete_button);
            this.componentToolbar = (Toolbar) view.findViewById(R.id.component_toolbar);
            this.componentDetails = (ViewGroup) view.findViewById(R.id.component_details);
        }

        @Override
        public void onClick(View v)
        {
            IFTTTComponent component = (IFTTTComponent) v.getTag();

            controller.go(IFTTTComponentDetail.newInstance(componentList, owner, component));
        }
    }
}
