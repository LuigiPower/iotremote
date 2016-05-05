package it.giuggi.iotremote.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTListFragment;
import it.giuggi.iotremote.ui.fragment.BaseFragment;
import it.giuggi.iotremote.ui.fragment.NodeList;

/**
 * Created by Federico Giuggioloni on 05/05/16.
 * Adapter for the drawer...
 */
public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.CustomViewHolder> {
    private List<DrawerItem> drawerItems;
    private ViewGroup container;

    public DrawerItemAdapter(List<DrawerItem> drawerItems, ViewGroup container) {
        this.drawerItems = drawerItems;
        this.container = container;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_item, container, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        DrawerItem item = drawerItems.get(i);

        customViewHolder.itemText.setText(item.name);

        customViewHolder.container.setTag(i);
        customViewHolder.container.setOnClickListener(customViewHolder);
    }

    @Override
    public int getItemCount() {
        return (null != drawerItems ? drawerItems.size() : 0);
    }

    public class CustomViewHolder extends BaseViewHolder implements View.OnClickListener
    {
        protected ViewGroup container;
        protected TextView itemText;

        public CustomViewHolder(View view) {
            super(view);

            this.container = (ViewGroup) view.findViewById(R.id.drawer_item_container);
            this.itemText = (TextView) view.findViewById(R.id.drawer_item_name);
        }

        @Override
        public void onClick(View view)
        {
            int i = (int) view.getTag();

            BaseFragment fragment;

            switch(i)
            {
                case 0:
                    fragment = NodeList.newInstance();
                    break;
                case 1:
                    fragment = IFTTTListFragment.newInstance();
                    break;
                case 2:
                    //TODO settings fragment;
                    fragment = NodeList.newInstance();
                    break;
                default:
                    fragment = NodeList.newInstance();
                    break;
            }

            controller.clearStack();
            controller.closeDrawer();
            controller.go(fragment);
        }
    }
}