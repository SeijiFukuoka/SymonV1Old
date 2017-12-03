package br.com.symon.ui.sales

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.common.toast
import br.com.symon.common.whenNotNullNorEmpty
import br.com.symon.common.widget.EndlessScrollListener
import br.com.symon.data.model.Constants
import br.com.symon.data.model.Constants.Companion.FIRST_PAGE
import br.com.symon.data.model.Constants.Companion.SEEK_BAR_MAX
import br.com.symon.data.model.Constants.Companion.SEEK_BAR_MIN
import br.com.symon.data.model.Sale
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerSalesFragmentComponent
import br.com.symon.injection.components.SalesFragmentComponent
import br.com.symon.injection.modules.SalesFragmentModule
import br.com.symon.ui.SalesAdapter
import kotlinx.android.synthetic.main.fragment_sales.*
import java.util.*


class SalesFragment : BaseFragment(), SalesContract.View, SalesAdapter.OnItemClickListener, SeekBar.OnSeekBarChangeListener {

    private var extraSearchQuery: String? = ""

    companion object {
        private const val EXTRA_SEARCH_QUERY = "EXTRA_SEARCH_QUERY"

        fun newInstance(searchQuery: String?): SalesFragment {
            val f = SalesFragment()
            val args = Bundle()
            args.putString(EXTRA_SEARCH_QUERY, searchQuery)
            f.arguments = args
            return f
        }
    }

    private val salesFragmentComponent: SalesFragmentComponent
        get() = DaggerSalesFragmentComponent.builder()
                .applicationComponent((activity.application as CustomApplication).applicationComponent)
                .salesFragmentModule(SalesFragmentModule(this))
                .build()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var salesAdapter: SalesAdapter
    private var currentPage: Int = Constants.FIRST_PAGE
    private var user: UserTokenResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        salesFragmentComponent.inject(this)
        fragmentId = SalesFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_sales)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && arguments.getString(EXTRA_SEARCH_QUERY) != null)
            extraSearchQuery = arguments.getString(EXTRA_SEARCH_QUERY)
        showLoading()
        setUpRecyclersViews()
        getUser()
        setUpSeekBar()

        salesFragmentSalesSearchCloseTextView.setOnClickListener {
            showLoading()
            salesFragmentSalesSearchHeaderLayout.visibility = View.GONE
            salesFragmentSalesRecyclerView.visibility = View.GONE
            fetchData(FIRST_PAGE)
        }

        salesFragmentSalesSearchCloseImageView.setOnClickListener {
            showLoading()
            salesFragmentSalesSearchHeaderLayout.visibility = View.GONE
            salesFragmentSalesRecyclerView.visibility = View.GONE
            fetchData(FIRST_PAGE)
        }
    }

    override fun setUser(userTokenResponse: UserTokenResponse) {
        user = userTokenResponse
        if (extraSearchQuery!!.isNotEmpty()) {
            fetchSearchQuery()
        } else {
            fetchData(Constants.FIRST_PAGE)
        }
    }

    override fun showSales(salesListResponse: SalesListResponse) {
        salesListResponse.salesList.whenNotNullNorEmpty {
            if (currentPage == Constants.FIRST_PAGE) {
                salesAdapter = SalesAdapter(salesListResponse.salesList, user?.user!!, this)
                salesFragmentSalesRecyclerView.adapter = salesAdapter
                hideLoading()
                salesFragmentSalesRecyclerView.visibility = View.VISIBLE
            } else {
                salesAdapter.addList(salesListResponse.salesList)
            }
            setRecyclerViewScrollListener(salesListResponse)
        }
    }

    override fun updateActionSAle(position: Int, isLike: Boolean) {
        salesAdapter.updateItem(position, isLike)
    }

    override fun onSaleImageClick(sale: Sale) {
        activity.toast("onSaleImageClick ID = ${sale.id}")
    }

    override fun onLikeSaleClick(position: Int, sale: Sale) {
        salesFragmentComponent.salesPresenter().likeSale(position, sale.id!!, user?.token!!)
        activity.toast("onLikeSaleClick")
    }

    override fun onDislikeSaleClick(position: Int, sale: Sale) {
        salesFragmentComponent.salesPresenter().disLikeSale(position, sale.id!!, user?.token!!)
        activity.toast("onDislikeSaleClick")
    }

    override fun onReportSaleClick(sale: Sale) {
        salesFragmentComponent.salesPresenter().reportSale(user?.token, saleReportRequest = SaleReportRequest(sale.id))
    }

    override fun onBlockUserClick(blockUser: User) {
        salesFragmentComponent.salesPresenter().blockUser(user?.token, userBlockedId = BlockUserRequest(blockUser.id))
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
            when (progress) {
                in 0..5 -> {
                    salesFragmentHeaderRangeSeekBar.progress = Constants.SEEK_BAR_MIN
                    salesFragmentHeaderRangeTextView.text = String.format(Locale.getDefault(), resources.getString(R.string.sales_fragment_range_filter_text_formatted), 5)
                    salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_low, null))
//                    TODO("PENDENTE CHAMADA DA API")
                }
                6 -> {
                    salesFragmentHeaderRangeTextView.text = getRangeFilterText()
                    salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_low, null))
//                    TODO("PENDENTE CHAMADA DA API")
                }
                in 7..12 -> {
                    salesFragmentHeaderRangeTextView.text = getRangeFilterText()
                    salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_medium, null))
//                    TODO("PENDENTE CHAMADA DA API")
                }
                else -> {
                    salesFragmentHeaderRangeTextView.text = getRangeFilterText()
                    salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_high, null))
//                    TODO("PENDENTE CHAMADA DA API")
                }
            }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun showSearchSales(salesList: MutableList<Sale>) {
        if (salesList.isNotEmpty()) {
            if (currentPage == Constants.FIRST_PAGE) {
                salesAdapter = SalesAdapter(salesList, user?.user!!, this)
                salesFragmentSalesRecyclerView.adapter = salesAdapter
                hideLoading()
                salesFragmentSalesRecyclerView.visibility = View.VISIBLE
            } else {
                salesAdapter.addList(salesList)
            }
        } else {
            activity.toast("Nenhum resultado encontrado para a palavra $extraSearchQuery")
        }
        hideLoading()
    }

    override fun showReportSaleResponse() {
        activity.toast("showReportSaleResponse")
    }

    override fun showBlockUserResponse() {
        activity.toast("showBlockUserResponse")
    }

    private fun setUpRecyclersViews() {
        linearLayoutManager = LinearLayoutManager(context)
        salesFragmentSalesRecyclerView.setHasFixedSize(true)
        salesFragmentSalesRecyclerView.layoutManager = linearLayoutManager
        salesFragmentSalesRecyclerView.isNestedScrollingEnabled = false
        salesFragmentSalesRecyclerView.itemAnimator.changeDuration = 0
    }

    private fun getUser() {
        salesFragmentComponent.salesPresenter().getUser()
    }

    private fun setUpSeekBar() {
        salesFragmentHeaderRangeSeekBar.progress = SEEK_BAR_MIN
        salesFragmentHeaderRangeTextView.text = String.format(Locale.getDefault(), resources.getString(R.string.sales_fragment_range_filter_text_formatted), salesFragmentHeaderRangeSeekBar.progress)
        salesFragmentHeaderRangeSeekBar!!.max = SEEK_BAR_MAX
        salesFragmentHeaderRangeSeekBar!!.setOnSeekBarChangeListener(this)
    }

    private fun fetchData(page: Int) {
        salesFragmentComponent.salesPresenter().loadSales(user?.token!!,
                if (page > 1) page else Constants.FIRST_PAGE,
                Constants.RESULTS_PER_PAGE
        )
    }

    private fun fetchSearchQuery() {
        salesFragmentSalesSearchHeaderLayout.visibility = View.VISIBLE
        salesFragmentSalesSearchQueryTextView.text = String.format(Locale.getDefault(), resources.getString(R.string.sales_fragment_search_query_formatted), extraSearchQuery)
        salesFragmentComponent.salesPresenter().searchQuerySale(user?.token!!, extraSearchQuery!!)
    }

    private fun setRecyclerViewScrollListener(salesListResponse: SalesListResponse) {
        salesFragmentSalesRecyclerView.addOnScrollListener(EndlessScrollListener({
            if (currentPage < salesListResponse.totalPages) {
                currentPage++
                fetchData(currentPage)
            }
        }, linearLayoutManager))
    }

    private fun getRangeFilterText(): String =
            String.format(Locale.getDefault(), resources.getString(R.string.sales_fragment_range_filter_text_formatted), salesFragmentHeaderRangeSeekBar.progress)
}
