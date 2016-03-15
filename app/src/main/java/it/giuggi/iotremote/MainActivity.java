package it.giuggi.iotremote;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import it.giuggi.iotremote.fragment.BaseFragment;
import it.giuggi.iotremote.fragment.NodeList;
import it.giuggi.iotremote.gcm.RegistrationIntentService;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "IOT_REMOTE_APP";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private int fragmentContainer = R.id.fragment_container;

    //GCM api key
    //AIzaSyAh1LQr0p_0qB6b4RKhrMr_nxPtjxZfqiI
    //GCM sender
    //224490332382

    private void changeFragment(BaseFragment in)
    {
        Log.i("it.giuggi.iotremote", "CHANGING FRAGMENT TO " + in.generateTag());

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(fragmentContainer, in, in.generateTag())
                .addToBackStack(in.generateTag())
                //TODO animations?
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Log.i("it.giuggi.iotremote", "play services checked, starting registration intent service");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        changeFragment(new NodeList());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
