package ru.komcity.android.forum;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import ru.komcity.android.base.IMainActivityCommand;
import ru.komcity.android.base.ModulesGraph;
import ru.komcity.android.base.Utils;

public class ForumFragment extends Fragment implements IAsyncLoader, IHtmlLoader, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;

    private IMainActivityCommand commandToMainActivity;
    private ForumAdapter adapter;
    private Utils utils;
    private ModulesGraph modules = new ModulesGraph();
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AppCompatActivity mainActivity;

        if (context instanceof AppCompatActivity){
            mainActivity = (AppCompatActivity)context;
            commandToMainActivity = (IMainActivityCommand)mainActivity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        ButterKnife.bind(this, view);

        utils = new Utils();
        commandToMainActivity.onSetTitle(modules.getTitleForum());

        mRecyclerView.setHasFixedSize(true);    // Не будем динамически изменять размеры
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider_red));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(this);
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

    @Override
    public void onRefresh() {
        loadForums();
    }
}
