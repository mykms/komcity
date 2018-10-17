package ru.komcity.mobile.news;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jsoup.nodes.Document;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.mobile.FragmentBaseListener;
import ru.komcity.mobile.R;
import ru.komcity.mobile.base.AsyncLoader.HtmlLoader;
import ru.komcity.mobile.base.AsyncLoader.IAsyncLoader;
import ru.komcity.mobile.base.ExtraConst;
import ru.komcity.mobile.base.IMainActivityCommand;
import ru.komcity.mobile.base.ModulesGraph;
import ru.komcity.mobile.base.Utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class NewsFragment extends Fragment implements IAsyncLoader, INewsLoader,
        CalendarClickListener, FragmentBaseListener, SwipeRefreshLayout.OnRefreshListener {
    private long dateForStartSearch = 0;
    private int currentPage = 0;
    private IMainActivityCommand commandToMainActivity;
    private NewsAdapter adapter;
    private LinearLayoutManager layoutManager = null;
    private List<NewsArchiveLinkItem> newsArchiveLinks = new ArrayList<>();
    private Utils utils;
    private ModulesGraph modules = new ModulesGraph();
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);
    private Set<NewsArchiveLinkItem> normalLinks = new TreeSet<>(new Comparator<NewsArchiveLinkItem>() {
        @Override
        public int compare(NewsArchiveLinkItem o1, NewsArchiveLinkItem o2) {
            return o1.getPage() - o2.getPage();
        }
    });

    @BindView(R.id.recycler_view)   RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)   SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setIMainActivityCommand(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setIMainActivityCommand(context);
    }

    private void setIMainActivityCommand(Object activity) {
        if (activity instanceof IMainActivityCommand){
            this.commandToMainActivity = (IMainActivityCommand)activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            this.dateForStartSearch = args.getLong(ExtraConst.EXTRA_DATE_SEARCH_START, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        ButterKnife.bind(this, view);

        utils = new Utils();
        adapter = new NewsAdapter(getActivity(), new ArrayList<Object>());
        mRecyclerView.setHasFixedSize(true);    // Не будем динамически изменять размеры
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                                                            mRecyclerView.getContext(),
                                                            layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider_red));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true); // включаем

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initParams();
        loadNews(getLinkArchive());
        commandToMainActivity.onSetTitle(getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        commandToMainActivity.onSetTitle(getTitle());
    }

    private String getLinkArchive() {
        if (this.dateForStartSearch > 0) {
            Calendar curCalendar = Calendar.getInstance();
            curCalendar.setTimeInMillis(this.dateForStartSearch);
            String year = curCalendar.get(Calendar.YEAR) + "";
            int mm = curCalendar.get(Calendar.MONTH) + 1;
            String month = mm >= 10 ? mm + "" : "0" + mm;
            return "archive/" + year + "_" + month + "/";
        } else {
            return "";
        }
    }

    private String getTitle() {
        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(this.dateForStartSearch);
        String year = curCalendar.get(Calendar.YEAR) + "";
        String month = curCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        return this.dateForStartSearch > 0 ? modules.getTitleNews() + " за " + month + " " + year : modules.getTitleNews();
    }

    private void initParams() {
        if (this.commandToMainActivity != null) {
            commandToMainActivity.onSetTitle(modules.getTitleNews());
            commandToMainActivity.setCurrentFragment(this);
            commandToMainActivity.setVisibleMenuIcon(this.dateForStartSearch <= 0);
        }
    }

    private void loadNews(String path) {
        if (htmlLoader != null) {
            try {
                if (path == null) {
                    path = "";
                }
                htmlLoader.htmlAddressToParse("news/" + path);
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    @Override
    public void onCompletedLoading(Document html) {
        if (this.dateForStartSearch > 0) {
            if (this.newsArchiveLinks.isEmpty()) {
                htmlLoader.parseNews(html);
            }
            htmlLoader.parseNewsArchiveLinks(html, getLinkArchive());
        } else {
            htmlLoader.parseNews(html);
        }
    }

    @Override
    public void onReadyToShow(List<Object> items) {
        if (items == null) {
            items = new ArrayList<>();
        }
        swipeRefresh.setRefreshing(false); // выключаем

        if (adapter.getSize() >= items.size() && adapter.getSize() > 1) {
            mRecyclerView.scrollToPosition(adapter.getSize() - 1);
        }
        adapter.addItems(items);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NewsClickListener() {
            @Override
            public void onItemClick(NewsItem item) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("DATE", item.getDate());
                intent.putExtra("TITLE", item.getTitle());
                intent.putExtra("URL", item.getUrl());
                intent.putExtra("TEXT", item.getText());
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        if (this.dateForStartSearch > 0) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (layoutManager != null && !swipeRefresh.isRefreshing() && (currentPage + 1 < newsArchiveLinks.size()) ) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                            if (!newsArchiveLinks.isEmpty()) {
                                NewsArchiveLinkItem item = newsArchiveLinks.get(currentPage + 1);
                                dateForStartSearch = 0;
                                swipeRefresh.setRefreshing(true);
                                currentPage = item.getPage() - 1;
                                loadNews(item.getLink().replace("/news/", ""));
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onLinksLoaded(List<NewsArchiveLinkItem> links) {
        if (links == null) {
            links = new ArrayList<>();
        }
        int curPage = this.newsArchiveLinks.isEmpty() ? 1 : this.newsArchiveLinks.get(this.newsArchiveLinks.size() - 1).getPage();
        int nextPage = links.isEmpty() ? 1 : links.get(links.size() - 1).getPage();
        if (curPage < nextPage) {
            this.normalLinks.addAll(links);
            this.newsArchiveLinks.clear();
            this.newsArchiveLinks.add(new NewsArchiveLinkItem(getLinkArchive(), 1));
            this.newsArchiveLinks.addAll(this.normalLinks);
            loadNews(links.get(links.size() - 1).getLink());// Пройдем по последней ссылке в списке
        } else {
            // Отобразим новости архивные
            //Set<NewsArchiveLinkItem> uniqueLinks = new TreeSet<>(this.newsArchiveLinks);
        }
    }

    @Override
    public void onRefresh() {
        loadNews("");
    }

    @Override
    public void onDateSelected(long dateTime) {
        Calendar curCalendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(2002, 11, 1);
        if (this.commandToMainActivity != null) {
            if (dateTime > curCalendar.getTimeInMillis() || dateTime < startCalendar.getTimeInMillis()) {
                commandToMainActivity.onError("Вы не можете просмотреть новость за выбранную дату!");
            } else {
                Bundle args = new Bundle();
                args.putLong(ExtraConst.EXTRA_DATE_SEARCH_START, dateTime);
                commandToMainActivity.onLoadFragment(modules.getNameNews(), args);
            }
        }
    }

    @Override
    public void getToolbarTitle() {
        if (this.commandToMainActivity != null) {
            commandToMainActivity.onSetTitle(getTitle());
        }
    }
}
