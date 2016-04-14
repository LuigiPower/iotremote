package it.giuggi.iotremote.ifttt;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IFTTTContext
{
    public enum IFTTTContextType { ANY, AT_HOME, IN_CAR, WALKING, OUTSIDE, AT_WORK/*?*/ }

    public IFTTTContextType type;

    public IFTTTContext ofType(IFTTTContextType type)
    {
        this.type = type;
        return this;
    }

    /**
     * Applies this IFTTTContext to given Situation.
     * A CurrentSituation object will be passed in, which contains the status of all
     * relevant sensors to be checked.
     * @param context IFTTTCurrentSituation containing relevant sensor data
     * @return true if we are in said context, false otherwise
     */
    public abstract boolean apply(IFTTTCurrentSituation context);
}
