package it.giuggi.iotremote.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import it.giuggi.iotremote.INavigationController;
import it.giuggi.iotremote.ui.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder
{
    protected static INavigationController controller = new INavigationController()
    {
        @Override
        public void goBack()
        {
            //NOOP
        }

        @Override
        public void go(Intent intent)
        {
            //NOOP
        }

        @Override
        public void go(BaseFragment in)
        {
            //NOOP
        }

        @Override
        public void go(BaseFragment in, boolean backstack)
        {
            //NOOP
        }

        @Override
        public void go(BaseFragment in, boolean backstack, boolean clearRight)
        {
            //NOOP
        }

        @Override
        public void showDialog(View view)
        {
            //NOOP
        }

        @Override
        public void clearStack()
        {
            //NOOP
        }

        @Override
        public void closeDrawer()
        {
            //NOOP
        }

        @Override
        public void openDrawer()
        {
            //NOOP
        }
    };

    public static void initNavigation(INavigationController nav)
    {
        controller = nav;
    }

    public BaseViewHolder(View itemView)
    {
        super(itemView);
    }
}
