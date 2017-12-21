package br.com.symon.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import br.com.symon.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.math.BigDecimal
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun AppCompatActivity.isDisplayedByTag(fragmentTag: String): Boolean {
    val fragment = this.supportFragmentManager.findFragmentByTag(fragmentTag)
    return fragment != null && fragment.isVisible
}

fun FragmentActivity.replace(@IdRes id: Int, fragment: Fragment?) {
    this.supportFragmentManager
            .beginTransaction()
            .replace(id, fragment, fragment?.javaClass?.canonicalName)
            .commit()
}

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

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Date.dateFormat(): String = format("dd/MM/yyyy")

private fun Date.format(format: String): String = asString(SimpleDateFormat(format, Locale("pt", "BR")))

private fun Date.asString(format: DateFormat): String = format.format(this)

fun dpToPixels(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics).toInt()

fun ImageView.loadUrl(url: String?) {
    Glide.with(context).load(url).into(this)
}

fun ImageView.loadUrl(uri: Uri?) {
    Glide.with(context).load(uri).into(this)
}

fun ImageView.loadUrlToBeRounded(url: String?) {
    Glide.with(context).load(url)
            .apply(RequestOptions.circleCropTransform()).into(this)
}

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.parseToBigDecimal(): BigDecimal {
    val replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(Locale("pt", "BR")).currency.symbol)
    val cleanString = this.replace(replaceable.toRegex(), "")
    return if (cleanString.isEmpty()) {
        BigDecimal(0.00)
    } else {
        BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)
    }
}

fun SwipeRefreshLayout.setPrimaryColors() {
    this.setColorSchemeColors(
            ResourcesCompat.getColor(resources, R.color.colorPrimary, null),
            ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null))
}