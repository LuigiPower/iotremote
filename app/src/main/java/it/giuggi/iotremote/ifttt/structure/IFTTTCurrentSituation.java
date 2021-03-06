package it.giuggi.iotremote.ifttt.structure;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import it.giuggi.iotremote.ifttt.activityrecognition.ActivityDetectionIntentService;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * IFTTTCurrentSituation
 * Captures a snapshot of current sensor's status, to match with previously set
 * IFTTT rules.
 *
 * TODO GPS (TEST + INTERFACE TO SET)
 * TODO Geofencing (at home, at work, at custom locations...)
 * Wi-Fi signal: allow user to setup "home network", "work network"... to recognize places (TODO TEST + INTERFACE TO SET)
 * Movement - Accellerometer: Find out whether you are in a car, walking or standing still
 * TODO Keep looking for other ways of getting data
 */
public class IFTTTCurrentSituation extends BroadcastReceiver implements LocationListener, SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private static GoogleApiClient activityRecognitionApi = null;

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

        transient private static final long TIMEOUT_INTERVAL = 30000L;

        transient private Location location;
        transient private OnSnapshotReadyListener listener;

        transient private WifiInfo wifiStatus;
        transient private NetworkInfo activeNetwork;

        transient private LinkedList<NetworkInfo> wifi;
        transient private LinkedList<NetworkInfo> bluetooth;
        transient private LinkedList<NetworkInfo> mobile;

        transient private boolean didTimeout;
        transient private final Runnable timeoutRunnable;
        transient private Handler timeoutHandler;

        private int totalSensors = -1;
        private int initializedSensors = 0;
        private float[] accelerometer = null;
        private float[] temperature = null;
        private float[] gravity = null;
        private float[] magneticField = null;
        private float[] gyroscope = null;
        private float[] heartRate = null;
        private float[] light = null;
        private float[] linearAcceleration = null;
        private float[] pressure = null;
        private float[] proximity = null;
        private float[] humidity = null;

        public ArrayList<DetectedActivity> currentActivities = null;

        public CurrentSituation(Context context, OnSnapshotReadyListener listener)
        {
            this.wifi = new LinkedList<>();
            this.bluetooth = new LinkedList<>();
            this.mobile = new LinkedList<>();

            this.listener = listener;
            this.location = null;

            this.didTimeout = false;
            this.timeoutHandler = new Handler(context.getMainLooper());

            this.timeoutRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    didTimeout = true;
                    checkReady();
                }
            };
            this.timeoutHandler.postDelayed(this.timeoutRunnable, TIMEOUT_INTERVAL);
        }

        private String floatArrayToString(float[] array)
        {
            String s = "";
            if(array == null)
            {
                return "null\n";
            }

            for(float val : array)
            {
                s += val + ", ";
            }
            s += ";\n";
            return s;
        }

        @Override
        public String toString()
        {
            String s = "\n++++++++++++++++++++++++++++\n" +
                       "+    CURRENT SITUATION     +\n" +
                       "+--------------------------+\n" +
                       "+ location: " + location + "\n" +
                       "+ wifi status: " + wifiStatus + "\n" +
                       "+ active network: " + activeNetwork + "\n" +
                       "+ wifi list: " + wifi + "\n" +
                       "+ bluetooth list: " + bluetooth + "\n" +
                       "+ mobile list: " + mobile + "\n" +
                       "+--------------------------+\n" +
                       "+           SENSORS        +\n" +
                       "+--------------------------+\n" +
                       "+ accelerometer: " + floatArrayToString(accelerometer) +
                       "+ temperature: " + floatArrayToString(temperature) +
                       "+ gravity: " + floatArrayToString(gravity) +
                       "+ magneticField: " + floatArrayToString(magneticField) +
                       "+ gyroscope: " + floatArrayToString(gyroscope) +
                       "+ heartRate: " + floatArrayToString(heartRate) +
                       "+ light: " + floatArrayToString(light) +
                       "+ linearAcceleration: " + floatArrayToString(linearAcceleration) +
                       "+ pressure: " + floatArrayToString(pressure) +
                       "+ proximity: " + floatArrayToString(proximity) +
                       "+ humidity: " + floatArrayToString(humidity) +
                       "+--------------------------+\n" +
                       "+    ACTIVITY DETECTION    +\n" +
                       "+--------------------------+\n";

            for(DetectedActivity activity : currentActivities)
            {
                s += "+ Detected activity: " + activity.toString() + "\n";
            }

            s += "+++++++++++++++++++++++++++\n";
            return s;
        }

        public boolean isLocationIn(double latitude, double longitude, float radius)
        {
            Location target = new Location("");
            target.setLatitude(latitude);
            target.setLongitude(longitude);

            if(location == null)
            {
                return false;
            }

            float distance = location.distanceTo(target);
            return distance < radius;
        }

        public boolean checkReady()
        {
            boolean ready = this.location != null           //Wait for the current location
                    && totalSensors == initializedSensors   //Wait until all sensors have been initialized
                    && currentActivities != null;          //Wait for activity detection
            ready = ready || didTimeout;    //If I'm waiting for too long, fire the listener with the data gathered until now

//            Log.e("CHECKING READY", "CHECKING ready is " + ready);
//            Log.e("CHECKING READY", "CHECKING " + this.location + " " + totalSensors + "/" + initializedSensors + " " + currentActivities);

            if(currentActivities == null && !activityRecognitionApi.isConnected() && !activityRecognitionApi.isConnecting())
            {
                currentActivities = new ArrayList<>();
                activityRecognitionApi.connect();
                return checkReady(); //This makes sense, as if currentActivities was the problem, it will return true, else it won't loop
            }

            if(ready)
            {
                this.timeoutHandler.removeCallbacks(this.timeoutRunnable);
                listener.onSnapshotReady(this);
                return true;
            }
            //else
            //    listener.onSnapshotReady(this);//TODO remove this
            return false;
        }

        /**
         * Checks if wifi is connected to specified SSID
         * @param ssid SSID to check (Ex.: Home WIFI)
         * @return true if connected to specified SSID, else false
         */
        public boolean isConnectedTo(String ssid)
        {
            if(wifiStatus == null) return false;

            String currentssid = wifiStatus.getSSID().replace("\"", "");
            return currentssid.equalsIgnoreCase(ssid);
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
    private final SensorManager mSensorManager;

    /**
     * Creates a new IFTTTCurrentSituation.
     * TODO start acquiring data from sensors
     */
    private IFTTTCurrentSituation(Context context, OnSnapshotReadyListener listener)
    {
        //Log.d("IFTTTCurrentSituation", "Building IFTTTCurrentSituation");
        situation = new CurrentSituation(context, listener);

        /** ACTIVITY RECOGNITION SETUP ***********************************************************/
        if(activityRecognitionApi == null)
        {
            activityRecognitionApi = new GoogleApiClient.Builder(context)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        else
        {
            activityRecognitionApi.registerConnectionCallbacks(this);
            activityRecognitionApi.registerConnectionFailedListener(this);
        }

        LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(ActivityDetectionIntentService.BROADCAST_ACTION));
        activityRecognitionApi.connect();
        /** END ACTIVITY RECOGNITIION SETUP  *****************************************************/
        /*****************************************************************************************/

        /** SENSOR SETUP *************************************************************************/
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        int totalSensors = 0;
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor : sensorList)
        {
            //To avoid starting up unnecessary sensors
            if(sensor.getType() == Sensor.TYPE_ACCELEROMETER
                    || sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE
                    || sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
                    || sensor.getType() == Sensor.TYPE_GRAVITY
                    || sensor.getType() == Sensor.TYPE_GYROSCOPE
                    || sensor.getType() == Sensor.TYPE_HEART_RATE
                    || sensor.getType() == Sensor.TYPE_LIGHT
                    //|| sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION
                    || sensor.getType() == Sensor.TYPE_PRESSURE
                    || sensor.getType() == Sensor.TYPE_PROXIMITY
                    || sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY)
            {
                //TODO is this sensor the default sensor for that type?
                totalSensors++;
                //Log.d("IFTTTCurrent", "Listening on sensor " + sensor);
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        situation.totalSensors = totalSensors;
        /** END SENSOR SETUP  ********************************************************************/
        /*****************************************************************************************/

        /** LOCATION SETUP ***********************************************************************/
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

        List<String> providers = manager.getProviders(true);
        for(String provider : providers)
        {
            Location location = manager.getLastKnownLocation(provider);
            //TODO get the most recent known location6
            if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000)   //TODO set time differently
            {
                //Log.d("IFTTTCurrent", "last known location is (" + location + ")");
                situation.location = location;
                situation.checkReady();
            }
        }

        if(situation.location == null)
        {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    boolean foundProvider = false;
                    List<String> providers = manager.getProviders(true);
                    for (String provider : providers)
                    {
                        if(manager.isProviderEnabled(provider))
                        {
                            //noinspection ResourceType
                            manager.requestLocationUpdates(provider, 0, 0, IFTTTCurrentSituation.this);
                            foundProvider = true;
                        }
                    }

                    if(!foundProvider)
                    {
                        situation.location = new Location("");
                    }
                }
            });
        }
        /** END LOCATION SETUP  ******************************************************************/
        /*****************************************************************************************/

        /** CONNECTIVITY SETUP *******************************************************************/
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
        /** END CONNECTIVITY SETUP  **************************************************************/
        /*****************************************************************************************/

        situation.checkReady();
    }

    public static IFTTTCurrentSituation acquireSnapshot(Context context, OnSnapshotReadyListener listener)
    {
        return new IFTTTCurrentSituation(context, listener);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onLocationChanged(Location location)
    {
        //Log.d("IFTTTCurrent", "onLocationChanged(" + location + ")");
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

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        int type = event.sensor.getType();
        switch(type)
        {
            case Sensor.TYPE_ACCELEROMETER:
                situation.accelerometer = event.values;
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                situation.temperature = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                situation.magneticField = event.values;
                break;
            case Sensor.TYPE_GRAVITY:
                situation.gravity = event.values;
                break;
            case Sensor.TYPE_GYROSCOPE:
                situation.gyroscope = event.values;
                break;
            case Sensor.TYPE_HEART_RATE:
                situation.heartRate = event.values;
                break;
            case Sensor.TYPE_LIGHT:
                situation.light = event.values;
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                situation.linearAcceleration = event.values;
                break;
            case Sensor.TYPE_PRESSURE:
                situation.pressure = event.values;
                break;
            case Sensor.TYPE_PROXIMITY:
                situation.proximity = event.values;
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                situation.humidity = event.values;
                break;
            default: //I don't use data from this sensor
                break;
        }
        //Log.d("IFTTTCurrent", "Initialized sensor " + event.sensor + " with values " + event);
        situation.initializedSensors++;
        //Log.d("IFTTTCurrent", "Counters: total | current /  " + situation.totalSensors + " | " + situation.initializedSensors);
        mSensorManager.unregisterListener(this, event.sensor);
        situation.checkReady();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //Log.i("IFTTTCurrent", "onAccuracyChanged");
    }

    /** ACTIVITY DETECTION API *******************************************************************/
    protected PendingIntent getRecognitionIntent()
    {
        Intent intent = new Intent(activityRecognitionApi.getContext(), ActivityDetectionIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(activityRecognitionApi.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    protected void unregisterActivityRecognition()
    {
        activityRecognitionApi.unregisterConnectionCallbacks(this);
        activityRecognitionApi.unregisterConnectionFailedListener(this);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.i("IFTTTCurrent", "onReceive");
        ArrayList<DetectedActivity> updatedActivities = intent.getParcelableArrayListExtra(ActivityDetectionIntentService.ACTIVITY_EXTRA);
        LocalBroadcastManager.getInstance(activityRecognitionApi.getContext()).unregisterReceiver(this);
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(activityRecognitionApi, getRecognitionIntent());
        unregisterActivityRecognition();

        situation.currentActivities = updatedActivities != null ? updatedActivities : new ArrayList<DetectedActivity>();
        situation.checkReady();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        //Log.i("IFTTTCurrent", "onConnected");
        if(!activityRecognitionApi.isConnected())
        {
            //TODO check if detected activities work most of the time in rules
            situation.currentActivities = new ArrayList<>();
            situation.checkReady();
            return;
        }

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(activityRecognitionApi, 0, getRecognitionIntent());
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        //Log.i("IFTTTCurrent", "Connection suspended");
        activityRecognitionApi.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        //Log.i("IFTTTCurrent", "Connection failed");
        LocalBroadcastManager.getInstance(activityRecognitionApi.getContext()).unregisterReceiver(this);
        situation.currentActivities = new ArrayList<>();
        situation.checkReady();
    }
    /** END ACTIVITY DETECTION API ***************************************************************/
    /*********************************************************************************************/
}
