package it.giuggi.iotremote;

import android.view.View;

import it.giuggi.iotremote.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 16/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public interface INavigationController
{
    public void goBack();
    public void go(BaseFragment in);
    public void showDialog(View view);
}
