package it.giuggi.iotremote.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ui.fragment.SettingsFragment;

/**
 * Created by Federico Giuggioloni on 13/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class SettingsActivity extends AppCompatActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager().beginTransaction().add(R.id.container, SettingsFragment.newInstance()).commit();

    }
}
