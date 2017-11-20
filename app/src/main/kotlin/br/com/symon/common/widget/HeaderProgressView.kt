package br.com.symon.common.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import br.com.symon.R
import kotlinx.android.synthetic.main.view_header_progress.view.*

class HeaderProgressView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_header_progress, this, true)
        viewHeaderProgressFirstStepLayout.visibility = View.GONE
        viewHeaderProgressSecondStepLayout.visibility = View.GONE
        viewHeaderProgressThirdStepLayout.visibility = View.GONE
    }

    fun bind(step: Int?) {
        when (step) {
            1 -> viewHeaderProgressFirstStepLayout.visibility = View.VISIBLE
            2 -> viewHeaderProgressSecondStepLayout.visibility = View.VISIBLE
            3 -> viewHeaderProgressThirdStepLayout.visibility = View.VISIBLE
        }
    }
}