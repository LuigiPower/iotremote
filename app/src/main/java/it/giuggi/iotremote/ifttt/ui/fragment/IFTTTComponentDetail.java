package it.giuggi.iotremote.ifttt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.implementations.dummy.DummyComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTComponent;
import it.giuggi.iotremote.ifttt.structure.IFTTTRule;
import it.giuggi.iotremote.ui.fragment.BaseFragment;

/**
 * Created by Federico Giuggioloni on 04/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class IFTTTComponentDetail extends BaseFragment implements View.OnClickListener
{
    public static final String TAG = "IFTTTCOMPONENTDETAIL";

    private List<IFTTTComponent> listWithDummy;
    private IFTTTRule owner;
    private IFTTTComponent component;

    public static IFTTTComponentDetail newInstance(List<IFTTTComponent> listWithDummy, IFTTTRule owner, IFTTTComponent component)
    {
        IFTTTComponentDetail fragment = new IFTTTComponentDetail();
        fragment.listWithDummy = listWithDummy;
        fragment.owner = owner;
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
        View v = inflater.inflate(R.layout.component_detail_fragment, container, false);

        Toolbar componentName = (Toolbar) v.findViewById(R.id.component_toolbar);
        componentName.setTitle(component.getComponentName(getContext()));
        componentName.setBackgroundResource(component.getColorId());

        ViewGroup details = (ViewGroup) v.findViewById(R.id.component_details);
        component.loadEditView(details);

        View complete = v.findViewById(R.id.complete_component);
        complete.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v)
    {
        IFTTTComponent tosave = component;

        if(component instanceof DummyComponent)
        {
            tosave = ((DummyComponent) component).getWorkInProgress();
        }

        //If component already esists, update it
        //Else save it
        if(tosave.update(getContext()) <= 0)
        {
            tosave.save(getContext());
            owner.linkComponent(getContext(), tosave);
            listWithDummy.add(0, tosave);
        }

        controller.goBack();
    }
}
