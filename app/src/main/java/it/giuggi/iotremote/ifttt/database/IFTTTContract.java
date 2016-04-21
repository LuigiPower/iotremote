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

    public static abstract class IFTTTFilterEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "IFTTTFilter";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MODE = "mode";
    }

    public static abstract class IFTTTEventEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "IFTTTFilter";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MODE = "mode";
    }
}
