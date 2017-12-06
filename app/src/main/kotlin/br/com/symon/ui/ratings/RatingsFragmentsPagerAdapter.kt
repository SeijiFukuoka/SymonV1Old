package br.com.symon.ui.ratings

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter


class RatingsFragmentsPagerAdapter(
        fragmentManager: FragmentManager,
        private val fragmentList: MutableList<RatingsChildFragment>,
        private val titlesList: MutableList<String>)
    : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int = fragmentList.size
    override fun getItem(position: Int): Fragment = fragmentList[position]
    override fun getPageTitle(position: Int): CharSequence {
        return titlesList[position]
    }
}