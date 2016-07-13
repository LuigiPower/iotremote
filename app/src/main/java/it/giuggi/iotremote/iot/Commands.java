package it.giuggi.iotremote.iot;

import android.content.res.Resources;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import java.util.ArrayList;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 12/07/16.
 * Class that just contains premade-commands for the nodes
 * TODO in the future, make all this data available in the database, and allow the user to add custom presets
 */
public final class Commands
{
    public static final String CUSTOM_COMMAND = "";
    public static final String SET_GPIO = "gpio%s/%s";
    public static final String SET_MODE = "basic/set/mode/%s";

    public static ArrayList<Pair<String, String>> getCommandList(Resources resources)
    {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>(CUSTOM_COMMAND, resources.getString(R.string.custom_command)));
        list.add(new Pair<>(SET_GPIO, resources.getString(R.string.set_gpio)));
        list.add(new Pair<>(SET_MODE, resources.getString(R.string.set_mode)));
        return list;
    }

    public static String newCommand(String commands, String... fills)
    {
        commands = String.format(commands, (Object[]) fills);
        return commands;
    }
}
