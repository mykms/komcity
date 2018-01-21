package ru.komcity.android.pricemap;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class PriceViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title_price)    TextView lbl_title_price;
    @BindView(R.id.date_price)    TextView lbl_date_price;
    @BindView(R.id.geo_price)    TextView lbl_geo_price;

    public PriceViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
