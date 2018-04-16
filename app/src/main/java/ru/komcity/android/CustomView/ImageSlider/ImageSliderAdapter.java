package ru.komcity.android.CustomView.ImageSlider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class ImageSliderAdapter extends PagerAdapter {
    private List<Object> imageItemsLinkList = new ArrayList<>();
    private LayoutInflater inflater;
    @BindView(R.id.slider_item_image) public ImageView item_image;

    public ImageSliderAdapter(Context mContext, List<Object> mItems) {
        if (mItems != null)
            imageItemsLinkList = mItems;

        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageItemsLinkList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.slider_view_item, container, false);
        ButterKnife.bind(this, view);

        Picasso.with(container.getContext()).load(imageItemsLinkList.get(position).toString())
                .error(R.drawable.vector_ic_image_loading)
                .placeholder(R.drawable.vector_ic_image_loading)
                .into(item_image);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
