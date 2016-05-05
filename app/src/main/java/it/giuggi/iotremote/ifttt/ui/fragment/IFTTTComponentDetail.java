package it.giuggi.iotremote.ifttt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.implementations.dummy.DummyComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;
import it.giuggi.iotremote.ui.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 04/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class IFTTTComponentDetail extends BaseFragment
{
    public static final String TAG = "IFTTTCOMPONENTDETAIL";

    private IFTTTComponent component;

    public static IFTTTComponentDetail newInstance(IFTTTComponent component)
    {
        IFTTTComponentDetail fragment = new IFTTTComponentDetail();
        fragment.component = component;
        return fragment;
    }

    @Override
    public String generateTag()
    {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.detail_component, container, false);

        TextView componentName = (TextView) v.findViewById(R.id.component_name);
        componentName.setText(component.getComponentName(getContext()));

        //if(this.component instanceof DummyComponent)
        //{
            //TODO if dummy component, create a Spinner with all available <component_type> types and dynamically load that specific component's edit layout (Just do it inside DummyComponent?)
        //}
        //else
        //{
        ViewGroup details = (ViewGroup) v.findViewById(R.id.component_details);
        this.component.loadEditView(details);
        //}

        return v;
    }

}
