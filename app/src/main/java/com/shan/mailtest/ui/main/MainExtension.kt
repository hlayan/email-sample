package com.shan.mailtest.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.shan.mailtest.R
import com.shan.mailtest.databinding.DialogDeleteItemBinding
import java.text.SimpleDateFormat
import java.util.*

fun showLog(message: String) {
    Log.d("myMessage", message)
}

fun getHtml(bodyHTML: String): String {
    val head = "<head><style>img{max-width: 100%; width:100%; height: auto;}</style></head>"
    return "<html>$head<body>$bodyHTML</body></html>"
}

fun String.isValidEmail() = !isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

@Suppress("unused")
fun String.getString() {
    val stringBuilder = StringBuilder(this)
    val startIdx: Int = stringBuilder.indexOf("<")
    val endIdx: Int = stringBuilder.indexOf(">")
    stringBuilder.replace(startIdx, endIdx, "")
}

const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm"

@SuppressLint("SimpleDateFormat")
fun Date.getDateFormat(pattern: String): String {
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(this)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showInfoDialog(message: String) {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        show()
    }
}

/**
 * Reference link
 *
 * https://android--examples.blogspot.com/2016/10/android-alertdialog-width-height.html
 *
 */
fun DialogDeleteItemBinding.showCustomDialog(
    resources: Resources,
    windowManager: WindowManager,
    onLogic: (dialog: AlertDialog, binding: DialogDeleteItemBinding) -> Unit = { _, _ -> }
) {
    val builder = AlertDialog.Builder(root.context)

    builder.setView(root)

    val dialog = builder.create()

    dialog.window?.setBackgroundDrawable(
        ContextCompat.getDrawable(
            root.context,
            R.drawable.rounded_corner_dialog
        )
    )

    onLogic(dialog, this)
    dialog.show()

    // Get screen width and height in pixels
    val displayMetrics = resources.displayMetrics

    @Suppress("DEPRECATION")
    windowManager.defaultDisplay.getRealMetrics(displayMetrics)

    // The absolute width of the available display size in pixels.
    val displayWidth = displayMetrics.widthPixels

    // Initialize a new window manager layout parameters
    val layoutParams = WindowManager.LayoutParams()

    // Copy the alert dialog window attributes to new layout parameter instance
    layoutParams.copyFrom(dialog.window!!.attributes)

    val dialogWindowWidth = (displayWidth * 0.86f).toInt()

    layoutParams.width = dialogWindowWidth

    // Apply the newly created layout parameters to the alert dialog window
    dialog.window!!.attributes = layoutParams
}