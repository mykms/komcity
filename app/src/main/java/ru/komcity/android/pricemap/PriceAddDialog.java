package ru.komcity.android.pricemap;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.komcity.android.R;

public class PriceAddDialog extends Dialog {
    @BindView(R.id.btn_add) public Button btn_add;
    @BindView(R.id.btn_geo) public Button btn_geo;
    @BindView(R.id.lbl_title) public TextView lbl_title;
    @BindView(R.id.lbl_prod_name_text) public EditText lbl_prod_name_text;
    @BindView(R.id.lbl_prod_price_value) public EditText lbl_prod_price_value;
    @BindView(R.id.lbl_market_name_text) public EditText lbl_market_name_text;
    @BindView(R.id.lst_prod_type) public Spinner lst_prod_type;
    @BindView(R.id.lst_prod_subtype) public Spinner lst_prod_subtype;

    public PriceAddDialog(@NonNull Context context) {
        super(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_price, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // Без заголовка окна
        setContentView(dialogView);

        ButterKnife.bind(this);
    }

    /**
     * Устанавливает обработчик кнопки Добавления
     * @param clickListener
     */
    public void setAcceptClickListener(View.OnClickListener clickListener) {
        if (clickListener != null)
            btn_add.setOnClickListener(clickListener);
    }

    /**
     * Устанавливает (переопределяет) заголовок в диалоге
     * @param title
     */
    @Override
    public void setTitle(@Nullable CharSequence title) {
        if (title == null)
            if (!title.toString().isEmpty()) {
                lbl_title.setText(title);
            }
    }

    @OnClick(R.id.btn_close_dialog)
    public void onCloseDialog(View view) {
        this.dismiss();
    }
}
