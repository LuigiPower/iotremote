package it.giuggi.iotremote.ifttt.structure;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * IFTTTCurrentSituation
 * Captures a snapshot of current sensor's status, to match with previously set
 * IFTTT rules.
 *
 * TODO GPS (TEST + INTERFACE TO SET)
 * TODO Geofencing (at home, at work, at custom locations...)
 * TODO Wi-Fi signal: allow user to setup "home network", "work network"... to recognize places (TEST + INTERFACE TO SET)
 * TODO Movement - Accellerometer: Find out whether you are in a car, walking or standing still (This one should update outside "acquireSnapshot", and provide the data when "acquireSnapshot" is called)
 * TODO Keep looking for other ways of getting data
 */
public class IFTTTCurrentSituation implements LocationListener
{
    public interface OnSnapshotReadyListener
    {
        void onSnapshotReady(CurrentSituation situation);
    }

    /**
     * Current situation keeps a snapshot of various sensors and information about networks
     * this snapshot may be updated by when the rules have already been resolved, but it is
     * expected behaviour (Context is relative to GCM arrival)
     */
    public class CurrentSituation
    {
        /**
         * Context type should be calculated using all data acquired into the snapshot
         * ANY means "UNKNOWN" in this context
         */
        private IFTTTContext.IFTTTContextType contextType = IFTTTContext.IFTTTContextType.ANY;

        private Location location;
        private OnSnapshotReadyListener listener;

        private WifiInfo wifiStatus;
        private NetworkInfo activeNetwork;

        private LinkedList<NetworkInfo> wifi;
        private LinkedList<NetworkInfo> bluetooth;
        private LinkedList<NetworkInfo> mobile;

        public CurrentSituation(OnSnapshotReadyListener listener)
        {
            this.wifi = new LinkedList<>();
            this.bluetooth = new LinkedList<>();
            this.mobile = new LinkedList<>();

            this.listener = listener;
            this.location = null;
        }

        public boolean isLocationIn(double latitude, double longitude, double radius)
        {
            //TODO use geofencing
            return true;
        }

        public boolean checkReady()
        {
            Log.d("IFTTTCurrentSituation", "Checking ready");
            boolean ready = this.location != null;

            if(ready)
            {
                listener.onSnapshotReady(this);
                return true;
            }
            else
                listener.onSnapshotReady(this);//TODO remove this
            return false;
        }

        public String toLogString()
        {
            return toString() + " Location: " + this.location + " wifi: " + this.wifi+ " bluetooth: " + this.bluetooth + " mobile: " + this.mobile;
        }

        /**
         * Checks if wifi is connected to specified SSID
         * @param ssid SSID to check (Ex.: Home WIFI)
         * @return true if connected to specified SSID, else false
         */
        public boolean isConnectedTo(String ssid)
        {
            return wifiStatus != null && wifiStatus.getSSID() != null && wifiStatus.getSSID().equalsIgnoreCase(ssid);
        }

        /**
         * Checks if phone is connected to the internet
         * @return true if connected, false otherwise
         */
        public boolean isConnected()
        {
            return activeNetwork != null && activeNetwork.isConnected();
        }
    }

    private final CurrentSituation situation;
    private final LocationManager manager;

    /**
     * Creates a new IFTTTCurrentSituation.
     * TODO start acquiring data from sensors
     */
    private IFTTTCurrentSituation(Context context, OnSnapshotReadyListener listener)
    {
        Log.d("IFTTTCurrentSituation", "Building IFTTTCurrentSituation");
        situation = new CurrentSituation(listener);

        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //TODO do this on first activity startup
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000)   //TODO set time differently
        {
            situation.location = location;
            situation.checkReady();
        }
        else
        {
            //TODO this doesn't work outside UI Thread
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    //noinspection ResourceType
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, IFTTTCurrentSituation.this);
                }
            });
        }

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            Network[] networks = connManager.getAllNetworks();


            for (Network network : networks)
            {
                NetworkInfo net = connManager.getNetworkInfo(network);
                if(net.getType() == ConnectivityManager.TYPE_WIFI)
                {
                    situation.wifi.add(net);
                }
                else if(net.getType() == ConnectivityManager.TYPE_BLUETOOTH)
                {
                    situation.bluetooth.add(net);
                }
                else if(net.getType() == ConnectivityManager.TYPE_MOBILE)
                {
                    situation.mobile.add(net);
                }
            }
        }
        else
        {
            NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo bluetooth = connManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
            NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            situation.wifi.add(wifi);
            situation.bluetooth.add(bluetooth);
            situation.mobile.add(mobile);
        }
        situation.activeNetwork = connManager.getActiveNetworkInfo();

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        situation.wifiStatus = wifiInfo;

        situation.checkReady();
    }

    public static void acquireSnapshot(Context context, OnSnapshotReadyListener listener)
    {
        IFTTTCurrentSituation iftttCurrentSituation = new IFTTTCurrentSituation(context, listener);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onLocationChanged(Location location)
    {
        manager.removeUpdates(this);
        situation.location = location;
        situation.checkReady();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }
}
