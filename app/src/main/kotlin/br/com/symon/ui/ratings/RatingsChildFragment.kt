package br.com.symon.ui.ratings

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.common.toast
import br.com.symon.common.widget.EndlessScrollListener
import br.com.symon.data.model.Constants
import br.com.symon.data.model.Constants.Companion.FIRST_PAGE
import br.com.symon.data.model.Sale
import br.com.symon.data.model.User
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerRatingsChildFragmentComponent
import br.com.symon.injection.components.RatingsChildFragmentComponent
import br.com.symon.injection.modules.RatingsChildFragmentModule
import br.com.symon.ui.saleDetail.SaleDetailActivity
import br.com.symon.ui.sales.SalesAdapter
import kotlinx.android.synthetic.main.fragment_ratings_child.*

class RatingsChildFragment : BaseFragment(), RatingsChildFragmentContract.View, SalesAdapter.OnItemClickListener {

    enum class RatingsChildType(val ratingChildType: Int) {
        FAVORITES(0),
        LIKES(1),
        DISLIKES(2),
        COMMENTS(3)
    }

    private val ratingsChildComponent: RatingsChildFragmentComponent
        get() = DaggerRatingsChildFragmentComponent.builder()
                .applicationComponent((activity.application as CustomApplication).applicationComponent)
                .ratingsChildFragmentModule(RatingsChildFragmentModule(this))
                .build()

    companion object {
        private const val API_OPTION_KEY: String = "API_OPTION_KEY"
        const val EXTRA_ORDER_BY: String = "EXTRA_ORDER_BY"

        fun newInstance(apiOptionKey: RatingsChildType, orderBy: Int): RatingsChildFragment {
            val f = RatingsChildFragment()
            val args = Bundle()
            args.putSerializable(API_OPTION_KEY, apiOptionKey)
            args.putInt(EXTRA_ORDER_BY, orderBy)
            f.arguments = args
            return f
        }
    }

    interface OnRatingsChildListener {
        fun onResponseQuantityLoaded(apiOptionKey: RatingsChildType, quantity: Int)
        fun onTabsUpdateNeeded()
    }

    private var extraOrderBy: Int = 0
    private lateinit var apiOptionKey: RatingsChildType
    private lateinit var userTokenResponse: UserTokenResponse
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var salesAdapter: SalesAdapter
    private var currentPage: Int = Constants.FIRST_PAGE

    private lateinit var onResponseLoaded: OnRatingsChildListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (parentFragment is OnRatingsChildListener) {
            onResponseLoaded = parentFragment as OnRatingsChildListener
        } else {
            throw RuntimeException("The parent fragment must implement OnResponseLoaded")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratingsChildComponent.inject(this)
        fragmentId = RatingsChildFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_ratings_child)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            apiOptionKey = arguments.getSerializable(API_OPTION_KEY) as RatingsChildType
            extraOrderBy = arguments.getInt(RatingsChildFragment.EXTRA_ORDER_BY, RatingsFragment.OrderBy.NEWEST.value)
        }

        showLoading()
        setUpRecyclersViews()
        ratingsChildComponent.ratingsChildFragmentPresenter().getUserCache()
    }

    override fun setUser(userTokenResponse: UserTokenResponse) {
        this.userTokenResponse = userTokenResponse
        fetchData(FIRST_PAGE)
    }

    override fun showTabResponse(salesListResponse: SalesListResponse) {
        if (salesListResponse.salesList.isNotEmpty()) {
            setSalesAdapter(salesListResponse)
            ratingsChildFragmentRecyclerView.visibility = View.VISIBLE
            ratingsChildFragmentNoContent.visibility = View.GONE
        } else {
            ratingsChildFragmentRecyclerView.visibility = View.GONE
            ratingsChildFragmentNoContent.visibility = View.VISIBLE
        }
        onResponseLoaded.onResponseQuantityLoaded(apiOptionKey, salesListResponse.totalItems)
        hideLoading()
    }

    override fun onSaleImageClick(sale: Sale) {
        val saleDetailActivity = SaleDetailActivity.newIntent(activity, sale, userTokenResponse)
        startActivity(saleDetailActivity)
    }

    override fun onLikeSaleClick(position: Int, sale: Sale) {
        ratingsChildComponent.ratingsChildFragmentPresenter().likeSale(position, sale.id, userTokenResponse.token)
    }

    override fun onDislikeSaleClick(position: Int, sale: Sale) {
        ratingsChildComponent.ratingsChildFragmentPresenter().disLikeSale(position, sale.id, userTokenResponse.token)
    }

    override fun onReportSaleClick(sale: Sale) {
        activity.toast("showReportSaleResponse")
    }

    override fun onBlockUserClick(user: User) {
        activity.toast("showBlockUserResponse")
    }

    override fun updateActionSAle(position: Int, isLike: Boolean) {
        onResponseLoaded.onTabsUpdateNeeded()
        salesAdapter.updateItem(position, isLike)
    }

    override fun showReportSaleResponse() {
        activity.toast("showReportSaleResponse")
    }

    override fun showBlockUserResponse() {
        activity.toast("showBlockUserResponse")
    }

    fun updateFragment() {
        fetchData(FIRST_PAGE)
    }

    private fun setUpRecyclersViews() {
        linearLayoutManager = LinearLayoutManager(context)
        ratingsChildFragmentRecyclerView.setHasFixedSize(true)
        ratingsChildFragmentRecyclerView.layoutManager = linearLayoutManager
        ratingsChildFragmentRecyclerView.isNestedScrollingEnabled = false
        ratingsChildFragmentRecyclerView.itemAnimator.changeDuration = 0
    }

    private fun fetchData(page: Int) {
        ratingsChildComponent.ratingsChildFragmentPresenter().loadTab(apiOptionKey, userTokenResponse.token,
                if (page > 1) page else Constants.FIRST_PAGE,
                Constants.RESULTS_PER_PAGE, extraOrderBy)
    }

    private fun setSalesAdapter(salesListResponse: SalesListResponse) {
        if (currentPage == Constants.FIRST_PAGE) {
            salesAdapter = SalesAdapter(salesListResponse.salesList, userTokenResponse.user!!, this)
            ratingsChildFragmentRecyclerView.adapter = salesAdapter
        } else {
            salesAdapter.addList(salesListResponse.salesList)
        }
        setRecyclerViewScrollListener(salesListResponse)
    }

    private fun setRecyclerViewScrollListener(salesListResponse: SalesListResponse) {
        ratingsChildFragmentRecyclerView.addOnScrollListener(EndlessScrollListener({
            if (currentPage < salesListResponse.totalPages) {
                currentPage++
                fetchData(currentPage)
            }
        }, linearLayoutManager))
    }
}
