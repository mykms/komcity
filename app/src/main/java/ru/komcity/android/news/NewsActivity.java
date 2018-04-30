package ru.komcity.android.news;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.CustomView.ImageSlider.CompleteLoadImageListener;
import ru.komcity.android.CustomView.ImageSliderView;
import ru.komcity.android.CustomView.ShareToSocial.ShareToSocial;
import ru.komcity.android.R;
import ru.komcity.android.base.AsyncLoader.HtmlLoader;
import ru.komcity.android.base.AsyncLoader.IAsyncLoader;
import ru.komcity.android.base.AsyncLoader.IHtmlLoader;
import ru.komcity.android.base.Utils;

public class NewsActivity extends AppCompatActivity implements IAsyncLoader, IHtmlLoader {
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);
    private Utils utils = new Utils();
    private boolean isShowSocial = false;
    private List<Bitmap> imageBmpList = new ArrayList<>();

    @BindView(R.id.toolbar_top)       Toolbar toolbar;
    @BindView(R.id.date_news)         TextView date_news;
    @BindView(R.id.title_news)        TextView title_news;
    @BindView(R.id.text_news)         TextView text_news;
    @BindView(R.id.image_slider_news) ImageSliderView slider;
    @BindView(R.id.share_to_social)   ShareToSocial shareToSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setTitle(getString(R.string.title_news));
        }

        // Расположение панели с кнопками
        if (slider != null) {
            slider.setRadioPanelGravity(ImageSliderView.Gravity.Bottom);
            slider.setCompleteLoadImageListener(new CompleteLoadImageListener() {
                @Override
                public void onCompleteLoadBMP(Bitmap loadedBmp) {
                    imageBmpList.add(loadedBmp);
                }
            });
        }

        Intent intent = getIntent();
        if (intent != null) {
            date_news.setText(intent.getStringExtra("DATE"));
            title_news.setText(intent.getStringExtra("TITLE"));
            text_news.setText(intent.getStringExtra("TEXT"));
            loadImages(intent.getStringExtra("URL"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    private void loadImages(String newsLink) {
        if (htmlLoader != null) {
            try {
                htmlLoader.htmlAddressToParse(newsLink);
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.news_share:
                isShowSocial = !isShowSocial;
                if (isShowSocial) {
                    shareToSocial.setVisibility(View.VISIBLE);
                    shareToSocial.setTextForShare(text_news.getText().toString());
                    if (imageBmpList.size() > 0) {
                        String fname = "komcity" + Calendar.getInstance().getTime().getTime() + ".jpg";
                        shareToSocial.setBitmapToShare(this, imageBmpList.get(0), fname);
                    }
                } else {
                    shareToSocial.setVisibility(View.GONE);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utils.REQUEST_FILE_SAVE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (imageBmpList.size() > 0) {
                    String fname = "komcity" + Calendar.getInstance().getTime().getTime() + ".jpg";
                    shareToSocial.setBitmapToShare(this, imageBmpList.get(0), fname);
                }
            } else {
                new Utils(getApplicationContext()).showMessage(getString(R.string.info_share_only_text), true);
            }
        }
    }

    @Override
    public void onCompletedLoading(Document html) {
        htmlLoader.parseNewsImageLinks(html);
    }

    @Override
    public void onReadyToShow(List<Object> items) {
        slider.setItems(items);
    }
}
