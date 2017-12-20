package br.com.symon.common.widget

import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.StaticLayout
import android.text.TextUtils.TruncateAt
import android.util.AttributeSet
import android.widget.TextView
import java.util.*

class EllipsizingTextView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private val ELLIPSIS = "..."

    interface EllipsizeListener {
        fun ellipsizeStateChanged(ellipsized: Boolean)
    }

    private val ellipsizeListeners = ArrayList<EllipsizeListener>()
    private var isEllipsized: Boolean = false
    private var isStale: Boolean = false
    private var programmaticChange: Boolean = false
    private var fullText: String? = null
    private var maxLines = -1
    private var lineSpacingMultiplier2 = 1.0f
    private var lineAdditionalVerticalPadding = 0.0f


    fun addEllipsizeListener(listener: EllipsizeListener?) {
        if (listener == null) {
            throw NullPointerException()
        }
        ellipsizeListeners.add(listener)
    }

    fun removeEllipsizeListener(listener: EllipsizeListener) {
        ellipsizeListeners.remove(listener)
    }

    fun isEllipsized(): Boolean {
        return isEllipsized
    }

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        this.maxLines = maxLines
        isStale = true
    }

    override fun getMaxLines(): Int {
        return maxLines
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        this.lineAdditionalVerticalPadding = add
        this.lineSpacingMultiplier2 = mult
        super.setLineSpacing(add, mult)
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        if (!programmaticChange) {
            fullText = text.toString()
            isStale = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isStale) {
            super.setEllipsize(null)
            resetText()
        }
        super.onDraw(canvas)
    }

    private fun resetText() {
        val maxLines = getMaxLines()
        var workingText = fullText
        var ellipsized = false
        if (maxLines != -1) {
            val layout = createWorkingLayout(workingText)
            if (layout.lineCount > maxLines) {
                workingText = fullText!!.substring(0, layout.getLineEnd(maxLines - 1)).trim { it <= ' ' }
                while (createWorkingLayout(workingText!! + ELLIPSIS).lineCount > maxLines) {
                    val lastSpace = workingText.lastIndexOf(' ')
                    if (lastSpace == -1) {
                        break
                    }
                    workingText = workingText.substring(0, lastSpace)
                }
                workingText = workingText + ELLIPSIS
                ellipsized = true
            }
        }
        if (workingText != text) {
            programmaticChange = true
            try {
                text = workingText
            } finally {
                programmaticChange = false
            }
        }
        isStale = false
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized
            for (listener in ellipsizeListeners) {
                listener.ellipsizeStateChanged(ellipsized)
            }
        }
    }

    private fun createWorkingLayout(workingText: String?): Layout {
        return StaticLayout(workingText, paint, width - paddingLeft - paddingRight,
                Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier2, lineAdditionalVerticalPadding, false)
    }

    override fun setEllipsize(where: TruncateAt) {
        // Ellipsize settings are not respected
    }

}