package ru.komcity.mobile.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
    private Context context;
    public static final int REQUEST_FILE_SAVE = 15;

    public Utils(Context mContext) {
        context = mContext;
    }

    public Utils() {
        //void
    }
    /*
    public static Utils getInstance() {
        if (utils == null)
            utils = new Utils();
        return utils;
    }
    */
    public boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void showMessage(String messageText, boolean isShortDuration) {
        int duration = Toast.LENGTH_LONG;
        if (isShortDuration)
            duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, messageText, duration).show();
    }

    public void showMessageSnackbar(String messageText, View view) {
        Snackbar.make(view, messageText, Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     * Сохранение Bitmap-рисунка на файловой системе в папке Pictures
     * @param context
     * @param image Рисунок
     * @param fileName Имя файл (*.jpg)
     * @return Результат сохранения. true - в случае успеха, иначе false
     */
    public boolean saveImageLocal(Activity context, Bitmap image, String fileName) {
        boolean result = false;
        if (context == null || image == null || fileName == null)
            return result;
        // Check permission
        if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!folder.exists()) {
                if (!folder.mkdirs())
                    return result;
            }

            File file = new File(folder, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            try {
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file.getAbsoluteFile());
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    result = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return result;
                } finally {
                    if (stream != null) {
                        stream.flush();
                        stream.close();
                    }
                    return result;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            //request permission
            ActivityCompat.requestPermissions(context, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_FILE_SAVE);
            return result;
        }
    }

    public void getException(Exception ex) {
        ex.printStackTrace();
    }
}
