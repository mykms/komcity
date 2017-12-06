package ru.komcity.android.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<Object> feedItemList;
    private Context mContext;
    private NewsClickListener onItemClickListener;

    public NewsAdapter(Context context, List<Object> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, null);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsHolder, int i) {
        final NewsItem item = (NewsItem)feedItemList.get(i);

        //Download image using picasso library
        if (!TextUtils.isEmpty(item.getUrl())) {
            Picasso.with(mContext).load(item.getUrl())
                    .error(R.drawable.vector_ic_news)
                    .placeholder(R.drawable.vector_ic_news)
                    .into(newsHolder.imageNews);
        }

        //Setting text view title
        newsHolder.lbl_title_news.setText(item.getTitle());
        newsHolder.lbl_date_news.setText(item.getDate());
        newsHolder.lbl_text_news.setText(item.getText());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(item);
            }
        };
        newsHolder.imageNews.setOnClickListener(listener);
        newsHolder.lbl_title_news.setOnClickListener(listener);
        newsHolder.lbl_text_news.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_news) ImageView imageNews;
        @BindView(R.id.title_news) TextView lbl_title_news;
        @BindView(R.id.date_news) TextView lbl_date_news;
        @BindView(R.id.text_news) TextView lbl_text_news;

        public NewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public NewsClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(NewsClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
