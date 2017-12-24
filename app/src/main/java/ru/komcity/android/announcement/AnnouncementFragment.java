package ru.komcity.android.announcement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.jsoup.nodes.Document;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.AsyncLoader.HtmlLoader;
import ru.komcity.android.base.AsyncLoader.IAsyncLoader;
import ru.komcity.android.base.IMainActivityCommand;
import ru.komcity.android.base.ModulesGraph;
import ru.komcity.android.base.Utils;

public class AnnouncementFragment extends Fragment implements IAsyncLoader, IAnnoncementHtmlLoader, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.category_list) Spinner category_list;
    @BindView(R.id.announcement_type_list) Spinner announcement_type_list;
    @BindView(R.id.subcategory_list) Spinner subcategory_list;

    private IMainActivityCommand commandToMainActivity;
    private Utils utils;
    private ModulesGraph modules = new ModulesGraph();
    private HtmlLoader htmlLoader = new HtmlLoader(this, this);
    private HtmlLoader htmlLoaderSubCategory;
    private IAsyncLoader asyncSubCategory = new IAsyncLoader() {
        @Override
        public void onCompletedLoading(Document html) {
            htmlLoaderSubCategory.parseAnnouncementSubCategory(html);
        }
    };

    public AnnouncementFragment() {
        htmlLoaderSubCategory = new HtmlLoader(this, asyncSubCategory);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setIMainActivityCommand(activity);
    }

    private void setIMainActivityCommand(Object activity) {
        if (activity instanceof AppCompatActivity){
            AppCompatActivity mainActivity = (AppCompatActivity)activity;
            commandToMainActivity = (IMainActivityCommand)mainActivity;
            commandToMainActivity.onSetTitle(modules.getTitleAnnouncement());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setIMainActivityCommand(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.announcement_fragment, container, false);
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
        //
    }

    @Override
    public void onRefresh() {
        loadAnnouncement();
    }

    @Override
    public void onReadyToShowCategoryAndTypes(final List<Object> items) {
        AnnouncementCategoryAdapter adapter = new AnnouncementCategoryAdapter(getActivity().getApplicationContext(), R.layout.announcement_item_spinner, items);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_list.setAdapter(adapter);
        category_list.setSelection(-1);// выделяем элемент
        category_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setAnnouncementTypesToSpinner((AnnouncementCategoryModel)items.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });
    }

    private void setAnnouncementTypesToSpinner(final AnnouncementCategoryModel item) {
        AnnouncementTypesAdapter adapter = new AnnouncementTypesAdapter(getActivity().getApplicationContext(), R.layout.announcement_item_spinner, item.getSubCategoryList());
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        announcement_type_list.setAdapter(adapter);
        announcement_type_list.setSelection(-1);// выделяем элемент
        announcement_type_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AnnouncementTypeModel itemModel = (AnnouncementTypeModel)item.getSubCategoryList().get(i);
                if (htmlLoader != null) {
                    try {
                        htmlLoaderSubCategory.htmlAddressToParse("board/main/categoryclicklistener/?id1=" + itemModel.getRef1() + "&id2=" + itemModel.getRef2() + "&h=3");
                    } catch (Exception ex) {
                        utils.getException(ex);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });
    }

    @Override
    public void onReadyToShowSubCategory(List<Object> items) {
        AnnouncementSubCategoryAdapter adapter = new AnnouncementSubCategoryAdapter(getActivity().getApplicationContext(), R.layout.announcement_item_spinner, items);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcategory_list.setAdapter(adapter);
        subcategory_list.setSelection(-1);// выделяем элемент
        subcategory_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Отображаем все объявления
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        swipeRefresh.setRefreshing(false); // включаем только здесь
    }
}
