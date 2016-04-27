package it.giuggi.iotremote.ifttt.database;

import android.content.Context;

/**
 * Created by Federico Giuggioloni on 27/04/16.
 * Databasable interface
 * enables class that extends it to be saved to permanent storage in a MySQL Database
 */
public abstract class Databasable
{
    protected abstract long doSave(Context context, IFTTTDatabase database);

    public final long save(Context context)
    {
        IFTTTDatabase database = IFTTTDatabase.getHelper(context);
        return doSave(context, database);
    }

    protected abstract int doUpdate(Context context, IFTTTDatabase database);

    public final int update(Context context)
    {
        IFTTTDatabase database = IFTTTDatabase.getHelper(context);
        return doUpdate(context, database);
    }

    protected abstract int doDelete(Context context, IFTTTDatabase database);

    public final int delete(Context context)
    {
        IFTTTDatabase database = IFTTTDatabase.getHelper(context);
        return doDelete(context, database);
    }

}
