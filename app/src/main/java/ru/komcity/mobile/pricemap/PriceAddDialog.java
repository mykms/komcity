package ru.komcity.mobile.pricemap;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import ru.komcity.mobile.R;
import ru.komcity.mobile.base.GeoMapUtils;
import ru.komcity.mobile.base.ProductBase;
import ru.komcity.mobile.base.Utils;

public class PriceAddDialog extends Dialog {
    private HashMap<String, ArrayList<String>> productTypesListItems = null;
    private IProductDBListener productDBListener = null;
    private String currentUser = "Anonymous";
    private Utils utils = null;

    @BindView(R.id.btn_add)             public Button btn_add;
    @BindView(R.id.btn_geo)             public ImageButton btn_geo;
    @BindView(R.id.lbl_prod_name_text)  public EditText lbl_prod_name_text;
    @BindView(R.id.lbl_prod_price_value)public EditText lbl_prod_price_value;
    @BindView(R.id.lbl_market_name_text)public EditText lbl_market_name_text;
    @BindView(R.id.lst_prod_type)       public Spinner lst_prod_type;
    @BindView(R.id.lst_prod_subtype)    public Spinner lst_prod_subtype;

    @OnItemSelected(R.id.lst_prod_type)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (productTypesListItems != null) {
            try {
                ArrayList<String> arrayLst = productTypesListItems.get(parent.getSelectedItem());
                if (arrayLst == null)
                    arrayLst = new ArrayList<>();
                arrayLst.add(0, getContext().getResources().getString(R.string.spinner_select_default));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayLst);
                lst_prod_subtype.setAdapter(adapter);
            } catch (Exception ex) {
                utils.getException(ex);
            }
        }
    }

    @OnClick(R.id.btn_add)
    public void onAddProduct(View view) {
        String prodName = "";
        String prodType = "";
        String prodSybType = "";
        String prodPrice = "";
        double prodPriceValue = 0.0;
        String marketName = "";
        String marketAddress = "Россия, Комсомольск-на-Амуре, ул.Дикопольцева, 29/3";
        ArrayList<Object> marketGeo = new ArrayList<>();

        // наименование товара
        if (lbl_prod_name_text != null) {
            prodName = lbl_prod_name_text.getText().toString().trim();
            if (prodName.isEmpty()) {
                utils.showMessage(getContext().getResources().getString(R.string.msg_empty_prod_name), true);
                return;
            }
        }
        if (lst_prod_type.getSelectedItemPosition() > 0) {
            prodType = lst_prod_type.getSelectedItem().toString();
        }
        if (lst_prod_subtype.getSelectedItemPosition() > 0) {
            prodSybType = lst_prod_subtype.getSelectedItem().toString();
        }
        // Цена
        if (lbl_prod_price_value != null) {
            prodPrice = lbl_prod_price_value.getText().toString().trim();
            if (prodPrice.isEmpty()) {
                utils.showMessage(getContext().getResources().getString(R.string.msg_empty_prod_price), true);
                return;
            } else {
                try {
                    prodPriceValue = Double.parseDouble(prodPrice);
                    if (prodPriceValue <= 0.0) {
                        utils.showMessage(getContext().getResources().getString(R.string.msg_empty_prod_less_zero), true);
                        return;
                    }
                } catch (Exception ex) {
                    utils.showMessage(getContext().getResources().getString(R.string.msg_not_contains_number), true);
                    return;
                }
            }
        }
        // Название магазина
        if (lbl_market_name_text != null) {
            marketName = lbl_market_name_text.getText().toString().trim();
            if (marketName.isEmpty()) {
                utils.showMessage(getContext().getResources().getString(R.string.msg_empty_market_name), true);
                return;
            }
        }

        GeoMapUtils geoUtils = new GeoMapUtils(getContext());
        marketGeo.add(geoUtils.getCoordinatesByAddressLatLng(marketAddress));
        // Добавляем товар
        PriceListModel price = new PriceListModel(  marketGeo, marketAddress, marketName,
                                                    prodPriceValue, prodName, prodType,
                                                    prodSybType, currentUser);

        ProductBase productBase = new ProductBase(productDBListener);
        productBase.addPriceToDB(price);

        this.dismiss();
    }

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(View view) {
        this.dismiss();
    }

    public PriceAddDialog(@NonNull final Context context) {
        super(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_price, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // Без заголовка окна
        setContentView(dialogView);

        utils = new Utils(context);

        ButterKnife.bind(this);
    }

    public void setPriceSaveComleteListener(IProductDBListener productDBListener) {
        this.productDBListener = productDBListener;
    }

    public void setProductTypes(HashMap<String, ArrayList<String>> productTypesListItems) {
        if (productTypesListItems == null)
            productTypesListItems = new HashMap<>();
        this.productTypesListItems = productTypesListItems;

        loadProductTypesItemsToSpinner();
    }

    public void setUserInfo(String uInfo) {
        if (uInfo != null) {
            currentUser = uInfo;
        }
    }

    private void loadProductTypesItemsToSpinner() {
        if (this.productTypesListItems != null) {
            String[] items = new String[productTypesListItems.size()];
            productTypesListItems.keySet().toArray(items);

            ArrayList<String> lst = new ArrayList<>(Arrays.asList(items));
            lst.add(0, getContext().getResources().getString(R.string.spinner_select_default));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lst);
            lst_prod_type.setAdapter(adapter);
        }
    }
}
