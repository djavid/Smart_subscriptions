package com.djavid.ui

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.djavid.core.ui.R

/**
 * EditText that hides keyboard on back button pressed.
 */

class ClosableEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideKeyBoard()
        }
        return true
    }

    fun hideKeyBoard() {
        val mgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(this.windowToken, 0)
        clearFocus()
    }

    fun showKeyBoard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun clearInput() {
        text?.clear()
    }

    fun clearInputAndFocus() {
        text?.clear()
        clearFocus()
        hideKeyBoard()
    }

    fun setError(enable: Boolean) {
        val drawable = if (enable) R.drawable.bg_edittext_error else R.drawable.bg_edittext
        background = context.getDrawable(drawable)
    }

}