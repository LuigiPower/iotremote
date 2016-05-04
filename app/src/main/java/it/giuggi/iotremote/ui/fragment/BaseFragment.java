package it.giuggi.iotremote.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import it.giuggi.iotremote.INavigationController;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Extension of Android's Fragment class
 * Once initialized from the activity that will be using these fragments,
 * allows for simple navigation tasks inside fragments
 */
public abstract class BaseFragment extends Fragment
{
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
        public void showDialog(View view)
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
