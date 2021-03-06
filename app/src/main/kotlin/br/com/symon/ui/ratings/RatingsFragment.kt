package br.com.symon.ui.ratings

import android.content.Context
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

    enum class OrderBy(val value: Int) {
        NEWEST(0),
        CHEAPER(1),
        EXPENSIVE(2)
    }

    private var extraOrderBy: Int = 0

    companion object {
        private const val EXTRA_ORDER_BY = "EXTRA_ORDER_BY"

        fun newInstance(orderBy: Int): RatingsFragment {
            val f = RatingsFragment()
            val args = Bundle()
            args.putInt(EXTRA_ORDER_BY, orderBy)
            f.arguments = args
            return f
        }
    }

    interface OnRatingsListener {
        fun onUserAvatarClick(userId: Int)
    }

    private lateinit var ratingsFragmentAdapter: RatingsFragmentsPagerAdapter
    private var mLastSelectedIndex: Int = 0
    private var mNeedRefresh: Boolean = false
    private lateinit var onRatingsListener: OnRatingsListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnRatingsListener) {
            onRatingsListener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnRageComicSelected.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentId = RatingsFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_ratings)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null)
            extraOrderBy = arguments.getInt(RatingsFragment.EXTRA_ORDER_BY, OrderBy.NEWEST.value)

        val salesRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.FAVORITES, extraOrderBy)
        val likesRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.LIKES, extraOrderBy)
        val dislikesRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.DISLIKES, extraOrderBy)
        val commentsRatingFragment = RatingsChildFragment.newInstance(RatingsChildFragment.RatingsChildType.COMMENTS, extraOrderBy)

        val fragmentsList: MutableList<RatingsChildFragment> = ArrayList()
        fragmentsList.add(salesRatingFragment)
        fragmentsList.add(likesRatingFragment)
        fragmentsList.add(dislikesRatingFragment)
        fragmentsList.add(commentsRatingFragment)

        val titlesList: MutableList<String> = ArrayList()
        titlesList.add("0")
        titlesList.add("0")
        titlesList.add("0")
        titlesList.add("0")

        ratingsFragmentAdapter = RatingsFragmentsPagerAdapter(activity, childFragmentManager, fragmentsList, titlesList)
        fragmentRatingsCustomViewPager.adapter = ratingsFragmentAdapter
        fragmentRatingsCustomViewPager.offscreenPageLimit = 4
        fragmentRatingsCustomViewPager.currentItem = mLastSelectedIndex
        fragmentRatingsTabLayout.setupWithViewPager(fragmentRatingsCustomViewPager)
        fragmentRatingsTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                mLastSelectedIndex = tab.position
                if (mNeedRefresh) {
                    updateChildFragment()
                    if (tab.position !in 1..2)
                        mNeedRefresh = false
                }
            }
        })

        for (i in 0 until fragmentRatingsTabLayout.tabCount) {
            val tab = fragmentRatingsTabLayout.getTabAt(i)
            tab?.customView = ratingsFragmentAdapter.getTabView(i)
        }
    }

    override fun onResponseQuantityLoaded(apiOptionKey: RatingsChildFragment.RatingsChildType, quantity: Int) {
        val textView = fragmentRatingsTabLayout.getTabAt(apiOptionKey.ratingChildType)?.customView?.findViewById(R.id.customRatingTabTextView) as TextView
        textView.text = quantity.toString()
    }

    override fun onTabsUpdateNeeded() {
        mNeedRefresh = true
        when (fragmentRatingsTabLayout.selectedTabPosition) {
            1 -> updateChildFragment()
            2 -> updateChildFragment()
        }
    }

    override fun onUserAvatarClick(userId: Int) {
        onRatingsListener.onUserAvatarClick(userId)
    }

    private fun updateChildFragment() {
        val currentFragment = ratingsFragmentAdapter.getItem(fragmentRatingsTabLayout.selectedTabPosition) as RatingsChildFragment
        currentFragment.updateFragment()
    }
}
