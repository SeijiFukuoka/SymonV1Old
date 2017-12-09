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


class RatingsFragment : BaseFragment(), RatingsContract, RatingsChildFragment.OnRatingsChildListener {

    private lateinit var ratingsFragmentAdapter: RatingsFragmentsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentId = RatingsFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_ratings)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val salesRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.FAVORITES)
        val likesRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.LIKES)
        val dislikesRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.DISLIKES)
        val commentsRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.COMMENTS)

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

        ratingsFragmentAdapter = RatingsFragmentsPagerAdapter(activity, childFragmentManager, fragmentsList, titlesList)
        fragmentRatingsCustomViewPager.adapter = ratingsFragmentAdapter
        fragmentRatingsCustomViewPager.offscreenPageLimit = 4
        fragmentRatingsTabLayout.setupWithViewPager(fragmentRatingsCustomViewPager)

        for (i in 0 until fragmentRatingsTabLayout.tabCount) {
            val tab = fragmentRatingsTabLayout.getTabAt(i)
            tab?.customView = ratingsFragmentAdapter.getTabView(i)
        }

        fragmentRatingsTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
//                fragmentRatingsCustomViewPager.reMeasureCurrentPage(tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun onResponseQuantityLoaded(apiOptionKey: RatingsChildFragment.RatingsChildType, quantity: Int) {
        val textView = fragmentRatingsTabLayout.getTabAt(apiOptionKey.ratingChildType)?.customView?.findViewById(R.id.customRatingTabTextView) as TextView
        textView.text = quantity.toString()
    }

    override fun onTabsUpdateNeeded() {
        (0 until fragmentRatingsTabLayout.tabCount)
                .asSequence()
                .map { ratingsFragmentAdapter.getItem(it) as RatingsChildFragment }
                .forEach { it.updateFragment() }
    }
}
