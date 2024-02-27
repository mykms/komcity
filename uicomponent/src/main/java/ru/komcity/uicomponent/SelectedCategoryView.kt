package ru.komcity.uicomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import ru.komcity.uicomponent.databinding.UiSelectedCategoryViewBinding

/**
 * Created by Aleksei Kholoimov on 05.04.2020
 * <p>
 * UI-component for showing title nd close button
 */
class SelectedCategoryView: FrameLayout {
    private var _binding: UiSelectedCategoryViewBinding? = UiSelectedCategoryViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val binding get() = _binding!!
    private var pTitle = ""
    private var pIsVisibleCloseButton = false
    private var attrs: AttributeSet? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.ui_selected_category_view, this, true)
        initAttributes(attrs)
        initState()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SelectedCategoryView, 0, 0)
        try {
            pTitle = attributes.getString(R.styleable.SelectedCategoryView_title) ?: ""
            pIsVisibleCloseButton = attributes.getBoolean(R.styleable.SelectedCategoryView_isCloseButtonVisible, false)
        } finally {
            attributes.recycle()
        }
    }

    private fun initState() {
        isCloseButtonVisible = pIsVisibleCloseButton
        title = pTitle
    }

    var isCloseButtonVisible: Boolean
        get() {
            return binding.ivClose?.isVisible ?: false
        }
        set(value) {
            binding.ivClose?.isVisible = value
        }

    var title: String
        get() {
            return binding.tvTitle?.text?.toString() ?: ""
        }
        set(value) {
            binding.tvTitle?.text = value
        }

    fun setOnCloseListener(l: (View) -> Unit) {
        binding.ivClose?.setOnClickListener(l)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        binding.tvTitle.setOnClickListener(l)
    }
}