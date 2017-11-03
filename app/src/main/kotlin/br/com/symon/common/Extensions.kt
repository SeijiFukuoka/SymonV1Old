package br.com.symon.common

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun Activity.toast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun <T> Activity.startIntent(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

fun String.replaceAll(regex: String, replacement: String): String? =
        Pattern.compile(regex).matcher(this).replaceAll(replacement).trim()

fun Date.format(format: String): String = asString(SimpleDateFormat(format, Locale("pt", "BR")))

private fun Date.asString(format: DateFormat): String = format.format(this)