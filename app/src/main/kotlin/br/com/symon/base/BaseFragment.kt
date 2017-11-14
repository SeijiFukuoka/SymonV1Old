package br.com.symon.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout

open class BaseFragment : Fragment(), BaseView {
    var fragmentId: String? = null

    private var progressContainer: RelativeLayout? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout = activity.findViewById<View>(android.R.id.content).rootView as ViewGroup
        val progressBar = ProgressBar(activity, null, android.R.attr.progressBarStyle)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)

        progressContainer = RelativeLayout(activity)
        progressContainer?.addView(progressBar)
        progressContainer?.gravity = Gravity.CENTER
        progressContainer?.visibility = View.GONE

        layout.addView(progressContainer, params)
    }

    override fun showLoading() {
        progressContainer?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressContainer?.visibility = View.GONE
    }

    override fun showError(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}