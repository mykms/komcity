package ru.komcity.mobile.pricemap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.komcity.mobile.R;
import ru.komcity.mobile.base.RequestCodes;
import ru.komcity.mobile.base.Utils;

public class MapPriceMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    private GoogleMap map;
    private Utils utils = new Utils();
    private double latDef = 50.549923;
    private double lngDef = 137.007948;
    private double latDefmin = 50.0;
    private double latDefmax = 51.0;
    private double lngDefmin = 136.5;
    private double lngDefmax = 137.2;
    private LatLng latLngDef = new LatLng(latDef, lngDef);
    private LatLng latLngDefMin = new LatLng(latDefmin, lngDefmin);
    private LatLng latLngDefMax = new LatLng(latDefmax, lngDefmax);

    @BindView(R.id.google_map) MapView googleMapView;

    @OnClick(R.id.btnAddFloat)
    public void btnAddFloat_OnClick() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telMng = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (telMng != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String imei = telMng.getImei();
                } else {
                    String imei = telMng.getDeviceId();
                }
            }
            showDialogAddText();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE}, RequestCodes.PHONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapprice_map_fragment, container, false);
        ButterKnife.bind(this, view);

        utils = new Utils(getActivity());

        googleMapView.onCreate(savedInstanceState);
        googleMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        checkPermissionLocation();
    }

    private void checkPermissionLocation() {
        if ((ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            initMap();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCodes.LOCATION);
        }
    }

    public void initMap() {
        setMapSetting();
        setMapLocation();
        setMapListeners();
    }

    private void setMapSetting() {
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setTiltGesturesEnabled(false);//Жесты наклона
        uiSettings.setRotateGesturesEnabled(true);//Жесты вращения
        uiSettings.setScrollGesturesEnabled(true);//Жесты прокрутки
        uiSettings.setZoomGesturesEnabled(true);//Жесты изменения масштаба
        uiSettings.setZoomControlsEnabled(true);// кнопки изменения масштаба
        uiSettings.setCompassEnabled(true);    // Компас
        try {
            // Кнопка определения текущего местоположения
            uiSettings.setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
        } catch (SecurityException ex) {
            utils.getException(ex);
        }
    }

    private void setMapListeners() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                utils.showMessage("Щелчок по карте",true);
            }
        });
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showDialogInfoForAddPrice();
            }
        });
        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                LatLng latLngCamera = map.getCameraPosition().target;
            }
        });
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng latLngCamera = map.getCameraPosition().target;
            }
        });
    }

    private void setMapLocation() {
        map.moveCamera(CameraUpdateFactory.newLatLng(latLngDef));
        map.addMarker(new MarkerOptions().position(latLngDef).title("Marker"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngDef, 10);
        map.animateCamera(cameraUpdate);
    }

    private void showDialogInfoForAddPrice() {
        AlertDialog.Builder addPriceDialog = new AlertDialog.Builder(getActivity());
        addPriceDialog.setTitle(getString(R.string.btn_add_product_default))
                .setIcon(R.drawable.vector_ic_komcity_logo)
                .setCancelable(true)
                .setMessage("Для добавление новой цены на карте воспользуйтесь кнопкой внизу экрана с правой стороны.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        addPriceDialog.create().show();
    }

    private void showDialogAddText() {
        PriceAddDialog addDialog = new PriceAddDialog(getActivity());
        addDialog.setProductTypes(new HashMap<String, ArrayList<String>>());
        addDialog.setUserInfo("user Petrov I.D.");
        addDialog.setPriceSaveComleteListener(new IProductDBListener() {
            @Override
            public void onSaveProductResult(boolean result) {
                if (result) {
                    (new Utils(getActivity().getApplicationContext())).showMessage("Сбой при добавлении товара", true);
                } else {
                    (new Utils(getActivity().getApplicationContext())).showMessage("Добавлен 1 товар", false);
                }
            }

            @Override
            public void onProductTypesLoadComplete(HashMap<String, ArrayList<String>> items) {

            }

            @Override
            public void onProductLoadComplete(List<Object> items) {

            }
        });
        addDialog.show();
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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
