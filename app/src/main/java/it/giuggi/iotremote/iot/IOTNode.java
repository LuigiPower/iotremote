package it.giuggi.iotremote.iot;

/**
 * Created by Federico Giuggioloni on 10/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class IOTNode
{
    public String name;
    public IOperatingMode mode;

    public IOTNode(String name)
    {
        this.name = name;
        this.mode = new EmptyMode();
    }

    public IOTNode(String name, IOperatingMode mode)
    {
        this.name = name;
        this.mode = mode;
    }

    public void setMode(IOperatingMode newMode)
    {
        this.mode = newMode;
    }
}
