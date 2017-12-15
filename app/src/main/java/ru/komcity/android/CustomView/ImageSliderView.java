package ru.komcity.android.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class ImageSliderView extends RelativeLayout {
    @BindView(R.id.id_view_pager) public ViewPager imageSlider;
    private Context context;

    public ImageSliderView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        context = mContext;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.slider_view, this);
        ButterKnife.bind(this);
    }

    public void setItems(List<Object> mItems) {
        ImageSliderAdapter adapter = new ImageSliderAdapter(context, mItems);
        imageSlider.setAdapter(adapter);
    }
}
