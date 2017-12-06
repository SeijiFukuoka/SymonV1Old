package br.com.symon.common.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

class WrapContentViewPager
constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var currentPagePosition = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasure = 0
        try {
            val child = getChildAt(currentPagePosition)
            if (child != null) {
                child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                val height = child.measuredHeight
                heightMeasure = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

    fun reMeasureCurrentPage(position: Int) {
        currentPagePosition = position
        requestLayout()
    }
}
