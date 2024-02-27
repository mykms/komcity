package ru.komcity.uicomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import ru.komcity.uicomponent.databinding.UiLoadingComponentViewBinding

/**
 * Created by Aleksei Kholoimov on 15.03.2020
 * <p>
 * Процесс загрузки (блокирующий)
 */
class LoadingComponent(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var _binding: UiLoadingComponentViewBinding? = UiLoadingComponentViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val binding get() = _binding!!
    private var pIsVisible = false

    var isVisible: Boolean
        get() {
            return binding.progressView?.isVisible ?: false
        }
        set(value) {
            if (value) {
                binding.rootLayout?.visibility = View.VISIBLE
            }
            else {
                binding.rootLayout?.visibility = View.GONE
            }
            binding.progressView?.isVisible = value
        }

    init {
        initAttributes(attrs)
        isVisible = pIsVisible
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.LoadingComponent, 0, 0)
        try {
            pIsVisible = attributes.getBoolean(R.styleable.LoadingComponent_isVisible, false)
        } finally {
            attributes.recycle()
        }
    }
}