package ru.komcity.android.announcement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jsoup.nodes.Document;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.AsyncLoader.HtmlLoader;
import ru.komcity.android.base.AsyncLoader.IAsyncLoader;
import ru.komcity.android.base.AsyncLoader.IHtmlLoader;
import ru.komcity.android.base.ModulesGraph;
import ru.komcity.android.base.Utils;

public class AnnouncementFragment extends Fragment implements IAsyncLoader, IHtmlLoader {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;

    private AnnouncementAdapter adapter;
    private Utils utils;
    private ModulesGraph modules = new ModulesGraph();
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);
        ButterKnife.bind(this, view);

        utils = new Utils();
        if (getActivity() != null)
            if (getActivity().getActionBar() != null)
                getActivity().getActionBar().setTitle(modules.getTitleForum());

        mRecyclerView.setHasFixedSize(true);    // Не будем динамически изменять размеры
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefresh.setEnabled(true);
        swipeRefresh.setRefreshing(true); // включаем

        loadAnnouncement();

        return view;
    }

    private void loadAnnouncement() {
        if (htmlLoader != null) {
            try {
                htmlLoader.htmlAddressToParse("board/main/GetVolumes/?h=null");
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    @Override
    public void onCompletedLoading(Document html) {
        htmlLoader.parseAnnouncement(html);
    }

    @Override
    public void onReadyToShow(List<Object> items) {

    }
}
