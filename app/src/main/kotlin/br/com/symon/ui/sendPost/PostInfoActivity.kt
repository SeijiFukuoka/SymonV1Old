package br.com.symon.ui.sendPost

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.loadUrl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_post_info.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*


class PostInfoActivity: BaseActivity() {
    companion object {
        const val EXTRA_URI = "EXTRA_URI"

        lateinit var uri: Uri

        fun newIntent(context: Context, uri: Uri?): Intent {
            val intent = Intent(context, PostInfoActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_info)


        uri = intent.getParcelableExtra(EXTRA_URI)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        customToolbarTitleTextView.text = getString(R.string.post_into_title)

        postInfoImageView.loadUrl(uri)
    }

}