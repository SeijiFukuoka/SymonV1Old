package br.com.symon.ui

import android.os.Bundle
import br.com.symon.R
import br.com.symon.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}