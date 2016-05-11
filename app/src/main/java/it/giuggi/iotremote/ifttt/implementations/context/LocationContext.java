package it.giuggi.iotremote.ifttt.implementations.context;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import it.giuggi.iotremote.R;
import it.giuggi.iotremote.ifttt.structure.IFTTTContext;
import it.giuggi.iotremote.ifttt.structure.IFTTTCurrentSituation;

/**
 * Created by Federico Giuggioloni on 11/05/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class LocationContext extends IFTTTContext implements OnMapReadyCallback
{
    public transient static final int PLACE_PICKER_REQUEST = 9049;
    public transient static LocationContext placeRequester;

    private transient MapView mapView;
    private transient GoogleMap googleMap;
    private transient TextView selectedPlace;

    private String locationName;
    private String placeName; //So I don't need Geocoding
    private double longitude;
    private double latitude;

    public LocationContext()
    {
        this("", "", 0.0, 0.0);
    }

    public LocationContext(String locationName, String placeName, double longitude, double latitude)
    {
        this.locationName = locationName;
        this.placeName = placeName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_pin_drop_24dp;
    }

    @Override
    public boolean apply(IFTTTCurrentSituation.CurrentSituation context)
    {
        return context.isLocationIn(longitude, latitude, 20.0f);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.detail_location;
    }

    @Override
    public int getEditLayoutResourceId()
    {
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
        currentPlace.setText(placeName);

        /*
        AppCompatActivity activity = (AppCompatActivity) getActivity(view);
        if(activity != null)
        {
            SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.detail_location_map);
            mapFragment.getMapAsync(this);
        }
        */
        mapView = (MapView) view.findViewById(R.id.detail_location_map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    protected void populateEditView(View view)
    {
        EditText editLocation = (EditText) view.findViewById(R.id.location_name);
        editLocation.setText(locationName);

        editLocation.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                locationName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        selectedPlace = (TextView) view.findViewById(R.id.current_place);

        if(placeName.isEmpty())
        {
            selectedPlace.setText(R.string.choose_place_dots);
        }
        else
        {
            selectedPlace.setText(placeName);
        }

        Button pickPlace = (Button) view.findViewById(R.id.pick_place);
        pickPlace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    placeRequester = LocationContext.this;
                    Activity host = getActivity(v);
                    if(host != null)
                    {
                        Intent intent = new PlacePicker.IntentBuilder().build(host);
                        host.startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    }
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void doneSelectingPlace(Context context, Intent data)
    {
        Place place = PlacePicker.getPlace(context, data);
        placeName = place.getName().toString();
        selectedPlace.setText(placeName);
        LatLng location = place.getLatLng();

        this.longitude = location.longitude;
        this.latitude = location.latitude;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(mapView.getContext());
        this.googleMap = googleMap;
        LatLng position = new LatLng(this.latitude, this.longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13.0f));
        googleMap.addMarker(new MarkerOptions().position(position).title(placeName));
        mapView.onResume();
    }

    @Override
    protected void cleanupView(View view)
    {
        super.cleanupView(view);

        if (googleMap != null)
        {
            googleMap.clear();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    }
}
