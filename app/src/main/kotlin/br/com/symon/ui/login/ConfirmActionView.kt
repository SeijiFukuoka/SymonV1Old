package br.com.symon.ui.login

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import br.com.symon.R
import kotlinx.android.synthetic.main.view_confirm_action_view.view.textConfirmationAction

class ConfirmActionView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  init {
    LayoutInflater.from(context).inflate(R.layout.view_confirm_action_view, this, true)
  }

  fun bind(message: String) {
    textConfirmationAction?.text = message
  }
}