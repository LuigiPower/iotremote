package it.giuggi.iotremote.ifttt.database;

import android.provider.BaseColumns;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * IFTTTContract
 * Contains all information about IFTTTDatabase tables
 */
public final class IFTTTContract
{
    public IFTTTContract() {}

    public static abstract class IFTTTRuleEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "IFTTTRule";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static abstract class IFTTTRuleComponentEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "IFTTTRuleComponent";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_GSON = "gson";
        public static final String COLUMN_NAME_TYPE = "type";   //FILTER, EVENT, CONTEXT, ACTION
        public static final String COLUMN_NAME_CLASS_NAME = "class_name";   //class name
    }

    public static abstract class IFTTTComponentLinkEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "IFTTTComponentLinkEntry";
        public static final String COLUMN_NAME_RULE_ID = "ruleid";
        public static final String COLUMN_NAME_COMPONENT_ID = "componentid";
    }

    public static abstract class IFTTTEventLog implements BaseColumns
    {
        public static final String TABLE_NAME = "IFTTTEventLog";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_SENDER_NAME = "sender";
        public static final String COLUMN_NAME_MODE_NAME = "mode";
        public static final String COLUMN_NAME_GSON = "gson";
        public static final String COLUMN_NAME_CLASS_NAME = "class_name";   //class name

    }

    public static final String INTEGER_PRIMARY_KEY = "INTEGER PRIMARY KEY";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String FOREIGN_KEY = "FOREIGN KEY";
    public static final String ON_DELETE_CASCADE = "ON DELETE CASCADE";
    public static final String ON_UPDATE_CASCADE = "ON UPDATE CASCADE";
    public static final String DATETIME = "DATETIME";
    public static final String DEFAULT = "DEFAULT";
    public static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
    public static final String REFERENCES = "REFERENCES";
    public static final String INTEGER = "INTEGER";
    public static final String TEXT = "TEXT";
    public static final String SPACE = " ";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String ADD = "ADD";
    public static final String CONSTRAINT = "CONSTRAINT";

    public static final String SQL_CREATE_RULE_ENTRIES =
            "CREATE TABLE " + IFTTTRuleEntry.TABLE_NAME + " ( " +
                    IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID + SPACE + INTEGER_PRIMARY_KEY + COMMA +
                    IFTTTRuleEntry.COLUMN_NAME_NAME + SPACE + TEXT +
                    ")";

    public static final String SQL_CREATE_RULECOMPONENT_ENTRIES =
            "CREATE TABLE " + IFTTTRuleComponentEntry.TABLE_NAME + " ( " +
                    IFTTTRuleComponentEntry.COLUMN_NAME_ENTRY_ID + SPACE + INTEGER_PRIMARY_KEY + COMMA +
                    IFTTTRuleComponentEntry.COLUMN_NAME_GSON + SPACE + TEXT + COMMA +
                    IFTTTRuleComponentEntry.COLUMN_NAME_TYPE + SPACE + TEXT + COMMA +
                    IFTTTRuleComponentEntry.COLUMN_NAME_CLASS_NAME + SPACE + TEXT +
                    ")";

    public static final String SQL_CREATE_COMPONENT_LINK_ENTRIES =
            "CREATE TABLE " + IFTTTComponentLinkEntry.TABLE_NAME + " ( " +
                    IFTTTComponentLinkEntry.COLUMN_NAME_COMPONENT_ID + SPACE + INTEGER + COMMA +
                    IFTTTComponentLinkEntry.COLUMN_NAME_RULE_ID + SPACE + INTEGER + COMMA +
                    PRIMARY_KEY + "(" +
                        IFTTTComponentLinkEntry.COLUMN_NAME_COMPONENT_ID + COMMA + IFTTTComponentLinkEntry.COLUMN_NAME_RULE_ID + " ) " + COMMA +
                    FOREIGN_KEY + "(" + IFTTTComponentLinkEntry.COLUMN_NAME_RULE_ID + ")" + SPACE +
                        REFERENCES + SPACE + IFTTTRuleEntry.TABLE_NAME + "(" + IFTTTRuleEntry.COLUMN_NAME_ENTRY_ID + ")" + SPACE + ON_DELETE_CASCADE + COMMA +
                    FOREIGN_KEY + "(" + IFTTTComponentLinkEntry.COLUMN_NAME_COMPONENT_ID + ")" + SPACE +
                        REFERENCES + SPACE + IFTTTRuleComponentEntry.TABLE_NAME + "(" + IFTTTRuleComponentEntry.COLUMN_NAME_ENTRY_ID + ")" + SPACE + ON_DELETE_CASCADE  +
                    ")";

    public static final String SQL_CREATE_EVENTLOG_ENTRIES =
            "CREATE TABLE " + IFTTTEventLog.TABLE_NAME + " ( " +
                    IFTTTEventLog.COLUMN_NAME_ENTRY_ID + SPACE + INTEGER_PRIMARY_KEY + COMMA +
                    IFTTTEventLog.COLUMN_NAME_TIMESTAMP + SPACE + DATETIME + SPACE + DEFAULT + SPACE + CURRENT_TIMESTAMP + COMMA +
                    IFTTTEventLog.COLUMN_NAME_GSON + SPACE + TEXT + COMMA +
                    IFTTTEventLog.COLUMN_NAME_TYPE + SPACE + TEXT + COMMA +
                    IFTTTEventLog.COLUMN_NAME_SENDER_NAME + SPACE + TEXT + COMMA +
                    IFTTTEventLog.COLUMN_NAME_MODE_NAME + SPACE + TEXT + COMMA +
                    IFTTTEventLog.COLUMN_NAME_CLASS_NAME + SPACE + TEXT +
                    ")";


}
