package it.giuggi.iotremote.ifttt.structure;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import it.giuggi.iotremote.R;
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

    public abstract int getColorId();

    public abstract int getLayoutResourceId();

    public abstract int getEditLayoutResourceId();

    protected abstract int getComponentNameResourceId();

    public String getComponentName(Context context)
    {
        int resourceid = getComponentNameResourceId();
        String name = context.getString(resourceid);
        return name;
    }

    public int getIcon()
    {
        return R.drawable.ic_pages_24dp;
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

        return database.deleteComponent(this.componentid);
    }

    protected abstract void populateView(View view);

    protected abstract void populateEditView(View view);

    private View doLoad(Context context, int resourceid, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(resourceid, parent, false);
        parent.removeAllViews();
        parent.addView(view);

        return view;
    }

    public View loadView(ViewGroup parent)
    {
        int resourceid = getLayoutResourceId();

        if(resourceid == -1)
        {
            return new View(parent.getContext());
        }

        View view = doLoad(parent.getContext(), resourceid, parent);
        populateView(view);

        return view;
    }

    public View loadEditView(ViewGroup parent)
    {
        int resourceid = getEditLayoutResourceId();

        if(resourceid == -1)
        {
            return new View(parent.getContext());
        }

        View view = doLoad(parent.getContext(), resourceid, parent);
        populateEditView(view);

        return view;
    }
}
