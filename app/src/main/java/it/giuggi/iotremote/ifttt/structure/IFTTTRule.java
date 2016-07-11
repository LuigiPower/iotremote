package it.giuggi.iotremote.ifttt.structure;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.database.Databasable;
import it.giuggi.iotremote.ifttt.database.IFTTTDatabase;
import it.giuggi.iotremote.iot.IOTNode;
import it.giuggi.iotremote.iot.IOperatingMode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * Generic IFTTTRule
 * Contains a list of filters, contexts and events, and a list of actions to run
 * if all these conditions are met
 * TODO how do I store IFTTTRules inside permanent storage?
 * [ solution: Use GSON to save all rules inside SharedPreferences or MySqlLite for querying
 * (ex.: Filter(id, name, type, mode), Context(id, type), Event(id, type), Action(id, type, action_json), Rule(id),
 * Association(filter, context, action, rule) ]
 * So that I can read Action (which is the most problematic) straight from the database
 * TODO (Is GSON even needed? I need to try and build an actual action before I make that call)
 */
public class IFTTTRule extends Databasable
{
    protected long ruleid;
    protected String name;
    protected List<IFTTTFilter> iftttFilters;
    protected List<IFTTTContext> iftttContexts;
    protected List<IFTTTEvent> iftttEvents;
    protected List<IFTTTAction> iftttActions;

    /**
     * Creates a new IFTTTRule with specified id
     * @param id id of Rule (inside the database)
     * @param name Name of the rule
     * @param iftttFilters An optional list of filters. if null, an empty list is created
     * @param iftttContexts An optional list of contexts. if null, an empty list is created
     * @param iftttEvents An optional list of events. if null, an empty list is created
     * @param iftttActions An optional list of actions. if null, an empty list is created
     */
    public IFTTTRule(long id,
            String name,
            @Nullable LinkedList<IFTTTFilter> iftttFilters,
            @Nullable LinkedList<IFTTTContext> iftttContexts,
            @Nullable LinkedList<IFTTTEvent> iftttEvents,
            @Nullable LinkedList<IFTTTAction> iftttActions)
    {
        this(name, iftttFilters, iftttContexts, iftttEvents, iftttActions);
        this.ruleid = id;
    }

    /**
     * Creates a new IFTTTRule
     * @param name Name of the rule
     * @param iftttFilters An optional list of filters. if null, an empty list is created
     * @param iftttContexts An optional list of contexts. if null, an empty list is created
     * @param iftttEvents An optional list of events. if null, an empty list is created
     * @param iftttActions An optional list of actions. if null, an empty list is created
     */
    public IFTTTRule(String name,
            @Nullable LinkedList<IFTTTFilter> iftttFilters,
            @Nullable LinkedList<IFTTTContext> iftttContexts,
            @Nullable LinkedList<IFTTTEvent> iftttEvents,
            @Nullable LinkedList<IFTTTAction> iftttActions)
    {
        this.ruleid = -1;
        this.name = name;

        if(iftttFilters == null)
        {
            this.iftttFilters = new LinkedList<>();
        }
        else
        {
            this.iftttFilters = iftttFilters;
        }

        if(iftttContexts == null)
        {
            this.iftttContexts = new LinkedList<>();
        }
        else
        {
            this.iftttContexts = iftttContexts;
        }

        if(iftttEvents == null)
        {
            this.iftttEvents = new LinkedList<>();
        }
        else
        {
            this.iftttEvents = iftttEvents;
        }

        if(iftttActions == null)
        {
            this.iftttActions = new LinkedList<>();
        }
        else
        {
            this.iftttActions = iftttActions;
        }
    }

    @SuppressWarnings("unchecked")
    public LinkedList<IFTTTComponent> getComponentsOfType(String type)
    {
        LinkedList<? extends IFTTTComponent> list;

        if(type.equalsIgnoreCase(IFTTTFilter.TYPE))
        {
            list = (LinkedList<? extends IFTTTComponent>) iftttFilters;
        }
        else if(type.equalsIgnoreCase(IFTTTEvent.TYPE))
        {
            list = (LinkedList<? extends IFTTTComponent>) iftttEvents;
        }
        else if(type.equalsIgnoreCase(IFTTTContext.TYPE))
        {
            list = (LinkedList<? extends IFTTTComponent>) iftttContexts;
        }
        else if(type.equalsIgnoreCase(IFTTTAction.TYPE))
        {
            list = (LinkedList<? extends IFTTTComponent>) iftttActions;
        }
        else return null;

        return (LinkedList<IFTTTComponent>) list;
    }

    public void addFilter(IFTTTFilter filter)
    {
        this.iftttFilters.add(filter);
    }

    public void addContext(IFTTTContext context)
    {
        this.iftttContexts.add(context);
    }

    public void addEvent(IFTTTEvent event)
    {
        this.iftttEvents.add(event);
    }

    public void addAction(IFTTTAction action)
    {
        this.iftttActions.add(action);
    }

    public void addComponent(IFTTTComponent component)
    {
        String type = component.getType();

        if(type.equalsIgnoreCase(IFTTTFilter.TYPE))
        {
            addFilter((IFTTTFilter) component);
        }
        else if(type.equalsIgnoreCase(IFTTTEvent.TYPE))
        {
            addEvent((IFTTTEvent) component);
        }
        else if(type.equalsIgnoreCase(IFTTTContext.TYPE))
        {
            addContext((IFTTTContext) component);
        }
        else if(type.equalsIgnoreCase(IFTTTAction.TYPE))
        {
            addAction((IFTTTAction) component);
        }
    }

    public IFTTTFilter getFilterAt(int i)
    {
        return iftttFilters.get(i);
    }

    public IFTTTEvent getEventAt(int i)
    {
        return iftttEvents.get(i);
    }

    public IFTTTContext getContextAt(int i)
    {
        return iftttContexts.get(i);
    }

    public IFTTTAction getActionAt(int i)
    {
        return iftttActions.get(i);
    }

    public boolean isValid()
    {
        return iftttActions.size() > 0;
    }

    /**
     * Applies this rule to given data (a gcm message)
     * data format:
     *      {
     *          "node": { node_data as usual },         //This is the node that made this event happen
     *          "event": {
     *              "type": "VALUE_CHANGED",            //One of the event types
     *              "mode_name": "gpio_read_mode",      //Mode that made this event happen TODO consider changing this into a copy of said mode taken from the node part
     *              "params": ["status"],           //Parameters that made this event happen
     *              "oldvalues": [0],                   //Old values of said parameters
     *              "newvalues": [1],                   //New values of said parameters
     *          }
     *      }
     * @return true if it is applicable, false otherwise
     */
    public boolean apply(IFTTTCurrentSituation.CurrentSituation currentSituation, JSONObject gcmMessage, Context context) throws JSONException
    {
        Log.d("IFTTTRule", "Applying rule " + name);

        /**
         * Initialize IoTNode from node part of the GCM message
         */
        JSONObject nodeJson = gcmMessage.getJSONObject("node");
        JSONObject modeJson = nodeJson.getJSONObject("mode");
        IOperatingMode mode = IOperatingMode.stringToMode(modeJson.getString("name"), modeJson.getJSONObject("params"));
        IOTNode node = new IOTNode(nodeJson.getString("ip"), nodeJson.getString("name"), mode);

        /**
         * Initialize Event from event part of the GCM message
         */
        JSONObject eventJson = gcmMessage.getJSONObject("event");
        IFTTTEvent.Event event = new IFTTTEvent.Event(eventJson);

        boolean result = false;
        for(IFTTTFilter iftttFilter : iftttFilters)
        {
            Log.d("IFTTTRule", "Applying filter " + iftttFilter.getClass().getName());
            result = iftttFilter.apply(node);
            if(!result) return false;
        }

        for(IFTTTEvent iftttEvent : iftttEvents)
        {
            Log.d("IFTTTRule", "Applying filter " + iftttEvent.getClass().getName());
            result = iftttEvent.apply(event);
            if(!result) return false;
        }

        for(IFTTTContext iftttContext : iftttContexts)
        {
            Log.d("IFTTTRule", "Applying filter " + iftttContext.getClass().getName());
            result = iftttContext.apply(currentSituation);
            if(!result) return false;
        }

        //All done, run the actions and return true
        for(IFTTTAction action : iftttActions)
        {
            Log.d("IFTTTRule", "Applying filter " + action.getClass().getName());
            action.doAction(context);
        }
        return true;
    }

    /**
     * Adds the component to this rule and links it inside the database
     * @param context current context
     * @param component component to add and link
     */
    public  void linkComponent(Context context, IFTTTComponent component)
    {
        addComponent(component);

        if(ruleid < 0)
        {
            //Don't add the link; Will be added automatically when rule is first saved
            return;
        }

        long componentid = component.getComponentId();

        IFTTTDatabase database = IFTTTDatabase.getHelper(context);
        database.addLink(ruleid, componentid);
    }

    private Spannable listToDescription(Context context, List<? extends IFTTTComponent> list)
    {
        if(list.size() == 0)
        {
            return new SpannableString(context.getString(R.string.no_conditions));
        }

        String description = "";
        for(int i = 0; i < list.size(); i++)
        {
            IFTTTComponent component = list.get(i);
            description += component.getComponentName(context);
            if(i < list.size() - 2)
            {
                description += ", ";
            }
            if(i == list.size() - 2)
            {
                description += String.format(" %s ", context.getString(R.string.connective_and));
            }
            else
            {
                description += " ";
            }
        }
        Spannable spannableDescription = new SpannableString(description);
        spannableDescription.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, list.get(0).getColorId())), 0, description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableDescription;
    }

    public CharSequence getDescription(Context context)
    {
        SpannableStringBuilder description = new SpannableStringBuilder(String.format("%s ", context.getString(R.string.rule_description_start)));

        description.append(listToDescription(context, iftttFilters));
        description.append(String.format(" %s ", context.getString(R.string.clause_when)));
        description.append(listToDescription(context, iftttEvents));
        description.append(String.format(" %s ", context.getString(R.string.clause_if)));
        description.append(listToDescription(context, iftttContexts));
        description.append(String.format(" %s ", context.getString(R.string.clause_then)));
        description.append(listToDescription(context, iftttActions));

        return description;
    }

    @Override
    protected long doSave(Context context, IFTTTDatabase database)
    {
        long ruleid = database.addRule(this.name);
        this.ruleid = ruleid;

        for(IFTTTComponent databasable : iftttFilters)
        {
            long componentid = databasable.save(context);
            database.addLink(ruleid, componentid);
        }

        for(IFTTTComponent databasable : iftttEvents)
        {
            long componentid = databasable.save(context);
            database.addLink(ruleid, componentid);
        }

        for(IFTTTComponent databasable : iftttContexts)
        {
            long componentid = databasable.save(context);
            database.addLink(ruleid, componentid);
        }

        for(IFTTTComponent databasable : iftttActions)
        {
            long componentid = databasable.save(context);
            database.addLink(ruleid, componentid);
        }

        return ruleid;
    }

    @Override
    protected int doUpdate(Context context, IFTTTDatabase database)
    {
        if(this.ruleid == -1) return -1;    //Return -1 in case rule was never saved
        int updated_count = 0;

        database.updateRule(this.ruleid, this.name);

        for(IFTTTComponent databasable : iftttFilters)
        {
            updated_count += databasable.update(context);
        }

        for(IFTTTComponent databasable : iftttEvents)
        {
            updated_count += databasable.update(context);
        }

        for(IFTTTComponent databasable : iftttContexts)
        {
            updated_count += databasable.update(context);
        }

        for(IFTTTComponent databasable : iftttActions)
        {
            updated_count += databasable.update(context);
        }

        return updated_count;
    }

    @Override
    protected int doDelete(Context context, IFTTTDatabase database)
    {
        if(this.ruleid == -1) return -1;    //Return -1 in case rule was never saved
        int deleted_count = 0;

        database.deleteRule(this.ruleid);

        for(IFTTTComponent databasable : iftttFilters)
        {
            deleted_count += databasable.delete(context);
            database.deleteLink(this.ruleid, databasable.getComponentId());
        }

        for(IFTTTComponent databasable : iftttEvents)
        {
            deleted_count += databasable.delete(context);
            database.deleteLink(this.ruleid, databasable.getComponentId());
        }

        for(IFTTTComponent databasable : iftttContexts)
        {
            deleted_count += databasable.delete(context);
            database.deleteLink(this.ruleid, databasable.getComponentId());
        }

        for(IFTTTComponent databasable : iftttActions)
        {
            deleted_count += databasable.delete(context);
            database.deleteLink(this.ruleid, databasable.getComponentId());
        }

        return deleted_count;
    }

    public long getRuleid()
    {
        return ruleid;
    }
}
