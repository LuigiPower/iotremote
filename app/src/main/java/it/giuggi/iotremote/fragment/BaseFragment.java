package it.giuggi.iotremote.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import it.giuggi.iotremote.INavigationController;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
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
