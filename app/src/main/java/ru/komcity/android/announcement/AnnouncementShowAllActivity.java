package ru.komcity.android.announcement;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import org.jsoup.nodes.Document;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.AsyncLoader.HtmlLoader;
import ru.komcity.android.base.AsyncLoader.IAsyncLoader;
import ru.komcity.android.base.AsyncLoader.IHtmlLoader;
import ru.komcity.android.base.Utils;

public class AnnouncementShowAllActivity extends AppCompatActivity implements IAsyncLoader, IHtmlLoader {
    @BindView(R.id.toolbar_top) Toolbar toolbar;
    @BindView(R.id.announcement_web_content) WebView web_content;

    private static String EXTRA_LINK_ID = "EXTRA_LINK_ID";
    private static String EXTRA_TOOLBAR_TITLE = "EXTRA_TOOLBAR_TITLE";
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);
    private Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_show_all_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        String title = "Просмотр объявлений";
        Intent intent = getIntent();
        if (intent != null) {
            loadAnnouncement(intent.getStringExtra(EXTRA_LINK_ID));
            String dopTitle = intent.getStringExtra(EXTRA_TOOLBAR_TITLE);
            if (dopTitle != null && dopTitle.isEmpty())
                title += " " + dopTitle;
        }

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setTitle(title);
        }


    }

    private void loadAnnouncement(String newsLink) {
        if (htmlLoader != null) {
            try {
                if (newsLink != null && !newsLink.isEmpty())
                    htmlLoader.htmlAddressToParse("board/main/getPubAdverts/?id=" + newsLink);
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
        //
    }

    @Override
    public void onReadyToShow(List<Object> items) {
        //
    }
}
