package it.giuggi.iotremote.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.iot.IOperatingMode;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class IOperatingModeAdapter extends RecyclerView.Adapter<IOperatingModeAdapter.CustomViewHolder> {
    private List<IOperatingMode> IOperatingModeList;
    private ViewGroup container;

    public IOperatingModeAdapter(List<IOperatingMode> IOperatingModeList, ViewGroup container) {
        this.IOperatingModeList = IOperatingModeList;
        this.container = container;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.riga_iot_mode, container, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        IOperatingMode mode = IOperatingModeList.get(i);

        LayoutInflater inflater = LayoutInflater.from(customViewHolder.modeContainer.getContext());

        customViewHolder.modeContainer.addView(mode.loadDashboardLayout(inflater, customViewHolder.modeContainer));
    }

    @Override
    public int getItemCount() {
        return (null != IOperatingModeList ? IOperatingModeList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ViewGroup modeContainer;

        public CustomViewHolder(View view) {
            super(view);

            this.modeContainer = (ViewGroup) view.findViewById(R.id.mode_container);
        }
    }
}