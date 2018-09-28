package ru.komcity.mobile.CustomView.ImageSlider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.komcity.mobile.R;

public class ImageSliderAdapter extends PagerAdapter {
    private List<Object> imageItemsLinkList = new ArrayList<>();
    private LayoutInflater inflater;
    private CompleteLoadImageListener loadImageListener = null;
    private Context context = null;
    private ViewPager viewPager = null;
    @BindView(R.id.slider_item_image) public ImageView item_image;

    @OnClick(R.id.slider_item_image)
    public void showBigImage_OnClick() {
        Intent bigImage = new Intent(context, ImageViewFullscreenActivity.class);
        if (imageItemsLinkList != null) {
            int c = imageItemsLinkList.size();
            ArrayList<String> links = new ArrayList<>(c);
            for (int i = 0; i < c; i++) {
                try {
                    links.add(imageItemsLinkList.get(i).toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            bigImage.putStringArrayListExtra(ImageViewFullscreenActivity.EXTRA_LINKS_IMAGE, links);
            if (this.viewPager != null) {
                bigImage.putExtra(ImageViewFullscreenActivity.EXTRA_LINKS_POSITION, this.viewPager.getCurrentItem());
            }
            if (context != null) {
                context.startActivity(bigImage);
            }
        }
    }

    public ImageSliderAdapter(Context mContext, List<Object> mItems) {
        if (mItems != null)
            imageItemsLinkList = mItems;

        context = mContext;
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

        Picasso.with(container.getContext())
                .load(imageItemsLinkList.get(position).toString())
                .error(R.drawable.vector_ic_image_loading)
                .placeholder(R.drawable.vector_ic_image_loading)
                .into(getTarget(item_image));
        container.addView(view);

        return view;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setCompleteLoadImageListener(CompleteLoadImageListener listener) {
        if (listener != null) {
            loadImageListener = listener;
        }
    }

    private Target getTarget(final ImageView imgTarget){
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                imgTarget.setImageBitmap(bitmap);
                if (loadImageListener != null) {
                    loadImageListener.onCompleteLoadBMP(bitmap);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
