package br.com.symon.ui.ratings

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import kotlinx.android.synthetic.main.fragment_ratings.*
import java.util.*


class RatingsFragment : BaseFragment(), RatingsContract {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentId = RatingsFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_ratings)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val salesRatingFragment = RatingsChildFragment.newInstance(0)
        val likesRatingFragment = RatingsChildFragment.newInstance(1)
        val dislikesRatingFragment = RatingsChildFragment.newInstance(2)
        val commentsRatingFragment = RatingsChildFragment.newInstance(3)

        val fragmentsList: MutableList<RatingsChildFragment> = ArrayList()
        fragmentsList.add(salesRatingFragment)
        fragmentsList.add(likesRatingFragment)
        fragmentsList.add(dislikesRatingFragment)
        fragmentsList.add(commentsRatingFragment)

        val titlesList: MutableList<String> = ArrayList()
        titlesList.add("1")
        titlesList.add("1")
        titlesList.add("7")
        titlesList.add("3")

        fragmentRatingsCustomViewPager.adapter = RatingsFragmentsPagerAdapter(childFragmentManager, fragmentsList, titlesList)
        fragmentRatingsTabLayout.setupWithViewPager(fragmentRatingsCustomViewPager)

        val tabOne = LayoutInflater.from(activity).inflate(R.layout.custom_rating_tab, null) as TextView
        tabOne.text = titlesList[0]
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ratings_sale_active, 0, 0, 0)
        fragmentRatingsTabLayout.getTabAt(0)?.customView = tabOne

        val tabTwo = LayoutInflater.from(activity).inflate(R.layout.custom_rating_tab, null) as TextView
        tabTwo.text = titlesList[1]
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ratings_sale_active, 0, 0, 0)
        fragmentRatingsTabLayout.getTabAt(1)?.customView = tabTwo

        val tabThree = LayoutInflater.from(activity).inflate(R.layout.custom_rating_tab, null) as TextView
        tabThree.text = titlesList[2]
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ratings_sale_active, 0, 0, 0)
        fragmentRatingsTabLayout.getTabAt(2)?.customView = tabThree

        val tabFour = LayoutInflater.from(activity).inflate(R.layout.custom_rating_tab, null) as TextView
        tabFour.text = titlesList[3]
        tabFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ratings_sale_active, 0, 0, 0)
        fragmentRatingsTabLayout.getTabAt(3)?.customView = tabFour

        fragmentRatingsTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                fragmentRatingsCustomViewPager.reMeasureCurrentPage(tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }
}
