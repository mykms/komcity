package ru.komcity.android.pricemap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class MapPriceFragment extends Fragment {
    @BindView(R.id.tabs) public TabLayout tabs;
    @BindView(R.id.map_price_viewpager) public ViewPager viewPager;

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
