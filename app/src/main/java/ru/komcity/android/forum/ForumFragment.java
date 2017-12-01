package ru.komcity.android.forum;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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

public class ForumFragment extends Fragment implements IAsyncLoader, IHtmlLoader {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;

    private ForumAdapter adapter;
    private Utils utils;
    private ModulesGraph modules = new ModulesGraph();
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        ButterKnife.bind(this, view);

        utils = new Utils();
        if (getActivity() != null)
            if (getActivity().getActionBar() != null)
                getActivity().getActionBar().setTitle(modules.getTitleForum());

        mRecyclerView.setHasFixedSize(true);    // Не будем динамически изменять размеры
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefresh.setEnabled(true);
        swipeRefresh.setRefreshing(true); // включаем

        loadForums();

        return view;
    }

    private void loadForums() {
        if (htmlLoader != null) {
            try {
                htmlLoader.htmlAddressToParse("forum/");
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    @Override
    public void onCompletedLoading(Document html) {
        htmlLoader.parseForum(html);
    }

    @Override
    public void onReadyToShow(List<Object> items) {
        adapter = new ForumAdapter(getActivity(), items);
        mRecyclerView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false); // включаем
        adapter.setOnItemClickListener(new ForumClickListener() {
            @Override
            public void onItemClick(ForumItem item) {
                Toast.makeText(getActivity(), item.getDescription(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
