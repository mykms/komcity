package ru.komcity.uicomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.ui_loading_component_view.view.*

/**
 * Created by Aleksei Kholoimov on 15.03.2020
 * <p>
 * Процесс загрузки (блокирующий)
 */
class LoadingComponent(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var pIsVisible = false

    var isVisible: Boolean
        get() {
            return progressView?.isVisible ?: false
        }
        set(value) {
            if (value) {
                rootLayout?.visibility = View.VISIBLE
            }
            else {
                rootLayout?.visibility = View.GONE
            }
            progressView?.isVisible = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.ui_loading_component_view, this, true)
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