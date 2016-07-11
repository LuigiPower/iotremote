package it.giuggi.iotremote.ui.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import it.giuggi.iotremote.INavigationController;
import it.giuggi.iotremote.OnBroadcastEvent;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Extension of Android's Fragment class
 * Once initialized from the activity that will be using these fragments,
 * allows for simple navigation tasks inside fragments
 */
public abstract class BaseFragment extends Fragment
{
    /**
     * Where to show the fragment on bigger devices
     * @see android.view.Gravity
     * Gravity.LEFT is the left part of the Master-Detail Flow (Lists and content selection)
     * Gravity.RIGHT is the right part of the Master-Detail Flow (Content)
     */
    protected int gravity;

    private static HashSet<OnBroadcastEvent> receivers = new HashSet<>();

    public static void registerReceiver(OnBroadcastEvent receiver)
    {
        receivers.add(receiver);
    }

    public static void unregisterReceiver(OnBroadcastEvent receiver)
    {
        receivers.remove(receiver);
    }

    public static Collection<OnBroadcastEvent> getCurrentReceivers()
    {
        return receivers;
    }

    @SuppressLint("RtlHardcoded")
    protected void putLeft()
    {
        gravity = Gravity.LEFT;
    }

    @SuppressLint("RtlHardcoded")
    protected void putRight()
    {
        gravity = Gravity.RIGHT;
    }

    public int getGravity()
    {
        return gravity;
    }

    public BaseFragment createFillin()
    {
        return null;
    }

    protected static INavigationController controller = new INavigationController()
    {
        @Override
        public void goBack()
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

    public abstract String generateTag();
}
