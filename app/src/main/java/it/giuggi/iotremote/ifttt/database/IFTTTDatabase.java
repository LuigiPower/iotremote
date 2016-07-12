package it.giuggi.iotremote.ifttt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.giuggi.iotremote.ifttt.structure.IFTTTAction;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTEvent;
import it.giuggi.iotremote.ifttt.structure.IFTTTFilter;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Database helper class for {@link Databasable} objects
 */
public class IFTTTDatabase extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "IFTTT_DATABASE";
    public static final int DATABASE_VERSION = 1;

    private static IFTTTDatabase instance;

    public static synchronized IFTTTDatabase getHelper(Context context)
    {
        if (instance == null)
            instance = new IFTTTDatabase(context);

        return instance;
    }

    public IFTTTDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(IFTTTContract.SQL_CREATE_COMPONENT_LINK_ENTRIES);
        db.execSQL(IFTTTContract.SQL_CREATE_RULE_ENTRIES);
        db.execSQL(IFTTTContract.SQL_CREATE_RULECOMPONENT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    /* ---- START: METHODS THAT QUERY DATABASE CONTENTS ---- */

    /**
     * Debug method to check all elements in database
     */
    public synchronized void checkDatabase()
    {
        SQLiteDatabase sql = getReadableDatabase();

        Cursor c1 = sql.rawQuery("SELECT * FROM " + IFTTTContract.IFTTTRuleComponentEntry.TABLE_NAME, null);
        Cursor c2 = sql.rawQuery("SELECT * FROM " + IFTTTContract.IFTTTRuleEntry.TABLE_NAME, null);
        Cursor c3 = sql.rawQuery("SELECT * FROM " + IFTTTContract.IFTTTComponentLinkEntry.TABLE_NAME, null);

        while(c1.moveToNext())
        {
            for(int i = 0; i < c1.getColumnCount(); i++)
            {
                Log.i(DATABASE_NAME, c1.getColumnName(i) + ": " + c1.getString(i));
            }
        }

        while(c2.moveToNext())
        {
            for(int i = 0; i < c2.getColumnCount(); i++)
            {
                Log.i(DATABASE_NAME, c2.getColumnName(i) + ": " + c2.getString(i));
            }
        }

        while(c3.moveToNext())
        {
            for(int i = 0; i < c3.getColumnCount(); i++)
            {
                Log.i(DATABASE_NAME, c3.getColumnName(i) + ": " + c3.getString(i));
            }
        }

        c3.close();
        c2.close();
        c1.close();
        sql.close();
    }

    @SuppressWarnings("unchecked")
    /**
     * Get a single rule that matches the passed ID
     * @param ruleid ID of the rule to find
     * @return an IFTTTRule instance that corresponds to the given ID
     */
    public synchronized IFTTTRule findRuleById(long ruleid) throws ClassNotFoundException
    {
        SQLiteDatabase sql = getReadableDatabase();

        Cursor c = sql.query(
                IFTTTContract.IFTTTRuleEntry.TABLE_NAME,
                new String[]{
                        IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID,
                        IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_NAME
                },
                IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID + " - ?",
                new String[]{
                        "" + ruleid
                },
                null,
                null,
                null
        );

        IFTTTRule rule = null;

        if(c.moveToFirst())
        {
            long id = c.getLong(c.getColumnIndex(IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID));
            String rulename = c.getString(c.getColumnIndex(IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_NAME));

            LinkedList<IFTTTFilter> iftttFilters = (LinkedList<IFTTTFilter>) getComponentsOfType(ruleid, IFTTTFilter.TYPE, sql);
            LinkedList<IFTTTContext> iftttContexts = (LinkedList<IFTTTContext>) getComponentsOfType(ruleid, IFTTTContext.TYPE, sql);
            LinkedList<IFTTTEvent> iftttEvents = (LinkedList<IFTTTEvent>) getComponentsOfType(ruleid, IFTTTEvent.TYPE, sql);
            LinkedList<IFTTTAction> iftttActions = (LinkedList<IFTTTAction>) getComponentsOfType(ruleid, IFTTTAction.TYPE, sql);

            rule = new IFTTTRule(id, rulename, iftttFilters, iftttContexts, iftttEvents, iftttActions);
        }

        c.close();
        sql.close();
        return rule;
    }

    @SuppressWarnings("unchecked")
    /**
     * Get a list of all saved rules
     */
    public synchronized List<IFTTTRule> getRuleList() throws ClassNotFoundException
    {
        SQLiteDatabase sql = getReadableDatabase();

        Cursor c = sql.query(
                IFTTTContract.IFTTTRuleEntry.TABLE_NAME,
                new String[]{
                        IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID,
                        IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_NAME
                },
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<IFTTTRule> ruleList = new ArrayList<>();

        while(c.moveToNext())
        {
            long ruleid = c.getLong(c.getColumnIndex(IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID));
            String rulename = c.getString(c.getColumnIndex(IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_NAME));

            LinkedList<IFTTTFilter> iftttFilters = (LinkedList<IFTTTFilter>) getComponentsOfType(ruleid, IFTTTFilter.TYPE, sql);
            LinkedList<IFTTTContext> iftttContexts = (LinkedList<IFTTTContext>) getComponentsOfType(ruleid, IFTTTContext.TYPE, sql);
            LinkedList<IFTTTEvent> iftttEvents = (LinkedList<IFTTTEvent>) getComponentsOfType(ruleid, IFTTTEvent.TYPE, sql);
            LinkedList<IFTTTAction> iftttActions = (LinkedList<IFTTTAction>) getComponentsOfType(ruleid, IFTTTAction.TYPE, sql);

            IFTTTRule rule = new IFTTTRule(ruleid, rulename, iftttFilters, iftttContexts, iftttEvents, iftttActions);
            ruleList.add(rule);
        }

        c.close();
        sql.close();
        return ruleList;
    }

    /**
     * Gets a list of components of given type
     * @param ruleid id of rule to get components of
     * @param type FILTER EVENT CONTEXT ACTION
     * @param sql Open database instance, or null
     * @return List of components
     * @throws ClassNotFoundException
     */
    public synchronized List<? extends IFTTTComponent> getComponentsOfType(long ruleid, String type, @Nullable SQLiteDatabase sql) throws ClassNotFoundException
    {
        boolean needToClose = false;
        if(sql == null)
        {
            sql = getReadableDatabase();
            needToClose = true;
        }

        String query = "SELECT * FROM " + IFTTTContract.IFTTTRuleComponentEntry.TABLE_NAME + ", " + IFTTTContract.IFTTTComponentLinkEntry.TABLE_NAME + " " +
                "WHERE " + IFTTTContract.IFTTTRuleComponentEntry.TABLE_NAME + "." + IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_ENTRY_ID +
                    " = " + IFTTTContract.IFTTTComponentLinkEntry.TABLE_NAME + "." + IFTTTContract.IFTTTComponentLinkEntry.COLUMN_NAME_COMPONENT_ID + " " +
                "AND " + IFTTTContract.IFTTTComponentLinkEntry.TABLE_NAME + "." + IFTTTContract.IFTTTComponentLinkEntry.COLUMN_NAME_RULE_ID + " = ?" + " " +
                "AND " + IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_TYPE + " = ?;";

        Cursor c = sql.rawQuery(query, new String[]{
                String.valueOf(ruleid),
                type
        });

        LinkedList<IFTTTComponent> componentList = new LinkedList<>();
        Gson gson = new Gson();

        while(c.moveToNext())
        {
            String json = c.getString(c.getColumnIndex(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_GSON));
            String classname = c.getString(c.getColumnIndex(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_CLASS_NAME));

            Class<?> clazz = Class.forName(classname);
            IFTTTComponent component = (IFTTTComponent) gson.fromJson(json, clazz);
            component.setComponentId(c.getLong(c.getColumnIndex(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_ENTRY_ID)));
            componentList.add(component);
        }

        c.close();
        if(needToClose)
        {
            sql.close();
        }
        return componentList;
    }

    /* ---- END: METHODS THAT QUERY DATABASE CONTENTS ---- */

    /* ---- START: METHODS THAT MODIFY DATABASE CONTENTS ---- */

    /**
     * addComponent adds the specified component in gson form into the database
     * @param gson gson form of the component
     * @param type type of the component (FILTER, EVENT, CONTEXT, ACTION)
     * @param clazz class of the component, to allow reinstantiation through Java Reflection
     * @return component id
     */
    public synchronized long addComponent(String gson, String type, Class<?> clazz)
    {
        SQLiteDatabase sql = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_GSON, gson);
        values.put(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_TYPE, type);
        values.put(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_CLASS_NAME, clazz.getName());
        long inserted_id = sql.insert(IFTTTContract.IFTTTRuleComponentEntry.TABLE_NAME, null, values);
        sql.close();

        return inserted_id;
    }

    /**
     * updateComponent updates the specified component
     * @param componentid id of the component to update
     * @param gson gson form of the component
     * @param type type of the component (FILTER, EVENT, CONTEXT, ACTION)
     * @param clazz class of the component, to allow reinstantiation through Java Reflection
     * @return updated components count (should always be 1 if successful, 0 otherwise)
     */
    public synchronized int updateComponent(long componentid, String gson, String type, Class<?> clazz)
    {
        SQLiteDatabase sql = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_GSON, gson);
        values.put(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_TYPE, type);
        values.put(IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_CLASS_NAME, clazz.getName());
        int number_updated = sql.update(IFTTTContract.IFTTTRuleComponentEntry.TABLE_NAME,
                values,
                IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{String.valueOf(componentid)});
        sql.close();

        return number_updated;
    }

    /**
     * deleteComponent deletes the specified component
     * @param componentid component to delete
     * @return number of rows deleted
     */
    public synchronized int deleteComponent(long componentid)
    {
        SQLiteDatabase sql = getWritableDatabase();

        ContentValues values = new ContentValues();
        int number_deleted = sql.delete(IFTTTContract.IFTTTRuleComponentEntry.TABLE_NAME,
                IFTTTContract.IFTTTRuleComponentEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{String.valueOf(componentid)});
        sql.close();

        return number_deleted;
    }

    /**
     * addLink adds a link between rule and component
     * @param ruleid rule to link
     * @param componentid component to link
     */
    public synchronized void addLink(long ruleid, long componentid)
    {
        SQLiteDatabase sql = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IFTTTContract.IFTTTComponentLinkEntry.COLUMN_NAME_RULE_ID, ruleid);
        values.put(IFTTTContract.IFTTTComponentLinkEntry.COLUMN_NAME_COMPONENT_ID, componentid);
        sql.insert(IFTTTContract.IFTTTComponentLinkEntry.TABLE_NAME, null, values);
        sql.close();
    }

    /**
     * deleteLink removes a link between rule and component
     * @param ruleid rule to unlink
     * @param componentid component to unlink
     * @return number of links deleted, should always be 1 if successful, 0 otherwise
     */
    public synchronized int deleteLink(long ruleid, long componentid)
    {
        SQLiteDatabase sql = getWritableDatabase();

        ContentValues values = new ContentValues();
        int number_deleted = sql.delete(IFTTTContract.IFTTTComponentLinkEntry.TABLE_NAME,
                IFTTTContract.IFTTTComponentLinkEntry.COLUMN_NAME_RULE_ID + " = ? AND "
                        + IFTTTContract.IFTTTComponentLinkEntry.COLUMN_NAME_COMPONENT_ID + " = ?",
                new String[]{ String.valueOf(ruleid), String.valueOf(componentid)});
        sql.close();

        return number_deleted;
    }

    /**
     * addRule adds a rule into the database with the specified name
     * @param name name of the rule
     * @return rule id
     */
    public synchronized long addRule(String name)
    {
        SQLiteDatabase sql = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_NAME, name);
        long inserted_id = sql.insert(IFTTTContract.IFTTTRuleEntry.TABLE_NAME, null, values);
        sql.close();

        return inserted_id;
    }

    /**
     * updateRule updates a rule inside the database
     * @param ruleid rule to update
     * @param name new name of the rule
     * @return number of updated rules (should be always 1 if succesful, 0 otherwise)
     */
    public synchronized int updateRule(long ruleid, String name)
    {
        SQLiteDatabase sql = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_NAME, name);
        int number_updated = sql.update(IFTTTContract.IFTTTRuleEntry.TABLE_NAME,
                values,
                IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{String.valueOf(ruleid)});
        sql.close();

        return number_updated;
    }

    /**
     * deleteRule deletes the specified rule
     * @param ruleid rule to delete
     * @return number of rows deleted
     */
    public synchronized int deleteRule(long ruleid)
    {
        SQLiteDatabase sql = getWritableDatabase();

        int number_deleted = sql.delete(IFTTTContract.IFTTTRuleEntry.TABLE_NAME,
                IFTTTContract.IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{String.valueOf(ruleid)});
        sql.close();

        return number_deleted;
    }

    /* ---- END: METHODS THAT MODIFY DATABASE CONTENTS ---- */

}
