package it.giuggi.iotremote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import it.giuggi.iotremote.gcm.RegistrationIntentService;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.ifttt.implementations.context.LocationContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTListFragment;
import it.giuggi.iotremote.ifttt.ui.fragment.IFTTTRuleDetail;
import it.giuggi.iotremote.ui.adapter.BaseViewHolder;
import it.giuggi.iotremote.ui.adapter.DrawerItem;
import it.giuggi.iotremote.ui.adapter.DrawerItemAdapter;
import it.giuggi.iotremote.ui.fragment.BaseFragment;
import it.giuggi.iotremote.ui.fragment.NodeDetails;
import it.giuggi.iotremote.ui.fragment.NodeList;

public class MainActivity extends AppCompatActivity implements INavigationController
{

    private static final String TAG = "IOT_REMOTE_APP";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int PERMISSIONS_CHECKED_REQUEST = 9001;

    private static final String CURRENT_FRAGMENT_TAG = "current_fragment_tag";
    private static final String CURRENT_FRAGMENT_TAG_LEFT = "current_fragment_tag_left";
    private static final String CURRENT_FRAGMENT_TAG_RIGHT = "current_fragment_tag_right";

    @SuppressWarnings("FieldCanBeLocal")
    private int fragmentContainer = R.id.fragment_container;
    @SuppressWarnings("FieldCanBeLocal")
    private int fragmentContainerLeft = R.id.fragment_container_left;
    @SuppressWarnings("FieldCanBeLocal")
    private int fragmentContainerRight = R.id.fragment_container;

    private boolean isMasterDetail = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private String currentFragmentTag;
    private String currentFragmentTagLeft;
    private String currentFragmentTagRight;

    //GCM api key
    //AIzaSyAh1LQr0p_0qB6b4RKhrMr_nxPtjxZfqiI
    //GCM sender
    //224490332382

    @SuppressWarnings("ResourceType")
    @SuppressLint({"CommitTransaction", "RtlHardcoded"})
    private void changeFragment(BaseFragment in, boolean backstack)
    {
        Log.i("Fillin creation...", "Showing fragment " + in + " master: " + isMasterDetail);
        String tag = in.generateTag();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
                /*
                .setCustomAnimations(R.anim.fragment_right_in,
                        R.anim.fragment_right_out,
                        R.anim.fragment_left_in,
                        R.anim.fragment_left_out);*/

        if(isMasterDetail)
        {
            Log.i("MASTER DETAIL", "MASTER DETAIL GRAVITY IS " + in.getGravity());
            if(in.getGravity() == Gravity.LEFT)
            {
                currentFragmentTagLeft = tag;
                transaction = transaction.replace(fragmentContainerLeft, in, tag);
            }
            else
            {
                currentFragmentTagRight = tag;
                transaction = transaction.replace(fragmentContainerRight, in, tag);
            }

            currentFragmentTag = null;
        }
        else
        {
            currentFragmentTag = tag;
            transaction = transaction.replace(fragmentContainer, in, tag);

            currentFragmentTagLeft = null;
            currentFragmentTagRight = null;
        }

        if(backstack)
        {
            transaction.addToBackStack(in.generateTag());
        }

        //TODO animations?
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(fragmentContainerLeft);
        isMasterDetail = view != null;

        BaseFragment.initNavigation(this);
        BaseViewHolder.initNavigation(this);

        // Fixing Later Map loading Delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();

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

        /**
         * Populating drawer
         */
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearLayout leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.drawer_list);
        ArrayList<DrawerItem> list = new ArrayList<>(3);

        String[] drawerText = getResources().getStringArray(R.array.drawer_items);
        for(String s : drawerText)
        {
            list.add(new DrawerItem(s));
        }

        if(recyclerView != null)
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new DrawerItemAdapter(list, leftDrawer));
        }

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.drawer_title)
            {

                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView)
                {
                    supportInvalidateOptionsMenu();
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }
        /***************************************************************************/


        /**
         * Restoring saved state after configuration change
         */
        if(savedInstanceState != null)
        {
            String tag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG, null);
            String tag_left = savedInstanceState.getString(CURRENT_FRAGMENT_TAG_LEFT, null);
            String tag_right = savedInstanceState.getString(CURRENT_FRAGMENT_TAG_RIGHT, null);

            if(tag != null)
            {
                BaseFragment toshow = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);

                if(isMasterDetail)
                {
                    detachFromContainer(toshow);
                    changeFragment(toshow, true);
                }
                else
                {
                    changeFragment(toshow, false);
                }

                if(isMasterDetail)
                {
                    BaseFragment fillin = toshow.createFillin();
                    Log.i("Fillin creation...", "Creating fillin " + fillin);
                    if(fillin != null)
                    {
                        changeFragment(fillin, false);
                    }
                }
                return;
            }

            if(tag_right != null)
            {
                BaseFragment toshow = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag_right);
                if(!isMasterDetail)
                {
                    detachFromContainer(toshow);
                    changeFragment(toshow, true);
                }
                else
                {
                    changeFragment(toshow, false);
                }

                return;
            }

            if(tag_left != null)
            {
                if(isMasterDetail)
                {
                    BaseFragment toshow = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag_left);
                    changeFragment(toshow, false);
                }

                return;
            }
        }
        /***************************************************************************/

        changeFragment(NodeList.newInstance(), false);
    }

    protected void detachFromContainer(BaseFragment in)
    {
        getSupportFragmentManager().popBackStackImmediate();
        getSupportFragmentManager()
                .beginTransaction()
                .remove(in)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.i("onSaveInstanceState", "tags: " + currentFragmentTag + " " + currentFragmentTagLeft + " " + currentFragmentTagRight);
        outState.putString(CURRENT_FRAGMENT_TAG, currentFragmentTag);
        outState.putString(CURRENT_FRAGMENT_TAG_LEFT, currentFragmentTagLeft);
        outState.putString(CURRENT_FRAGMENT_TAG_RIGHT, currentFragmentTagRight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TaskHandler.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TaskHandler.getInstance().onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        IFTTTDatabase.getHelper(this).close();
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
        changeFragment(in, true);
    }

    @Override
    public void go(BaseFragment in, boolean backstack)
    {
        changeFragment(in, backstack);
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
    public void clearStack()
    {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void closeDrawer()
    {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void openDrawer()
    {
        drawerLayout.openDrawer(Gravity.LEFT);
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
            go(NodeList.newInstance());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "ONACTIVITYRESULT requestcode: " + requestCode);
        if(requestCode == LocationContext.PLACE_PICKER_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                LocationContext.placeRequester.doneSelectingPlace(this, data);
            }
        }
    }
}
