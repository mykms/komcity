package ru.komcity.android.forum;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.Utils;

public class ForumDetailAdapter extends RecyclerView.Adapter<ForumDetailAdapter.ForumDetailViewHolder> {
    private List<Object> feedItemList;
    private Context mContext;
    private Utils utils = null;
    private ISocialShowListener socialShowListener = null;

    public ForumDetailAdapter(Context context, List<Object> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        utils = new Utils(mContext);
    }

    @Override
    public ForumDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forum_detail_item, null);
        ForumDetailViewHolder viewHolder = new ForumDetailViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v);
                return true;
            }
        });
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

    /**
     * Показывает выпадающее меню
     * @param v Запускающий элемент
     */
    private void showPopupMenu(final View v) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.forum_popup);

        @SuppressLint("RestrictedApi")
        MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popupMenu.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END|Gravity.TOP);
        menuHelper.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ForumDetailViewHolder viewHolder = new ForumDetailViewHolder(v);
                String textForShare = viewHolder.lbl_title_forum.getText().toString();
                switch (item.getItemId()) {
                    case R.id.menu_copy_text:
                        copyToClipBoardBuffer(mContext, textForShare);
                        utils.showMessage(mContext.getString(R.string.copied_text), true);
                        return true;
                    case R.id.menu_share_to_social:
                        if (socialShowListener != null) {
                            socialShowListener.showSocial(true, textForShare);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                menu.dismiss();
            }
        });
    }

    public void setShowSocialListener(ISocialShowListener mSocialListener) {
        socialShowListener = mSocialListener;
    }

    private void copyToClipBoardBuffer(Context context, String text) {
        if (text == null) {
            text = "";
        }
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("forum_copy_text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
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
