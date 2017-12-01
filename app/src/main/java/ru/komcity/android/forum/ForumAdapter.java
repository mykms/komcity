package ru.komcity.android.forum;

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

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {
    private List<Object> feedItemList;
    private Context mContext;
    private ForumClickListener onItemClickListener;

    public ForumAdapter(Context context, List<Object> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forum_item, null);
        ForumViewHolder viewHolder = new ForumViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForumViewHolder newsHolder, int i) {
        final ForumItem item = (ForumItem)feedItemList.get(i);

        //Setting text view title
        newsHolder.lbl_title_forum.setText(item.getTheme());
        newsHolder.lbl_date_forum.setText(item.getCountThemeReplic());
        newsHolder.lbl_text_forum.setText(item.getDescription());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(item);
            }
        };
        newsHolder.imageForum.setOnClickListener(listener);
        newsHolder.lbl_title_forum.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    class ForumViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_forum)   ImageView imageForum;
        @BindView(R.id.title_forum) TextView lbl_title_forum;
        @BindView(R.id.date_forum)  TextView lbl_date_forum;
        @BindView(R.id.text_forum)  TextView lbl_text_forum;

        public ForumViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ForumClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(ForumClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
