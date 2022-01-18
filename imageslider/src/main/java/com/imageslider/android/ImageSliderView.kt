package com.imageslider.android

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.imageslider.android.databinding.LayoutSliderBinding

/**
 * Created by Aleksei Kholoimov on 19.04.2020
 * <p>
 * View-компонент для пролистывания рисунков
 */
class ImageSliderView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var selectedPosition = 0
    private var isVisiblePanel = true
    private var onImageListener: ImageSliderCallback? = null
    @DrawableRes
    private var activeItem = R.drawable.bg_point_switcher
    @DrawableRes
    private var inActiveItem = R.drawable.bg_point_switcher
    private var activeItemSize = 12
    private var inActiveItemSize = 8
    private val binding = LayoutSliderBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        initAttributes(attrs)
        initComponents()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ImageSliderView, 0, 0)
        try {
            isVisiblePanel = attributes.getBoolean(R.styleable.ImageSliderView_isVisiblePanel, true)
            activeItem = attributes.getResourceId(R.styleable.ImageSliderView_activeItem, R.drawable.bg_point_switcher)
            inActiveItem = attributes.getResourceId(R.styleable.ImageSliderView_inActiveItem, R.drawable.bg_point_switcher)
            activeItemSize = attributes.getInteger(R.styleable.ImageSliderView_activeItemSize, 12)
            inActiveItemSize = attributes.getResourceId(R.styleable.ImageSliderView_inActiveItemSize, 8)
        } finally {
            attributes.recycle()
        }
    }

    private fun initComponents() {
        initImageSlider()
        initDotsSlider()
    }

    private fun initImageSlider() = with(binding.imageViewPager) {
        orientation = ViewPager2.ORIENTATION_HORIZONTAL
        offscreenPageLimit = 4
        adapter = null
    }

    private fun initDotsSlider() = with(binding.dotList) {
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter = null
    }

    fun setOnImageClickListener(onImageListener: ImageSliderCallback) {
        this.onImageListener = onImageListener
    }

    /**
     * Получает картинку по позиции
     * @param position - позиция для получени рисунка
     * @return - Возвращает рисунок в формате Bitmap (без гарантий)
     */
    fun getImageByPosition(position: Int): Bitmap? {
        return getBitmapFromAdapterByPosition(position)
    }

    private fun getBitmapFromAdapterByPosition(position: Int): Bitmap? {
        return (binding.imageViewPager.adapter as? ImageSliderAdapter)?.getBitmapByPosition(position)
    }

    /**
     * Устанавливает картинки, которые будут отображаться
     * @param items список картинок, содержащий http-ссылки на картинки
     */
    fun setItems(items: List<String>) {
        setImageAdapter(items)
        setDotsAdapter(items.map { false })
    }

    private fun setImageAdapter(items: List<String>) {
        binding.imageViewPager.adapter = ImageSliderAdapter(items, onImageListener)
    }

    private fun setDotsAdapter(items: List<Boolean>) {
        binding.dotList.adapter = DotsSliderAdapter(arrayListOf<Boolean>().apply { addAll(items) },
                activeItem, inActiveItem, activeItemSize, inActiveItemSize) {
            binding.imageViewPager.currentItem = it
        }
        binding.imageViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                (binding.dotList.adapter as? DotsSliderAdapter)?.apply {
                    changeItemAndUpdate(selectedPosition, false)
                    changeItemAndUpdate(position, true)
                }
                onImageListener?.onImageSwipe(getBitmapFromAdapterByPosition(position), position)
                selectedPosition = position
            }
        })
    }
}