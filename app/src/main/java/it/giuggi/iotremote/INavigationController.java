package it.giuggi.iotremote;

import android.view.View;

import it.giuggi.iotremote.ui.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * INavigationController
 * If implemented, exposes simple navigation methods
 */
public interface INavigationController
{
    public void goBack();
    public void go(BaseFragment in);
    public void go(BaseFragment in, boolean backstack);
    public void showDialog(View view);

    public void clearStack();

    public void closeDrawer();
    public void openDrawer();
}
