package ru.komcity.mobile.pricemap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.komcity.mobile.R;
import ru.komcity.mobile.base.ProductBase;
import ru.komcity.mobile.base.Utils;

public class MapPriceListFragment extends Fragment {
    private HashMap<String, ArrayList<String>> productTypesListItems = new HashMap<>();
    private Utils utils = new Utils();
    private ProductBase productBase = new ProductBase();
    private IProductDBListener productDBListener = new IProductDBListener() {
        @Override
        public void onSaveProductResult(boolean result) {
            if (result) {
                (new Utils(getActivity().getApplicationContext())).showMessage("Сбой при добавлении товара", true);
            } else {
                (new Utils(getActivity().getApplicationContext())).showMessage("Добавлен 1 товар", false);
            }
        }

        @Override
        public void onProductTypesLoadComplete(HashMap<String, ArrayList<String>> items) {
            if (items != null) {
                productTypesListItems.putAll(items);
            }
        }

        @Override
        public void onProductLoadComplete(List<Object> items) {
            showPriceList(items);
        }
    };

    @BindView(R.id.prod_list) public RecyclerView productList;

    @OnClick(R.id.btnAddFloat)
    public void onAddProduct_Click(View view) {
        showAddDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.mapprice_list_fragment, container, false);
        ButterKnife.bind(this, view);

        productList.setLayoutManager(new LinearLayoutManager(getActivity()));
        productBase.setProductDBListener(productDBListener);
        productBase.getPriceList();
        productBase.getProductTypesList();

        return view;
    }

    /**
     * Отображает список на экране
     * @param items список для отображения
     */
    private void showPriceList(List<Object> items) {
        PriceListAdapter adapter = new PriceListAdapter(getActivity(), items);
        adapter.setOnItemClickListener(new IPriceListClickListener() {
            @Override
            public void onItemClick(PriceListModel item) {
                //
            }
        });
        productList.setAdapter(adapter);
    }

    private void showAddDialog() {
        PriceAddDialog addDialog = new PriceAddDialog(getActivity());
        addDialog.setProductTypes(productTypesListItems);
        addDialog.setUserInfo("user Ivanov I.D.");
        addDialog.setPriceSaveComleteListener(productDBListener);
        addDialog.show();
    }
}
