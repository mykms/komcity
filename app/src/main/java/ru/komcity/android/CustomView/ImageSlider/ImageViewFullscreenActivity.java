package ru.komcity.android.CustomView.ImageSlider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;

public class ImageViewFullscreenActivity extends AppCompatActivity {
    public static final String EXTRA_LINKS_IMAGE = "ARRAY_IMAGE_LINKS";
    public static final String EXTRA_LINKS_POSITION = "ARRAY_IMAGE_POSITION";
    private String[] imageList = null;
    private int position = 0;
    private static final String stingForReplace = "thumb";

    @BindView(R.id.imgFullScreen) ImageView imgFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_fullscreen_activity);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                List<String> imgArrList = getIntent().getExtras().getStringArrayList(EXTRA_LINKS_IMAGE);
                position = getIntent().getExtras().getInt(EXTRA_LINKS_POSITION);
                if (imgArrList != null) {
                    imageList = new String[imgArrList.size()];
                    // Получаем пути к большим рисункам
                    for (int i = 0; i < imgArrList.size(); i++) {
                        imageList[i] = imgArrList.get(i)
                                .replace(stingForReplace + "s/", "")
                                .replace(stingForReplace + "_", "");
                    }

                    if (imageList != null) {
                        Picasso.with(getApplicationContext())
                                .load(imageList[position])
                                .error(R.drawable.vector_ic_image_loading)
                                .placeholder(R.drawable.vector_ic_image_loading)
                                .into(imgFullScreen);
                    }
                }
            }
        }
    }
}
