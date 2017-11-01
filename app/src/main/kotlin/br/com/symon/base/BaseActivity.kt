package br.com.symon.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.RelativeLayout
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : AppCompatActivity(), BaseView {

    private var progressContainer: RelativeLayout? = null

    var errorMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val layout = findViewById<View>(android.R.id.content).rootView as ViewGroup
        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyle)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)

        progressContainer = RelativeLayout(this)
        progressContainer?.addView(progressBar)
        progressContainer?.gravity = Gravity.CENTER
        progressContainer?.visibility = View.GONE

        layout.addView(progressContainer, params)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base))
    }

    override fun showLoading() {
        progressContainer?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressContainer?.visibility = View.GONE
    }

    override fun showError(stringResId: Int) {
        errorMessage = getString(stringResId)
    }
}