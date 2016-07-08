package it.giuggi.iotremote.ifttt.implementations.context;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * WifiNameContext checks if the phone is currently connected to the specified WiFi network
 */
public class WifiNameContext extends IFTTTContext
{
    private transient TextWatcher update = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            ssid = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    private String ssid;

    public WifiNameContext()
    {
        this("");
    }

    public WifiNameContext(String ssid)
    {
        this.ssid = ssid;
    }

    @Override
    public boolean apply(IFTTTCurrentSituation.CurrentSituation context)
    {
        return context.isConnectedTo(this.ssid);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_wifi_name;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_wifi_name;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.wifi_name_context;
    }

    @Override
    protected void populateView(View view)
    {
        TextView wifiname = (TextView) view.findViewById(R.id.wifi_name);
        wifiname.setText(this.ssid);

        wifiname.addTextChangedListener(update);
    }

    @Override
    protected void populateEditView(View view)
    {
        EditText wifiname = (EditText) view.findViewById(R.id.wifi_name);
        wifiname.setText(this.ssid);

        wifiname.addTextChangedListener(update);
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_signal_wifi_4_bar_24dp;
    }
}
