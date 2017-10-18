package br.com.symon.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import br.com.symon.R
import com.santalu.autoviewpager.AutoViewPager
import android.support.v4.app.FragmentStatePagerAdapter
import br.com.symon.fragments.LoginPagerFragment
import android.R.attr.typeface
import android.graphics.Typeface
import android.content.res.AssetManager
import android.widget.Button
import java.util.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val viewPager = findViewById<AutoViewPager>(R.id.view_pager)
        viewPager.adapter = SamplePagerAdapter(supportFragmentManager)
        viewPager.setAutoScrollEnabled(false)

        val facebookButton = findViewById<Button>(R.id.facebook_button)
        val continueButton = findViewById<Button>(R.id.continue_button)
        facebookButton.setAllCaps(false)
        continueButton.setAllCaps(false)
        val typeface = Typeface.createFromAsset(assets,
                String.format(Locale.US, "fonts/%s", "Montserrat-Medium.ttf"))

        facebookButton.typeface = typeface
        continueButton.typeface = typeface
        facebookButton.setShadowLayer(0.01f, -2.toFloat(), 2.toFloat(),   getResources().getColor(R.color.blackAlpha));
        continueButton.setShadowLayer(0.01f, -2.toFloat(), 2.toFloat(),   getResources().getColor(R.color.blackAlpha));

    }

    internal class SamplePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return LoginPagerFragment.newInstance()
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
