package it.giuggi.iotremote.ifttt.implementations.action;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.AutoCompleteEntry;
import it.giuggi.iotremote.ifttt.structure.AutoCompleteStringEntry;
import it.giuggi.iotremote.ifttt.structure.IFTTTAction;
import it.giuggi.iotremote.ifttt.ui.adapter.AutoCompleteAdapter;
import it.giuggi.iotremote.ifttt.ui.adapter.TextSpinnerAdapter;
import it.giuggi.iotremote.iot.node.Commands;
import it.giuggi.iotremote.net.WebRequestTask;

/**
 * Created by Federico Giuggioloni on 21/04/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class SendCommandAction extends IFTTTAction
{
    private String target;
    private String incomplete_command;
    private String[] parameters;

    public SendCommandAction()
    {
        this("", "");
    }

    public SendCommandAction(String target, String action)
    {
        this.target = target;
        this.incomplete_command = action;
    }

    @Override
    public void doAction(Context context)
    {
        /**
         * Had to take code from IoTNode for serialization purposes
         */
        Bundle data = new Bundle();
        data.putStringArray(WebRequestTask.DATA, new String[]{
                Commands.newCommand(incomplete_command, parameters),
                target
        });

        WebRequestTask.perform(WebRequestTask.Azione.SEND_TEST_COMMAND)
                .with(data)
                .listen(new WebRequestTask.OnResponseListener()
                {
                    @Override
                    public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
                    {
                        //Done
                    }
                })
                .send();
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_send_command;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        return R.layout.edit_detail_send_command;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.send_command_action;
    }

    @Override
    protected void populateView(View view)
    {
        TextView tx = (TextView) view.findViewById(R.id.command_name);

        boolean found = false;
        ArrayList<Pair<String, String>> list = Commands.getCommandList(view.getResources());
        for(Pair<String, String> pair : list)
        {
            if(pair.first.equalsIgnoreCase(incomplete_command))
            {
                tx.setText(pair.second);
                found = true;
                break;
            }
        }

        if(!found)
        {
            Pair<String, String> custom = list.get(0);
            tx.setText(custom.second);

            TextView customCommand = (TextView) view.findViewById(R.id.custom_command);
            customCommand.setText(incomplete_command);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void populateEditView(View view)
    {
        //TODO better layout
        final EditText customCommand = (EditText) view.findViewById(R.id.custom_command);
        customCommand.setText(this.incomplete_command);
        final ViewGroup parameterContainer = (ViewGroup) view.findViewById(R.id.parameter_container);

        Spinner preset = (Spinner) view.findViewById(R.id.command_preset);
        ArrayList<Pair<String, String>> list = Commands.getCommandList(view.getResources());

        TextSpinnerAdapter adapter = new TextSpinnerAdapter(view.getContext(), R.layout.autocompletetextentry, list);
        preset.setAdapter(adapter);

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).first.equalsIgnoreCase(incomplete_command))
            {
                preset.setSelection(i);
                break;
            }
        }

        if(!incomplete_command.isEmpty())
        {
            customCommand.setVisibility(View.GONE);
        }
        else
        {
            customCommand.setVisibility(View.VISIBLE);
        }

        preset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Pair<String, String> selected = (Pair<String, String>) parent.getItemAtPosition(position);
                incomplete_command = selected.first;

                parameterContainer.removeAllViews();

                if(!incomplete_command.isEmpty())
                {
                    customCommand.setVisibility(View.GONE);

                    int count = 0;
                    int idx = 0;

                    while ((idx = incomplete_command.indexOf("%s", idx)) != -1)
                    {
                        idx++;
                        count++;
                    }

                    parameters = new String[count];
                    for(int i = 0; i < count; i++)
                    {
                        final EditText param = new EditText(parameterContainer.getContext());
                        param.setTag(i);
                        param.setHint("Param " + i);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            param.setTextAppearance(R.style.StandardEditText);
                        }
                        else
                        {
                            param.setTextAppearance(param.getContext(), R.style.StandardEditText);
                        }

                        parameterContainer.addView(param);

                        param.addTextChangedListener(new TextWatcher()
                        {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                            {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count)
                            {
                                int i = (Integer) param.getTag();
                                parameters[i] = s.toString();
                            }

                            @Override
                            public void afterTextChanged(Editable s)
                            {

                            }
                        });
                    }
                }
                else
                {
                    customCommand.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final AutoCompleteTextView nodename = (AutoCompleteTextView) view.findViewById(R.id.node_name);
        nodename.setText(this.target);

        nodename.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                AutoCompleteStringEntry entry = (AutoCompleteStringEntry) parent.getItemAtPosition(position);
                target = entry.getString();
                nodename.setText(entry.getText());
            }
        });

        WebRequestTask.perform(WebRequestTask.Azione.GET_NODE_LIST)
                .listen(new WebRequestTask.OnResponseListener()
                {
                    @Override
                    public void onResponseReceived(Object ris, WebRequestTask.Tipo t, Object... datiIniziali)
                    {
                        JSONArray array = (JSONArray) ris;

                        ArrayList<AutoCompleteEntry> list = new ArrayList<>(10);
                        for(int i = 0; i < array.length(); i++)
                        {
                            try
                            {
                                JSONObject obj = array.getJSONObject(i);
                                list.add(new AutoCompleteStringEntry(obj.getString("name")));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        AutoCompleteAdapter adapter = new AutoCompleteAdapter(nodename.getContext(), R.layout.autocompletetextentry, list);
                        nodename.setAdapter(adapter);
                    }
                })
                .send();

        nodename.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                target = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_backup_24dp;
    }
}
