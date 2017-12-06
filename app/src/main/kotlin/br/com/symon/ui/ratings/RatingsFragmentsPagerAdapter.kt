package br.com.symon.ui.ratings

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.com.symon.R


class RatingsFragmentsPagerAdapter(
        context: Context,
        fragmentManager: FragmentManager,
        private val fragmentList: MutableList<RatingsChildFragment>,
        private val titlesList: MutableList<String>)
    : FragmentStatePagerAdapter(fragmentManager) {

    private val mContext: Context = context

    override fun getCount(): Int = fragmentList.size
    override fun getItem(position: Int): Fragment = fragmentList[position]
    override fun getPageTitle(position: Int): CharSequence {
        return titlesList[position]
    }

    fun getTabView(position: Int): View {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.custom_rating_tab, null)
        val textView: TextView = view.findViewById<TextView>(R.id.customRatingTabTextView) as TextView
        val imageView: ImageView = view.findViewById<ImageView>(R.id.customRatingsTabImageView) as ImageView
        textView.text = titlesList[position]
        imageView.setImageDrawable(ResourcesCompat.getDrawable(mContext.resources, R.drawable.ic_ratings_sale_active, null))
        return view
    }
}