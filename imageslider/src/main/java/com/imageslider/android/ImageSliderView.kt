package com.imageslider.android

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.layout_silder.view.*

class ImageSliderView : ConstraintLayout {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_silder, this)

        setVisibilityPointSwitcherPanel(false)
        groupPointSwitcher.removeAllViews()
        setSlidePageListener()
    }

    /**
     * Показывает или скрывает панель с кнопками перелистывания картинок
     * @param isVisible Если true - то покажем панель, иначе спрячем
     */
    private fun setVisibilityPointSwitcherPanel(isVisible: Boolean) {
        if (isVisible) {
            groupPointSwitcher.visibility = View.VISIBLE
        } else {
            groupPointSwitcher.visibility = View.GONE
        }
    }


    private fun setSlidePageListener() {
        imageViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // nothing
            }

            override fun onPageSelected(position: Int) {
                try {
                    (groupPointSwitcher.getChildAt(position) as RadioButton).isChecked = true
                } catch (ex: Exception) {
                    //utils.getException(ex)
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
                // nothing
            }
        })
    }

    /**
     * Устанавливает картинки, которые будут отображаться
     * @param mItems список картинок, содержащий http-ссылки на картинки
     */
    fun setItems(mItems: List<String>) {
        val itemsCount = mItems.size
        // панель и кнопки покажем только если больше одного рисунка
        if (mItems.size > 1) {
            setVisibilityPointSwitcherPanel(true)
            addRadioButtonsToPanel(itemsCount)
        } else {
            setVisibilityPointSwitcherPanel(false)
        }
        val adapter = ImageSliderAdapter(mItems, null)
//        adapter.setViewPager(imageSlider)
//        adapter.setCompleteLoadImageListener(object : CompleteLoadImageListener() {
//            fun onCompleteLoadBMP(loadedBmp: Bitmap) {
//                loadImageListener.onCompleteLoadBMP(loadedBmp)
//            }
//        })
        imageViewPager.adapter = adapter
    }

    /**
     * генерирует и добавляет кнопки перелистывания к панели
     * @param mCount количество кнопок
     */
    private fun addRadioButtonsToPanel(mCount: Int) {
        if (mCount < 0) {
            try {
                throw NegativeArraySizeException("Количество элементов должно быть больше или равно нулю")
            } catch (ex: NegativeArraySizeException) {
                //utils.getException(ex)
            }
        } else {
            val radioButton = emptyArray<RadioButton>()
            for (i in 0 until mCount) {
                try {
                    radioButton[i] = RadioButton(context)
                    radioButton[i].id = i
                    if (i == 0) radioButton[i].isChecked = true    // Установим, что первый элемент выбран
                    groupPointSwitcher?.addView(radioButton[i])
                } catch (ex: Exception) {
                    //utils.getException(ex)
                }

            }
            setRadioButtonClickListener()
        }
    }

    /**
     * По щелчку на кнопку-выбора загружает и отображает необходимый элемент
     */
    private fun setRadioButtonClickListener() {
        groupPointSwitcher?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            imageViewPager?.currentItem = i
        })
    }

//
//    /**
//     * Задачет расположение панели с переключателями рисунков
//     * @param gravity Тип расположения (Top-вверху, Bottom-внизу)
//     */
//    fun setRadioPanelGravity(gravity: Gravity?) {
//        if (gravity != null) {
//            val params = radioPanelLayout.getLayoutParams() as ConstraintLayout.LayoutParams
//            if (gravity == Gravity.Top) {
//                params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
//                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
//            } else if (gravity == Gravity.Bottom) {
//                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
//            }
//            radioPanelLayout.setLayoutParams(params)
//        }
//    }

    /**
     * Расположение панели с кнопками пролистывания
     */
    enum class Gravity {
        Top,
        Bottom
    }
}