package ru.komcity.android.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_top)    Toolbar toolbar;
    @BindView(R.id.date_news)    TextView date_news;
    @BindView(R.id.title_news)    TextView title_news;
    @BindView(R.id.img_news)    ImageView img_news;
    @BindView(R.id.text_news)    TextView text_news;
    @BindView(R.id.image_slider_news)    ViewPager image_slider;

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
            String test_url = intent.getStringExtra("URL");
            text_news.setText(intent.getStringExtra("TEXT"));

            List<String> listImg = new ArrayList<String>();
            listImg.add(test_url);

            NewsSliderAdapter adapter = new NewsSliderAdapter(this);
            image_slider.setAdapter(adapter);
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

    public class NewsSliderAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater inflater;

        public NewsSliderAdapter(Context mContext) {
            context = mContext;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == ((LinearLayout) object));
        }

        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, int position) {

            View view = inflater.inflate(R.layout.slider_item, container, false);
            container.addView(view);

            TextView text = (TextView) view.findViewById(R.id.sdefdasfasdf);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "asdfasdfd", Toast.LENGTH_LONG).show();
                }
            });

            return view;
        }
    }
}
