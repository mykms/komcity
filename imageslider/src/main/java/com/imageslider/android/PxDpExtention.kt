package com.imageslider.android

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 * Created by Aleksei Kholoimov on 19.04.2020
 * <p>
 * Расширение для конвертации единиц измерения под Android
 */
val Int.toDp: Int
    get() {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).roundToInt()
    }

val Int.toPx: Int
    get() {
        val metrics = Resources.getSystem().displayMetrics
        return (this / (metrics.densityDpi / 160f)).roundToInt()
    }