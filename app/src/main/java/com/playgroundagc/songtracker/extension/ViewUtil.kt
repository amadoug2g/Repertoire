package com.playgroundagc.songtracker.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by Amadou on 18/07/2021, 17:23
 *
 * View Extension Class
 *
 */

fun LinearLayout.visibility(visible: Boolean) {
    if (visible)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.GONE
}

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

fun FloatingActionButton.setInactive(enabled: Boolean) {
    this.isEnabled = enabled
}

fun View.setAllEnabled(enabled: Boolean) {
    if (this is ViewGroup) children.forEach { child -> child.setAllEnabled(enabled) }
    this.setOnTouchListener { _, _ -> enabled }
    this.isNestedScrollingEnabled = enabled
}

fun String.inputCheck(vararg text: String): Boolean {
    for (item in text)
        if (item.isNotEmpty()) continue else return false
    return true
}