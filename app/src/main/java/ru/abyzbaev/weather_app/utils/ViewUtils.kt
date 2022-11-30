package ru.abyzbaev.weather_app.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.util.stream.Collectors

class ViewUtils {
}

fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE;
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    length: Int = Snackbar.LENGTH_INDEFINITE,
    action: (View) -> Unit
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

