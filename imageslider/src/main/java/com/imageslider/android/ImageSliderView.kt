package com.imageslider.android

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.layout_silder.view.*

/**
 * Created by Aleksei Kholoimov on 19.04.2020
 * <p>
 * View-компонент для пролистывания рисунков
 */
class ImageSliderView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var isVisiblePanel = true
    private var onImageClickListener: ((imageUrl: String) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_silder, this)
        initAttributes(attrs)
        initComponents()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ImageSliderView, 0, 0)
        try {
            isVisiblePanel = attributes.getBoolean(R.styleable.ImageSliderView_isVisiblePanel, true)
        } finally {
            attributes.recycle()
        }
    }

    private fun initComponents() {
        initImageSlider()
        initDotsSlider()
    }

    private fun initImageSlider() = with(imageViewPager) {
        orientation = ViewPager2.ORIENTATION_HORIZONTAL
        offscreenPageLimit = 4
        setPageTransformer { page, position ->
            val myOffset = position * -(2 * 40.toPx + 40.toPx)
            if (this.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            } else {
                page.translationY = myOffset
            }
        }
        adapter = null
    }

    private fun initDotsSlider() = with(dotList) {
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter = null
    }

    fun setOnImageClickListener(onImageClickListener: (imageUrl: String) -> Unit) {
        this.onImageClickListener = onImageClickListener
    }

    /**
     * Устанавливает картинки, которые будут отображаться
     * @param items список картинок, содержащий http-ссылки на картинки
     */
    fun setItems(items: List<String>) {
        setImageAdapter(items)
        setDotsAdapter(items)
    }

    private fun setImageAdapter(items: List<String>) {
        imageViewPager.adapter = ImageSliderAdapter(items) { imageUrl ->
            onImageClickListener?.let { it(imageUrl) }
        }
    }

    private fun setDotsAdapter(items: List<String>) {
        dotList.adapter = DotsSliderAdapter(items) {
            //
        }
    }
}