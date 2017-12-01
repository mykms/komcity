package ru.komcity.android.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class Utils {
    private Context context;
    //private static Utils utils = null;

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

    public void getException(Exception ex) {
        ex.printStackTrace();
    }
}
