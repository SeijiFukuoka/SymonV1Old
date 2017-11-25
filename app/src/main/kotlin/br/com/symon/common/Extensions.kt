package br.com.symon.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun isDisplayedByTag(activity: AppCompatActivity, fragmentTag: String): Boolean {
    val fragment = activity.supportFragmentManager.findFragmentByTag(fragmentTag)
    return fragment != null && fragment.isVisible
}

fun replace(activity: FragmentActivity, @IdRes id: Int, fragment: Fragment) {
    activity.supportFragmentManager
            .beginTransaction()
            .replace(id, fragment, fragment.javaClass.canonicalName)
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

fun ImageView.loadUrlWithCircularImage(url: String?, @DrawableRes placeHolder: Int) = Glide.with(context)
        .load(url)
        .asBitmap()
        .centerCrop()
        .error(placeHolder)
        .into(object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
                circularBitmapDrawable.isCircular = true
                this@loadUrlWithCircularImage.setImageDrawable(circularBitmapDrawable)
            }
        })