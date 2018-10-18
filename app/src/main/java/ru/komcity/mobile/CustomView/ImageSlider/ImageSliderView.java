package ru.komcity.mobile.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.mobile.CustomView.ImageSlider.CompleteLoadImageListener;
import ru.komcity.mobile.CustomView.ImageSlider.ImageSliderAdapter;
import ru.komcity.mobile.R;
import ru.komcity.mobile.base.Utils;

public class ImageSliderView extends RelativeLayout {
    @BindView(R.id.id_view_pager) public ViewPager imageSlider;
    @BindView(R.id.group_bottom) public RelativeLayout radioPanelLayout;
    @BindView(R.id.group_radio) public RadioGroup radioGroup;
    private Context context;
    private CompleteLoadImageListener loadImageListener = null;
    private Utils utils = new Utils();

    public ImageSliderView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        context = mContext;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.slider_view, this);
        ButterKnife.bind(this);

        setVisibilityRadioPanel(false);
        radioGroup.removeAllViews();
        setSlidePageListener();
    }

    /**
     * Устанавливает картинки, которые будут отображаться
     * @param mItems список картинок, содержащий http-ссылки на картинки
     */
    public void setItems(List<Object> mItems) {
        if (mItems != null) {
            int itemsCount = mItems.size();
            // панель и кнопки покажем только если больше одного рисунка
            if (mItems.size() > 1) {
                setVisibilityRadioPanel(true);
                addRadioButtonsToPanel(itemsCount);
            } else {
                setVisibilityRadioPanel(false);
            }
            ImageSliderAdapter adapter = new ImageSliderAdapter(context, mItems);
            adapter.setViewPager(imageSlider);
            adapter.setCompleteLoadImageListener(new CompleteLoadImageListener() {
                @Override
                public void onCompleteLoadBMP(Bitmap loadedBmp) {
                    loadImageListener.onCompleteLoadBMP(loadedBmp);
                }
            });
            imageSlider.setAdapter(adapter);
        }
    }

    public void setCompleteLoadImageListener(CompleteLoadImageListener listener) {
        if (listener != null) {
            loadImageListener = listener;
        }
    }

    /**
     * генерирует и добавляет кнопки перелистывания к панели
     * @param mCount количество кнопок
     */
    private void addRadioButtonsToPanel(int mCount) {
        if(mCount < 0) {
            try {
                throw new NegativeArraySizeException("Количество элементов должно быть больше или равно нулю");
            } catch (NegativeArraySizeException ex) {
                utils.getException(ex);
            }
        } else {
            RadioButton[] radioButton = new RadioButton[mCount];
            for (int i = 0; i < mCount; i++) {
                try {
                    radioButton[i] = new RadioButton(context);
                    radioButton[i].setId(i);
                    if (i == 0) {
                        radioButton[i].setChecked(true);    // Установим, что первый элемент выбран
                        //radioButton[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.rb_bg_active));
                    } else {
                        //radioButton[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.rb_bg_in_active));
                    }
                    if (radioGroup != null) {
                        radioGroup.addView(radioButton[i]);
                    }
                } catch (Exception ex) {
                    utils.getException(ex);
                }
            }
            setRadioButtonClickListener();
        }
    }

    /**
     * По щелчку на кнопку-выбора загружает и отображает необходимый элемент
     */
    private void setRadioButtonClickListener() {
        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (imageSlider != null)
                        imageSlider.setCurrentItem(i);
                }
            });
        }
    }

    private void setSlidePageListener() {
        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // nothing
            }

            @Override
            public void onPageSelected(int position) {
                try {
                    ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
                } catch (Exception ex) {
                        utils.getException(ex);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // nothing
            }
        });
    }

    /**
     * Показывает или скрывает панель с кнопками перелистывания картинок
     * @param isVisible Если true - то покажем панель, иначе спрячем
     */
    public void setVisibilityRadioPanel(boolean isVisible) {
        if (isVisible) {
            radioPanelLayout.setVisibility(VISIBLE);
        } else {
            radioPanelLayout.setVisibility(GONE);
        }
    }

    /**
     * Задачет расположение панели с переключателями рисунков
     * @param gravity Тип расположения (Top-вверху, Bottom-внизу)
     */
    public void setRadioPanelGravity(Gravity gravity) {
        if (gravity != null) {
            LayoutParams params = (LayoutParams) radioPanelLayout.getLayoutParams();
            if (gravity == Gravity.Top) {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            } else if (gravity == Gravity.Bottom) {
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            }
            radioPanelLayout.setLayoutParams(params);
        }
    }

    /**
     * Расположение панели с кнопками пролистывания
     */
    public enum Gravity {
        Top,
        Bottom
    }
}
