package ru.komcity.uicomponent

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Aleksei Kholoimov on 05.05.2020
 * <p>
 * Декоративный элемент после каждого эдемента списка RecyclerView
 * [DividerWithRemoveDecorator.countTopRemove] - Количество элементов сверху для удаления
 * [DividerWithRemoveDecorator.countBottomRemove] - Количество элементов снизу для удаления
 * [DividerWithRemoveDecorator.decorResource] - Drawable Ресурс, для прорисовки на списке
 */
class DividerWithRemoveDecorator(context: Context,
                                 @DrawableRes
                                 private val decorResource: Int,
                                 private val countTopRemove: Int = 0,
                                 private val countBottomRemove: Int = 0) : RecyclerView.ItemDecoration() {

    private val divider: Drawable? = ContextCompat.getDrawable(context, decorResource)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        divider?.let {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            val start = if (countTopRemove < 0) {
                0
            } else {
                countTopRemove
            }
            for (i in start until childCount - countBottomRemove) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + it.intrinsicHeight
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }
        }
    }
}