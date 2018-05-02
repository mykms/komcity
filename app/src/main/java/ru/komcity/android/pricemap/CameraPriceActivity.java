package ru.komcity.android.pricemap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import ru.komcity.android.R;
import ru.komcity.android.base.Utils;

public class CameraPriceActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERM = 35;
    private Camera camera = null;
    private Utils utils = new Utils(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_price_activity);

        checkPermission();
        checkGoogleServiceAvailability();
    }

    private void checkPermission() {
        // Проверка на доступ к камере
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            createCameraSource();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERM);
        }
    }

    private void checkGoogleServiceAvailability() throws SecurityException {
        // Проверка поддержки устройства сервиса Гугла
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            //Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            //dlg.show();
        } else {
            /*
            if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
                }
             */
        }
    }

    private void createCameraSource() {
        camera = Camera.open();
        /*
        Context context = getApplicationContext();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay);
        barcodeFactory.registerCallBackFactory(this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, "Face detector dependencies cannot be downloaded due to low device storage", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Face detector dependencies cannot be downloaded due to low device storage");
            }
        }

        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);

        // автофокус и вспышка
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }
        mCameraSource = builder.setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null).build();
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        // Если есть доступ
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            utils.showMessage("Доступ к камере запрещен.\nВы не сможете сфотографировать цену!", true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
        }
    }
}
