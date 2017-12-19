package ru.komcity.android.forum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class ForumDetailAdapter extends RecyclerView.Adapter<ForumDetailAdapter.ForumDetailViewHolder> {
    private List<Object> feedItemList;
    private Context mContext;

    public ForumDetailAdapter(Context context, List<Object> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public ForumDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forum_detail_item, null);
        ForumDetailViewHolder viewHolder = new ForumDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForumDetailViewHolder holder, int position) {
        final ForumDetailItem item = (ForumDetailItem)feedItemList.get(position);

        if (item != null) {
            holder.lbl_title_forum.setText(item.getText());
            holder.lbl_date_forum.setText(item.getDate());
            holder.lbl_count_forum.setText(item.getName());
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    class ForumDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lbl_title)    TextView lbl_title_forum;
        @BindView(R.id.lbl_lastdate) TextView lbl_date_forum;
        @BindView(R.id.lbl_countmessage)  TextView lbl_count_forum;

        public ForumDetailViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
