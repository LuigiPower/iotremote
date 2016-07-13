package it.giuggi.iotremote.ifttt.structure;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.ifttt.database.Databasable;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.iot.mode.IOperatingMode;
import it.giuggi.iotremote.iot.node.IOTNode;

/**
 *      {
 *          "node": {
 *              //Data about the sender node
 *          },
 *          "event": {
 *              "type": "VALUE_CHANGED",            //One of the event types
 *              "mode": {
 *                  "name": "gpio_read_mode",      //Mode that made this event happen (copy of the mode)
 *                  "params": {
 *                      "status": 1,
 *                      "gpio": 1
 *                  }
 *               },
 *              "params": ["status"],           //Parameters that made this event happen
 *              "oldvalues": [0],                   //Old values of said parameters
 *              "newvalues": [1],                   //New values of said parameters
 *          }
 *      }
 */
public class Event extends Databasable
{
    transient public long logid = -1;
    transient public IFTTTCurrentSituation.CurrentSituation snapshot;
    transient public IOperatingMode mode;
    transient public IOTNode sender_node;
    transient public String timestamp;

    public String mode_name;
    public String sender_name;
    public String ip_address;
    public IFTTTEvent.IFTTTEventType type;
    public String[] parameters;
    private String[] oldvalues;
    private String[] newvalues;

    @Override
    protected long doSave(Context context, IFTTTDatabase database)
    {
        Gson gson = new Gson();
        this.logid = database.addEventLog(gson.toJson(this), sender_node.name, mode.getName(), type.name(), this.getClass());
        return this.logid;
    }

    @Override
    protected int doUpdate(Context context, IFTTTDatabase database)
    {
        //Never do update; logs are never updated, only written and deleted
        return 0;
    }

    @Override
    protected int doDelete(Context context, IFTTTDatabase database)
    {
        if(this.logid == -1) return -1;   //Return if component was never saved

        return database.deleteComponent(this.logid);
    }

    public Event()
    {

    }

    public Event(JSONObject obj) throws JSONException
    {
        JSONObject nodeJson = obj.getJSONObject("node");
        JSONObject eventJson = obj.getJSONObject("event");

        this.sender_node = IOTNode.fromJSON(nodeJson);

        String type = eventJson.getString(IOperatingMode.Parameters.TYPE);
        this.type = typeToEvent(type);
        JSONObject target_mode = eventJson.getJSONObject(IOperatingMode.Parameters.MODE);
        String mode_name = target_mode.getString(IOperatingMode.Parameters.NAME);
        JSONObject mode_params = target_mode.getJSONObject(IOperatingMode.Parameters.PARAMS);
        this.mode = IOperatingMode.stringToMode(mode_name, mode_params);

        this.sender_name = this.sender_node.name;
        this.ip_address = this.sender_node.ipAddress;
        this.mode_name = this.mode.getName();

        JSONArray params = eventJson.getJSONArray(IOperatingMode.Parameters.PARAMS);
        this.parameters = new String[params.length()];
        for(int i = 0; i < params.length(); i++)
        {
            this.parameters[i] = params.getString(i);
        }

        JSONArray oldvalues = eventJson.getJSONArray(IOperatingMode.Parameters.OLD_VALUES);
        JSONArray newvalues = eventJson.getJSONArray(IOperatingMode.Parameters.NEW_VALUES);

        this.oldvalues = new String[oldvalues.length()];
        this.newvalues = new String[newvalues.length()];

        //Should be fine, newvalues and oldvalues MUST have the same length
        for(int i = 0; i < oldvalues.length(); i++)
        {
            this.oldvalues[i] = oldvalues.getString(i);
            this.newvalues[i] = newvalues.getString(i);
        }
    }

    public static IFTTTEvent.IFTTTEventType typeToEvent(String type)
    {
        IFTTTEvent.IFTTTEventType toReturn = IFTTTEvent.IFTTTEventType.UNKNOWN;
        try
        {
            toReturn = IFTTTEvent.IFTTTEventType.valueOf(type.toUpperCase());
        }
        catch(IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }

        return toReturn;
    }

    public String[] getOldValues()
    {
        return this.oldvalues;
    }

    public String[] getNewValues()
    {
        return this.newvalues;
    }
}