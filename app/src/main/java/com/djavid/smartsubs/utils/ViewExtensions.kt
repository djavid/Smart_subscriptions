package com.djavid.smartsubs.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Interpolator

fun View.show(show: Boolean) {
    this.visibility = if (show) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun <T : Number> Context.toPx(value: T) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    value.toFloat(),
    resources.displayMetrics
).toInt()

fun <T : Number> Context.toDp(px: T) = px.toFloat() / resources.displayMetrics.density

fun View.animateAlpha(from: Float, to: Float, duration: Long, interpolator: Interpolator? = null) {
    val animation = AlphaAnimation(from, to)
    animation.duration = duration
    animation.fillAfter = true
    interpolator?.let { animation.interpolator = interpolator }

    startAnimation(animation)
}