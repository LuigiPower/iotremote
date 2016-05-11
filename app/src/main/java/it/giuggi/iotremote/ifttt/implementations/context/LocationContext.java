package it.giuggi.iotremote.ifttt.implementations.context;

import android.view.View;
import android.widget.TextView;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;

/**
 * Created by Federico Giuggioloni on 11/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class LocationContext extends IFTTTContext
{
    private String locationName;
    private double longitude;
    private double latitude;

    public LocationContext()
    {
        this("", 0.0, 0.0);
    }

    public LocationContext(String locationName, double longitude, double latitude)
    {
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean apply(IFTTTCurrentSituation.CurrentSituation context)
    {
        return context.isLocationIn(longitude, latitude, 20.0f);
    }

    @Override
    public int getLayoutResourceId()
    {
        //TODO Show chosen place (Geocoding / place name and that's it?)
        return R.layout.detail_location;
    }

    @Override
    public int getEditLayoutResourceId()
    {
        //TODO place picker
        return R.layout.edit_detail_location;
    }

    @Override
    protected int getComponentNameResourceId()
    {
        return R.string.location_context;
    }

    @Override
    protected void populateView(View view)
    {
        TextView locationName = (TextView) view.findViewById(R.id.location_name);
        locationName.setText(this.locationName);

        TextView currentPlace = (TextView) view.findViewById(R.id.current_place);
        currentPlace.setText("TODO");
    }

    @Override
    protected void populateEditView(View view)
    {
        populateView(view);

        EditText locationName = (EditText) view.
    }
}
