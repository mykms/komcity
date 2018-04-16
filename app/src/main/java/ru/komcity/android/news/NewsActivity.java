package ru.komcity.android.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.jsoup.nodes.Document;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
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
            ab.setTitle("Просмотр новости");
        }

        // Расположение панели с кнопками
        if (slider != null) {
            slider.setRadioPanelGravity(ImageSliderView.Gravity.Bottom);
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
                if (isShowSocial) {
                    shareToSocial.setVisibility(View.VISIBLE);
                } else {
                    shareToSocial.setVisibility(View.GONE);
                }
                isShowSocial = !isShowSocial;

                return true;
        }
        return super.onOptionsItemSelected(item);
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
