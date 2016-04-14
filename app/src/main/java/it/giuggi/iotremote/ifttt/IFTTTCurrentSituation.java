package it.giuggi.iotremote.ifttt;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * IFTTTCurrentSituation
 * Captures a snapshot of current sensor's status, to match with previously set
 * IFTTT rules.
 *
 * TODO GPS + Geofencing (at home, at work, at custom locations...)
 * TODO Wi-Fi signal: allow user to setup "home network", "work network"... to recognize places
 * TODO Movement - Accellerometer: Find out whether you are in a car, walking or standing still
 * TODO Check for other types of sensing
 */
public class IFTTTCurrentSituation
{
    /**
     * Creates a new IFTTTCurrentSituation.
     * TODO start acquiring data from sensors
     */
    private IFTTTCurrentSituation()
    {

    }

    public static IFTTTCurrentSituation acquireSnapshot()
    {
        return new IFTTTCurrentSituation();
    }


}
