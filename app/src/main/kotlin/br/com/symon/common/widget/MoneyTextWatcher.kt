package br.com.symon.common.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import br.com.symon.common.parseToBigDecimal
import java.lang.ref.WeakReference
import java.text.NumberFormat
import java.util.*


class MoneyTextWatcher(editText: EditText) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(editable: Editable) {
        val editText = editTextWeakReference.get() ?: return
        editText.removeTextChangedListener(this)

        val parsed = editable.toString().parseToBigDecimal()
        val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(parsed)

        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }
}
