package ru.komcity.android.news;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.jsoup.nodes.Document;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.AsyncLoader.HtmlLoader;
import ru.komcity.android.base.AsyncLoader.IAsyncLoader;
import ru.komcity.android.base.AsyncLoader.IHtmlLoader;
import ru.komcity.android.base.ModulesGraph;
import ru.komcity.android.base.Utils;
import java.util.List;

public class NewsFragment extends Fragment implements IAsyncLoader, IHtmlLoader {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;

    private NewsAdapter adapter;
    private Utils utils;
    private ModulesGraph modules = new ModulesGraph();
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        utils = new Utils();
        if (getActivity() != null)
            if (getActivity().getActionBar() != null)
                getActivity().getActionBar().setTitle(modules.getTitleNews());

        mRecyclerView.setHasFixedSize(true);    // Не будем динамически изменять размеры
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                                                            mRecyclerView.getContext(),
                                                            layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider_red));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        swipeRefresh.setEnabled(true);
        swipeRefresh.setRefreshing(true); // включаем

        loadNews();

        return view;
    }

    private void loadNews() {
        if (htmlLoader != null) {
            try {
                htmlLoader.htmlAddressToParse("news/");
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    @Override
    public void onCompletedLoading(Document html) {
        htmlLoader.parseNews(html);
    }

    @Override
    public void onReadyToShow(List<Object> items) {
        adapter = new NewsAdapter(getActivity(), items);
        mRecyclerView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false); // включаем
        adapter.setOnItemClickListener(new NewsClickListener() {
            @Override
            public void onItemClick(NewsItem item) {
                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
