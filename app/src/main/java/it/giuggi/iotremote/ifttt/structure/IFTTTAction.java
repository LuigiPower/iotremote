package it.giuggi.iotremote.ifttt.structure;

import it.giuggi.iotremote.iot.IOTNode;

/**
 * Created by Federico Giuggioloni on 14/04/16.
 * IFTTTAction
 * An action to be done if the IFTTTRule matches Filter, Event and Context
 */
public abstract class IFTTTAction
{

    /**
     * Action is one supported by the targetNode
     *  /gpio1/1
     *  /gpio1
     *  /dashbooard
     *  ...
     *
     * Or anything considering doAction can be overridden in any way
     * it could be a constant chosen from anywhere else, then checked inside doAction
     *
     * TODO how do I save it inside the database then?
     * Solution: Subclasses should only use action and targetNode to do things, which means I can
     * save those two things into the database and (maybe) the action_type (which is the subclass
     * name itself). This means I can reinstantiate that class when needed, while keeping flexibility
     * Should be useless: subclasses create them if needed and override doaction
     */
    //public IOTNode targetNode;
    //public String action;

    /**
     * Override to create a custom action
     */
    public abstract void doAction();
}
