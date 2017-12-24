package ru.komcity.android.pricemap;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.IMainActivityCommand;
import ru.komcity.android.base.ModulesGraph;

public class MapPriceFragment extends Fragment {
    @BindView(R.id.tabs) public TabLayout tabs;
    @BindView(R.id.map_price_viewpager) public ViewPager viewPager;

    private IMainActivityCommand commandToMainActivity;
    private ModulesGraph modules = new ModulesGraph();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setIMainActivityCommand(activity);
    }

    private void setIMainActivityCommand(Object activity) {
        if (activity instanceof AppCompatActivity){
            AppCompatActivity mainActivity = (AppCompatActivity)activity;
            commandToMainActivity = (IMainActivityCommand)mainActivity;
            commandToMainActivity.onSetTitle(modules.getTitleMapPriceMain());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setIMainActivityCommand(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapprice_fragment, container, false);
        ButterKnife.bind(this, view);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        MapPriceViewPageAdapter adapter = new MapPriceViewPageAdapter(getActivity().getFragmentManager());
        adapter.addFragment(new MapPriceMapFragment(), "КАРТА");
        adapter.addFragment(new MapPriceListFragment(), "СПИСОК");
        adapter.addFragment(new MapPriceFavoriteFragment(), "ИЗБРАННОЕ");
        viewPager.setAdapter(adapter);
    }
}
