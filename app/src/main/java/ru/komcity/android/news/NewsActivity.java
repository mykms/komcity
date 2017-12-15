package ru.komcity.android.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.CustomView.ImageSliderView;
import ru.komcity.android.R;
import ru.komcity.android.base.AsyncLoader.HtmlLoader;
import ru.komcity.android.base.AsyncLoader.IAsyncLoader;
import ru.komcity.android.base.AsyncLoader.IHtmlLoader;
import ru.komcity.android.base.Utils;

public class NewsActivity extends AppCompatActivity implements IAsyncLoader, IHtmlLoader {

    @BindView(R.id.toolbar_top)       Toolbar toolbar;
    @BindView(R.id.date_news)         TextView date_news;
    @BindView(R.id.title_news)        TextView title_news;
    @BindView(R.id.text_news)         TextView text_news;
    @BindView(R.id.image_slider_news) ImageSliderView slider;
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);
    private Utils utils = new Utils();

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

        Intent intent = getIntent();
        if (intent != null) {
            date_news.setText(intent.getStringExtra("DATE"));
            title_news.setText(intent.getStringExtra("TITLE"));
            text_news.setText(intent.getStringExtra("TEXT"));

            loadImages(intent.getStringExtra("URL"));
        }
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
