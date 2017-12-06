package br.com.symon.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import br.com.symon.R
import kotlinx.android.synthetic.main.view_confirm_action_view.view.*

class ConfirmActionView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_confirm_action_view, this, true)
    }

    fun bind(message: String) {
        textConfirmationAction?.text = message
    }
}