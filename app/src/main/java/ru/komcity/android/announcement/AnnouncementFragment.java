package ru.komcity.android.announcement;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.AsyncLoader.HtmlLoader;
import ru.komcity.android.base.AsyncLoader.IAsyncLoader;
import ru.komcity.android.base.AsyncLoader.IHtmlLoader;
import ru.komcity.android.base.ModulesGraph;
import ru.komcity.android.base.Utils;

public class AnnouncementFragment extends Fragment implements IAsyncLoader, IHtmlLoader, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.category_list) Spinner category_list;
    @BindView(R.id.announcement_type_list) Spinner announcement_type_list;
    @BindView(R.id.subcategory_list) Spinner subcategory_list;

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
                getActivity().getActionBar().setTitle(modules.getTitleAnnouncement());

        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true); // включаем

        category_list.setPrompt("Выберите категорию");

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
        List<String> list = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            list.add(items.get(i).toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_list.setAdapter(adapter);
        category_list.setSelection(-1);// выделяем элемент
        category_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                announcement_type_list.setAdapter(null);

                List<String> links = htmlLoader.getAnnouncementTypeByID(i);
                ArrayAdapter<String> adapterTypes = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, links);
                adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                announcement_type_list.setAdapter(adapterTypes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        swipeRefresh.setRefreshing(false); // включаем
    }

    @Override
    public void onRefresh() {
        loadAnnouncement();
    }
}
