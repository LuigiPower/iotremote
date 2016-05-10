package it.giuggi.iotremote.ifttt.structure;

import android.Manifest;
import android.content.Context;
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
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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
public class IFTTTCurrentSituation implements LocationListener, SensorEventListener
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

        public CurrentSituation(OnSnapshotReadyListener listener)
        {
            this.wifi = new LinkedList<>();
            this.bluetooth = new LinkedList<>();
            this.mobile = new LinkedList<>();

            this.listener = listener;
            this.location = null;
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
                       "+ humidity: " + floatArrayToString(humidity);

            s += "+++++++++++++++++++++++++++\n";
            return s;
        }

        public boolean isLocationIn(double latitude, double longitude, double radius)
        {
            //TODO use geofencing... ?
            return true;
        }

        public boolean checkReady()
        {
            boolean ready = this.location != null && totalSensors == initializedSensors;
            Log.d("IFTTTCurrentSituation", "Checking ready, which is " + ready);

            if(ready)
            {
                listener.onSnapshotReady(this);
                return true;
            }
            //else
            //    listener.onSnapshotReady(this);//TODO remove this
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
    private final SensorManager mSensorManager;

    /**
     * Creates a new IFTTTCurrentSituation.
     * TODO start acquiring data from sensors
     */
    private IFTTTCurrentSituation(Context context, OnSnapshotReadyListener listener)
    {
        Log.d("IFTTTCurrentSituation", "Building IFTTTCurrentSituation");
        situation = new CurrentSituation(listener);

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
                Log.d("IFTTTCurrent", "Listening on sensor " + sensor);
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
            if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000)   //TODO set time differently
            {
                Log.d("IFTTTCurrent", "last known location is (" + location + ")");
                situation.location = location;
                situation.checkReady();
            }
        }

        if(situation.location == null)
        {
            //TODO maybe don't use only the GPS_PROVIDER
            //TODO ask all providers for location updates, and stop all others on the first location update
            //TODO this doesn't work outside UI Thread
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    List<String> providers = manager.getProviders(true);
                    for (String provider : providers)
                    {
                        //noinspection ResourceType
                        manager.requestLocationUpdates(provider, 0, 0, IFTTTCurrentSituation.this);
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

    public static void acquireSnapshot(Context context, OnSnapshotReadyListener listener)
    {
        IFTTTCurrentSituation iftttCurrentSituation = new IFTTTCurrentSituation(context, listener);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onLocationChanged(Location location)
    {
        Log.d("IFTTTCurrent", "onLocationChanged(" + location + ")");
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
        Log.d("IFTTTCurrent", "Initialized sensor " + event.sensor + " with values " + event);
        Log.d("IFTTTCurrent", "Counters: total | current /  " + situation.totalSensors + " | " + situation.initializedSensors);
        situation.initializedSensors++;
        mSensorManager.unregisterListener(this, event.sensor);
        situation.checkReady();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
