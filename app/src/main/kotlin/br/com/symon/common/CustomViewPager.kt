package br.com.symon.common

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class CustomViewPager
@JvmOverloads
constructor(context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

  private var isPagingEnabled = true

  override fun onTouchEvent(event: MotionEvent): Boolean {
    return this.isPagingEnabled && super.onTouchEvent(event)
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    return this.isPagingEnabled && super.onInterceptTouchEvent(event)
  }

  fun setPagingEnabled(b: Boolean) {
    this.isPagingEnabled = b
  }
}