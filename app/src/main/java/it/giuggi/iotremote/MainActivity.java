package it.giuggi.iotremote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import it.giuggi.iotremote.ui.fragment.BaseFragment;
import it.giuggi.iotremote.ui.adapter.BaseViewHolder;
import it.giuggi.iotremote.ui.fragment.NodeList;
import it.giuggi.iotremote.gcm.RegistrationIntentService;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTListFragment;

public class MainActivity extends AppCompatActivity implements INavigationController
{

    private static final String TAG = "IOT_REMOTE_APP";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int PERMISSIONS_CHECKED_REQUEST = 9001;

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

        BaseFragment.initNavigation(this);
        BaseViewHolder.initNavigation(this);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Log.i("it.giuggi.iotremote", "play services checked, starting registration intent service");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 4);
            }
            return;
        }

        changeFragment(new IFTTTListFragment());
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

    @Override
    public void goBack()
    {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void go(BaseFragment in)
    {
        changeFragment(in);
    }

    @Override
    public void showDialog(View view)
    {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == PERMISSIONS_CHECKED_REQUEST)
        {
            for(int code : grantResults)
            {
                if (code != RESULT_OK)
                {
                    //OK permissions, keep going
                } else
                {
                    //Close application, we need permissions TODO maybe start anyway and disable IFTTT rules that use these permissions
                    finish();
                }
            }
            changeFragment(new NodeList());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
