package com.djavid.smartsubs.common.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
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

fun Window.setWhiteNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val dimDrawable = GradientDrawable()
        val navigationBarDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.WHITE)
        }
        val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

        val windowBackground = LayerDrawable(layers).apply {
            setLayerInsetTop(1, metrics.heightPixels)
        }

        setBackgroundDrawable(windowBackground)
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