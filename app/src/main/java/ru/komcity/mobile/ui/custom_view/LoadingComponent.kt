package ru.komcity.mobile.ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.view_loading_component.view.*
import ru.komcity.mobile.R

/**
 * Created by Aleksei Kholoimov on 15.03.2020
 * <p>
 * Процесс загрузки (блокирующий)
 */
class LoadingComponent(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading_component, this, true)
        initAttributes(attrs)
    }

    private fun initAttributes(attrs: AttributeSet?) {
    }

    var isVisible: Boolean = false
        get() {
            return progressView?.isVisible ?: false
        }
        set(value) {
            field = value
            if (value) rootLayout?.visibility = View.VISIBLE
            else rootLayout?.visibility = View.GONE
            progressView?.isVisible = value
        }
}