package ru.komcity.android.base;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.komcity.android.R;

public class GeoMapUtils {
    private Context context = null;
    private Utils utils = new Utils();
    private double latDef = 50.549923;  // Kms
    private double lngDef = 137.007948; // Kms
    private double latDefmin = 50.0;
    private double latDefmax = 51.0;
    private double lngDefmin = 136.5;
    private double lngDefmax = 137.2;
    private LatLng latLngDef = new LatLng(latDef, lngDef);
    private LatLng latLngDefMin = new LatLng(latDefmin, lngDefmin);
    private LatLng latLngDefMax = new LatLng(latDefmax, lngDefmax);

    public GeoMapUtils(Context context) {
        this.context = context;
        if (this.context == null) {
            try {
                throw new Exception("Context is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            utils = new Utils(this.context);
        }
    }

    public double[] getCoordinatesByAddressDouble(String addressText) {
        double[] coodr = new double[2];
        coodr[0] = latDef;
        coodr[1] = lngDef;

        if (addressText == null)
            return coodr;
        if (addressText.isEmpty())
            return coodr;

        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocationName(addressText, 1);
        } catch (IOException ex) {
            utils.getException(ex);
        }
        if(addresses.size() > 0) {
            coodr[0] = addresses.get(0).getLatitude();
            coodr[1] = addresses.get(0).getLongitude();
        }

        return coodr;
    }

    public LatLng getCoordinatesByAddressLatLng(String addressText) {
        double[] coodr = getCoordinatesByAddressDouble(addressText);
        return new LatLng(coodr[0], coodr[1]);
    }

    public Marker addMarkerToMap(GoogleMap map, LatLng coord, String title, String subTitle) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(coord)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.vector_ic_komcity_logo))
                .title(title)
                .snippet(subTitle));
        return marker;
    }
}
