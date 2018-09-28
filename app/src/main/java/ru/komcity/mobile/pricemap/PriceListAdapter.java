package ru.komcity.mobile.pricemap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.mobile.R;

public class PriceListAdapter extends RecyclerView.Adapter<PriceListAdapter.PriceListViewHolder> {
    private List<Object> feedItemList;
    private Context mContext;
    private IPriceListClickListener onItemClickListener;

    public PriceListAdapter(Context context, List<Object> itemList) {
        this.feedItemList = itemList;
        this.mContext = context;
    }

    @Override
    public PriceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_list_item, null);
        PriceListViewHolder viewHolder = new PriceListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PriceListViewHolder holder, int position) {
        final PriceListModel item = (PriceListModel)feedItemList.get(position);

        holder.prodName.setText(item.getProdName());         // Имя продукта
        holder.marketName.setText(item.getMarketName());     // Название магазина
        holder.prodPrice.setText(item.getPrice() + " Р");
        holder.prodDate.setText(item.getDate().toString());  // Дата добавления продукта

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(item);
            }
        };
        // Открываем карту и показываем все магазины где есть этот продукт
        holder.prodImg.setOnClickListener(listener);
        holder.prodName.setOnClickListener(listener);

        // Показываем карту и местонахождение
        holder.marketName.setOnClickListener(listener);

        // Показываем пользователя, который добавил
        holder.prodPrice.setOnClickListener(listener);
        holder.prodDate.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public static class PriceListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.prod_logo) ImageView prodImg;
        @BindView(R.id.prod_name) TextView prodName;
        @BindView(R.id.market_name) TextView marketName;
        @BindView(R.id.prod_price) TextView prodPrice;
        @BindView(R.id.prod_price_date) TextView prodDate;

        public PriceListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public IPriceListClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(IPriceListClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}