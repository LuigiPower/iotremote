package it.giuggi.iotremote.ifttt.structure;

import android.content.Context;

import com.google.gson.Gson;

import it.giuggi.iotremote.ifttt.database.Databasable;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;

/**
 * Created by Federico Giuggioloni on 27/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public abstract class IFTTTComponent extends Databasable
{
    private transient long componentid = -1;

    protected long getComponentId()
    {
        return componentid;
    }

    public void setComponentId(long componentId)
    {
        this.componentid = componentId;
    }

    protected abstract String getType();

    @Override
    protected long doSave(Context context, IFTTTDatabase database)
    {
        Gson gson = new Gson();
        this.componentid = database.addComponent(gson.toJson(this), this.getType(), this.getClass());
        return this.componentid;
    }

    @Override
    protected int doUpdate(Context context, IFTTTDatabase database)
    {
        if(this.componentid == -1) return -1;   //Return if component was never saved

        Gson gson = new Gson();
        return database.updateComponent(this.componentid, gson.toJson(this), this.getType(), this.getClass());
    }

    @Override
    protected int doDelete(Context context, IFTTTDatabase database)
    {
        if(this.componentid == -1) return -1;   //Return if component was never saved

        Gson gson = new Gson();
        return database.deleteComponent(this.componentid);
    }
}
