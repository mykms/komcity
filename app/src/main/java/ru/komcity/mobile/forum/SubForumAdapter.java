package ru.komcity.mobile.forum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.mobile.R;

public class SubForumAdapter extends RecyclerView.Adapter<SubForumAdapter.SubForumViewHolder> {
    private List<Object> feedItemList;
    private Context mContext;
    private ForumClickListener onItemClickListener;

    public SubForumAdapter(Context context, List<Object> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public SubForumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subforum_item, null);
        SubForumViewHolder viewHolder = new SubForumViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SubForumViewHolder holder, int position) {
        final ForumItem item = (ForumItem)feedItemList.get(position);

        if (item != null) {
            //Setting text view title
            holder.lbl_title_forum.setText(item.getTitle());
            holder.lbl_date_forum.setText(item.getDescription());
            holder.lbl_count_forum.setText(item.getCountReplic().trim() + item.getCountTheme().trim());

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(item);
                }
            };
            holder.lbl_title_forum.setOnClickListener(listener);
            holder.lbl_date_forum.setOnClickListener(listener);
            holder.lbl_count_forum.setOnClickListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    class SubForumViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lbl_title)  TextView lbl_title_forum;
        @BindView(R.id.lbl_lastdate) TextView lbl_date_forum;
        @BindView(R.id.lbl_countmessage)  TextView lbl_count_forum;

        public SubForumViewHolder(View view) {
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
