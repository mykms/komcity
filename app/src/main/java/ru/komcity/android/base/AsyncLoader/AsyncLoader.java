package ru.komcity.android.base.AsyncLoader;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ru.komcity.android.base.Utils;

public class AsyncLoader extends AsyncTask<Void, Void, Void> {
    private String fullAddress = "";
    private IAsyncLoader iAsyncLoader = null;
    private Document html = null;
    private Utils utils = new Utils();

    public AsyncLoader(IAsyncLoader mIAsyncLoader, String mFullAddress) {
        iAsyncLoader = mIAsyncLoader;
        fullAddress = mFullAddress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            //Считываем заглавную страницу
            html = Jsoup.connect(fullAddress).get();
        } catch (Exception ex) {
            utils.getException(ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (iAsyncLoader != null)
            iAsyncLoader.onCompletedLoading(html);
    }
}
