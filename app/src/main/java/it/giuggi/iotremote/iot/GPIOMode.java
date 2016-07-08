package it.giuggi.iotremote.iot;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 15/03/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class GPIOMode extends IOperatingMode
{
    private static final String GPIO_GROUP = "gpio";

    public static final String NAME = "gpio_mode";

    protected CheckBox gpioStatus = null;
    protected int gpio = 0;

    public GPIOMode()
    {
        super();
    }

    public GPIOMode(JSONObject params)
    {
        super(params);

        try
        {
            gpio = params.getInt("gpio");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * TODO remove this, only for debugging
     */
    protected WebRequestTask.OnResponseListener emptyResponse = new WebRequestTask.OnResponseListener()
    {
        @Override
        public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
        {
            JSONObject result = (JSONObject) ris;
            Log.i("GPIOMode Response", "Result is " + result);
        }
    };

    protected CompoundButton.OnCheckedChangeListener onStatusChange = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            int val = isChecked ? 1 : 0;
            owner.sendCommand(GPIO_GROUP + gpio + "/" + val, emptyResponse);
        }
    };

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public View loadDashboardLayout(LayoutInflater inflater, ViewGroup container)
    {
        View v = inflater.inflate(R.layout.gpio_mode, container, false);
        gpioStatus = (CheckBox) v.findViewById(R.id.gpio_status);
        loadCheckBox(gpioStatus);
        return v;
    }

    @Override
    public void destroyDashboardLayout(ViewGroup container)
    {

    }

    @SuppressLint("SetTextI18n")
    private void loadCheckBox(CheckBox v)
    {
        v.setOnCheckedChangeListener(null);
        v.setChecked(gpio > 0);
        v.setOnCheckedChangeListener(onStatusChange);
        v.setText("GPIO " + gpio);
    }

    @Override
    public void valueUpdate(JSONObject newParameters) throws JSONException
    {
        this.gpio = newParameters.getInt("gpio");
        loadCheckBox(gpioStatus);
    }

}
