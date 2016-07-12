package it.giuggi.iotremote.iot;

import android.text.TextUtils;

/**
 * Created by Federico Giuggioloni on 12/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public final class Commands
{
    public static final class Basic
    {
        public static final String group = "basic";

        public static final String get = "get";
        public static final String set = "set";
        public static final String parameter = "%s";
        public static final String value = "%s";
    }

    public static final class Gpio
    {
        public static final String group = "gpio%d";

        public static final String value = "%d";
    }

    public static final class Sensor
    {
        public static final String group = "sensor%d";
    }

    public static String newCommand(String[] commands, String targetNodeName, String... fills)
    {
        String joined = TextUtils.join("/", commands);
        joined = String.format(joined, (Object[]) fills);
        return joined;
    }
}
