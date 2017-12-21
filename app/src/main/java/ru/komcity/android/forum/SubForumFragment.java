package ru.komcity.android.forum;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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

public class SubForumFragment extends Fragment implements IAsyncLoader, IHtmlLoader, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);
    private ModulesGraph modules = new ModulesGraph();
    private IForumActivityCommand commandToMainActivity;
    private AppCompatActivity ownerActivity = null;
    private SubForumAdapter adapter = null;
    private Utils utils = new Utils();
    private String url = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ownerActivity = setIMainActivityCommand(activity);
    }

    private AppCompatActivity setIMainActivityCommand(Object activity) {
        if (activity instanceof AppCompatActivity){
            AppCompatActivity mainActivity = (AppCompatActivity)activity;
            return mainActivity;
        } else
            return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ownerActivity = setIMainActivityCommand(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forum_fragment, container, false);
        ButterKnife.bind(this, view);

        setRecyclerViewSetting();

        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true); // включаем

        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("NAME", "");
            url = bundle.getString("URL", "");

            commandToMainActivity = (IForumActivityCommand)ownerActivity;
            commandToMainActivity.onSetTitle(title);
        }
        loadSubForums();

        return view;
    }

    private void setRecyclerViewSetting() {
        mRecyclerView.setHasFixedSize(true);    // Не будем динамически изменять размеры
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider_red));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void loadSubForums() {
        if (htmlLoader != null) {
            try {
                htmlLoader.htmlAddressToParse(url);
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    @Override
    public void onRefresh() {
        loadSubForums();
    }

    @Override
    public void onCompletedLoading(Document html) {
        htmlLoader.parseSubForum(html);
    }

    @Override
    public void onReadyToShow(List<Object> items) {
        adapter = new SubForumAdapter(getActivity(), items);
        mRecyclerView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false); // выключаем
        adapter.setOnItemClickListener(new ForumClickListener() {
            @Override
            public void onItemClick(ForumItem item) {
                commandToMainActivity.replaceFragment(modules.getNameForumDetail(), item);
            }
        });
    }
}
