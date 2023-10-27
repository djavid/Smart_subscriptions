package com.djavid.smartsubs.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by mikepenz on 14.03.15.
 * This class implements a hack to change the layout padding on bottom if the keyboard is shown
 * to allow long lists with editTextViews
 * Basic idea for this solution found here: http://stackoverflow.com/a/9108219/325479
 */
class KeyboardUtil(activity: Activity, contentView: View, event: ((Boolean) -> Unit)? = null) {

    private val decorView: View = activity.window.decorView

    //a small helper to allow showing the editText focus
    private var onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val r = Rect()

        //r will be populated with the coordinates of your view that area still visible.
        decorView.getWindowVisibleDisplayFrame(r)

        //get screen height and calculate the difference with the usable area from the r
        val height = decorView.context.resources.displayMetrics.heightPixels
        val diff = height - r.bottom

        //if it could be a keyboard add the padding to the view
        if (diff != 0) { // if the usable screen height differs from the total screen height we assume that it shows a keyboard now
            event?.invoke(true)

            //check if the padding is 0 (if yes set the padding for the keyboard)
            if (contentView.paddingBottom != diff) { //set the padding of the contentView for the keyboard
                contentView.setPadding(0, 0, 0, diff)
            }
        } else {
            event?.invoke(false)

            //check if the padding is != 0 (if yes reset the padding)
            if (contentView.paddingBottom != 0) { //reset the padding of the contentView
                contentView.setPadding(0, 0, 0, 0)
            }
        }
    }

    init {
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun enable() {
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun disable() {
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

}