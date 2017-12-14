package ru.komcity.android.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_top) Toolbar toolbar;
    @BindView(R.id.date_news)   TextView date_news;
    @BindView(R.id.title_news)  TextView title_news;
    @BindView(R.id.img_news)    ImageView img_news;
    @BindView(R.id.text_news)   TextView text_news;

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
            //intent.getStringExtra("URL")
            text_news.setText(intent.getStringExtra("TEXT"));
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
}