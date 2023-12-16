package com.djavid.smartsubs.common.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Interpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

fun View.show(show: Boolean) {
    this.visibility = if (show) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun <T : Number> Context.toPx(value: T) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics
).toInt()

fun <T : Number> Context.toDp(px: T) = px.toFloat() / resources.displayMetrics.density

fun View.animateAlpha(from: Float, to: Float, duration: Long, interpolator: Interpolator? = null) {
    val animation = AlphaAnimation(from, to)
    animation.duration = duration
    animation.fillAfter = true
    interpolator?.let { animation.interpolator = interpolator }

    startAnimation(animation)
}

fun Activity?.hideKeyboard() {
    if (this != null && this.currentFocus != null) {
        val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
    }
}

fun ImageView.setTintColor(colorRes: Int) {
    val color = ContextCompat.getColor(context, colorRes)
    imageTintList = ColorStateList.valueOf(color)
}

fun TextView.setColor(colorRes: Int) {
    val color = ContextCompat.getColor(context, colorRes)
    setTextColor(color)
}