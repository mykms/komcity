package ru.komcity.android.announcement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    private String newsLinkID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_show_all_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        String title = "Просмотр объявлений";
        Intent intent = getIntent();
        if (intent != null) {
            newsLinkID = intent.getStringExtra(EXTRA_LINK_ID);
            loadAnnouncement(newsLinkID);
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
        // Отображение капчи http://komcity.ru/board/main/GetContact/?id=2546749&h=3
        // id=2546749 - это идентификатор объявления
        Element elem_head = html.head();
        elem_head.append("<script type=\"text/javascript\" src=\"http://www.komcity.ru/boardjs/km.js\"></script>");
        elem_head.append("<script type=\"text/javascript\" src=\"http://www.komcity.ru/boardplugs/fancybox/jquery.fancybox.pack.js\"></script>");
        elem_head.append("<script type=\"text/javascript\" src=\"http://www.komcity.ru/boardjs/jquery-1.10.2.min.js\"></script>");
        web_content.setWebChromeClient(new WebChromeClient());
        web_content.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        web_content.getSettings().setJavaScriptEnabled(true);
        web_content.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_content.loadDataWithBaseURL(null, html.toString(),"text/html", "UTF-8", null);
    }

    @Override
    public void onReadyToShow(List<Object> items) {
        //
    }
}
