package ru.komcity.android.pricemap;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class MapPriceMapFragment extends Fragment implements OnMapReadyCallback {
    @BindView(R.id.google_map)
    MapView googleMapView;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapprice_map_fragment, container, false);
        ButterKnife.bind(this, view);

        googleMapView.onCreate(savedInstanceState);
        googleMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(43.1, -87.9);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
/*
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
    }

    @Override
    public void onResume() {
        googleMapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        googleMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        googleMapView.onLowMemory();
    }
}
