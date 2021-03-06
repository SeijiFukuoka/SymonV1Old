package br.com.symon.ui.sales

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.common.setPrimaryColors
import br.com.symon.common.toast
import br.com.symon.common.widget.EndlessScrollListener
import br.com.symon.data.model.Constants
import br.com.symon.data.model.Constants.Companion.FIRST_PAGE
import br.com.symon.data.model.Constants.Companion.NEED_UPDATE_RESULT
import br.com.symon.data.model.Constants.Companion.SEEK_BAR_MAX
import br.com.symon.data.model.Sale
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerSalesFragmentComponent
import br.com.symon.injection.components.SalesFragmentComponent
import br.com.symon.injection.modules.SalesFragmentModule
import br.com.symon.ui.saleDetail.SaleDetailActivity
import br.com.symon.ui.send.SendSaleActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sales.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.*


class SalesFragment : BaseFragment(), SalesContract.View, SalesAdapter.OnItemClickListener, SeekBar.OnSeekBarChangeListener {

    companion object {
        private const val EXTRA_SEARCH_QUERY = "EXTRA_SEARCH_QUERY"
        const val REQUEST_SALE_DETAIL = 123

        fun newInstance(searchQuery: String?): SalesFragment {
            val f = SalesFragment()
            val args = Bundle()
            args.putString(EXTRA_SEARCH_QUERY, searchQuery)
            f.arguments = args
            return f
        }
    }

    interface OnSalesFragmentListener {
        fun onUserAvatarClick(userId: Int)
    }

    private val salesFragmentComponent: SalesFragmentComponent
        get() = DaggerSalesFragmentComponent.builder()
                .applicationComponent((activity.application as CustomApplication).applicationComponent)
                .salesFragmentModule(SalesFragmentModule(this))
                .build()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var salesAdapter: SalesAdapter
    private lateinit var locationProvider: ReactiveLocationProvider
    private lateinit var onSalesFragmentListener: OnSalesFragmentListener

    private var extraSearchQuery: String? = ""
    private var currentPage: Int = Constants.FIRST_PAGE
    private var user: UserTokenResponse? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var radius: Int = Constants.SEEK_BAR_MIN
    private var unblockUserId: Int = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnSalesFragmentListener) {
            onSalesFragmentListener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnSalesFragmentListener.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        salesFragmentComponent.inject(this)
        fragmentId = SalesFragment::class.java.canonicalName

        getUser()

        locationProvider = ReactiveLocationProvider(activity)

        RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe({ granted ->
                    if (granted) {
                        getLastKnowLocation()
                    } else {
                        hideLoading()
                        activity.toast(getString(R.string.general_permissions_message))
                    }
                })
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
        setUpSeekBar()

        salesFragmentSendSuccessContainerLinearLayout.setOnClickListener {
            salesFragmentSendSuccessContainerLinearLayout.visibility = View.GONE
        }

        salesFragmentSalesSearchCloseTextView.setOnClickListener {
            resetSaleData()
        }

        salesFragmentSalesSearchCloseImageView.setOnClickListener {
            resetSaleData()
        }

        salesFragmentSalesSwipeRefreshLayout.setPrimaryColors()
        salesFragmentSalesSwipeRefreshLayout.setOnRefreshListener {
            salesFragmentSalesSwipeRefreshLayout.isRefreshing = true
            fetchFirstPage(FIRST_PAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SALE_DETAIL && resultCode == NEED_UPDATE_RESULT) {
            fetchFirstPage(FIRST_PAGE)
        }
    }

    override fun setUser(userTokenResponse: UserTokenResponse) {
        user = userTokenResponse
    }

    override fun showSales(salesListResponse: SalesListResponse) {
        if (!salesListResponse.salesList.isEmpty()) {
            setSalesAdapter(salesListResponse)
        } else {
            salesAdapter.clear()
            activity.toast("Nenhum resultado encontrado na região procurada")
        }
        showContent(showContent = true)
        if (salesFragmentSalesSwipeRefreshLayout.isRefreshing)
            salesFragmentSalesSwipeRefreshLayout.isRefreshing = false
    }

    override fun showSearchSales(salesListResponse: SalesListResponse) {
        if (!salesListResponse.salesList.isEmpty()) {
            setSalesAdapter(salesListResponse)
        } else {
            activity.toast("Nenhum resultado encontrado para a palavra $extraSearchQuery")
        }
        showContent(showContent = true)
    }

    override fun updateActionSAle(position: Int, isLike: Boolean) {
        salesAdapter.updateItem(position, isLike)
    }

    override fun onSaleImageClick(sale: Sale) {
        val saleDetailActivity = SaleDetailActivity.newIntent(activity, sale, user!!)
        startActivityForResult(saleDetailActivity, REQUEST_SALE_DETAIL)
    }

    override fun onLikeSaleClick(position: Int, sale: Sale) {
        salesFragmentComponent.salesPresenter().likeSale(position, sale.id, user?.token!!)
    }

    override fun onDislikeSaleClick(position: Int, sale: Sale) {
        salesFragmentComponent.salesPresenter().disLikeSale(position, sale.id, user?.token!!)
    }

    override fun onReportSaleClick(sale: Sale) {
        salesFragmentComponent.salesPresenter().reportSale(user?.token, saleReportRequest = SaleReportRequest(sale.id))
    }

    override fun onBlockUserClick(userId: Int) {
        unblockUserId = userId
        salesFragmentComponent.salesPresenter().blockUser(user!!.token, BlockUserRequest(unblockUserId))
    }

    override fun onUserAvatarClick(userId: Int) {
        onSalesFragmentListener.onUserAvatarClick(userId)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        radius = progress
        when (progress) {
            0 -> {
                radius = 1
                seekBar?.progress = 1
                salesFragmentHeaderRangeTextView.text = String.format(Locale.getDefault(), resources.getString(R.string.sales_fragment_range_filter_text_formatted), 1)
                salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_low, null))
            }
            in 1..5 -> {
                salesFragmentHeaderRangeTextView.text = getRangeFilterText()
                salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_low, null))
            }
            6 -> {
                salesFragmentHeaderRangeTextView.text = getRangeFilterText()
                salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_low, null))
            }
            in 7..12 -> {
                salesFragmentHeaderRangeTextView.text = getRangeFilterText()
                salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_medium, null))
            }
            else -> {
                salesFragmentHeaderRangeTextView.text = getRangeFilterText()
                salesFragmentHeaderRangeImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_range_filter_high, null))
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        fetchData(FIRST_PAGE)
    }

    override fun showReportSaleResponse() {
        activity.toast("showReportSaleResponse")
    }

    override fun showBlockUserResponse(blockedUserRequest: BlockUserRequest) {
        salesAdapter.filterBlockedUsers(blockedUserRequest.userBlockedId)
    }

    override fun showBlockUserResponseError() {
        activity.toast("Erro ao bloquear o usuário, tente novamente mais tarde")
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
        salesFragmentHeaderRangeSeekBar.progress = 1
        salesFragmentHeaderRangeTextView.text = String.format(Locale.getDefault(), resources.getString(R.string.sales_fragment_range_filter_text_formatted), 1)
        salesFragmentHeaderRangeSeekBar!!.max = SEEK_BAR_MAX
        salesFragmentHeaderRangeSeekBar!!.setOnSeekBarChangeListener(this)
    }

    private fun fetchData(page: Int) {
        if (extraSearchQuery!!.isNotEmpty()) {
            fetchSearchQuery(page)
        } else {
            fetchFirstPage(page)
        }
    }

    private fun fetchFirstPage(page: Int) {
        if (page == Constants.FIRST_PAGE) {
            showContent(showContent = false)
            currentPage = 1
        }

        salesFragmentComponent.salesPresenter().loadSales(
                userToken = user?.token!!,
                page = if (page > 1) page else Constants.FIRST_PAGE,
                pageSize = Constants.RESULTS_PER_PAGE,
                radius = radius,
                latitude = latitude,
                longitude = longitude
        )
    }

    private fun fetchSearchQuery(page: Int) {
        salesFragmentSalesSearchHeaderLayout.visibility = View.VISIBLE
        salesFragmentSalesSearchQueryTextView.text = String.format(Locale.getDefault(), resources.getString(R.string.sales_fragment_search_query_formatted), extraSearchQuery)
        salesFragmentComponent.salesPresenter().searchQuerySale(user?.token!!, extraSearchQuery!!,
                if (page > 1) page else Constants.FIRST_PAGE,
                Constants.RESULTS_PER_PAGE
        )
    }

    private fun setSalesAdapter(salesListResponse: SalesListResponse) {
        if (currentPage == Constants.FIRST_PAGE) {
            salesAdapter = SalesAdapter(salesListResponse.salesList, user?.user!!, this)
            salesFragmentSalesRecyclerView.adapter = salesAdapter
            showContent(showContent = true)
        } else {
            salesAdapter.addList(salesListResponse.salesList)
        }
        setRecyclerViewScrollListener(salesListResponse)
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

    private fun resetSaleData() {
        salesFragmentSalesSearchHeaderLayout?.visibility = View.GONE
        extraSearchQuery = ""
        fetchFirstPage(FIRST_PAGE)
    }

    fun showSendSuccessMessage() {
        salesFragmentSendSuccessContainerLinearLayout?.visibility = View.VISIBLE
        resetSaleData()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnowLocation() {
        locationProvider.lastKnownLocation
                .subscribe({
                    getAddressFromLocation(it)
                })
    }

    private fun getAddressFromLocation(location: Location) {
        val reverseGeocodeObservable: Observable<MutableList<Address>> =
                locationProvider.getReverseGeocodeObservable(
                        location.latitude, location.longitude, SendSaleActivity.MAX_ADDRESSES)

        reverseGeocodeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    latitude = it[0].latitude
                    longitude = it[0].longitude
                    fetchData(FIRST_PAGE)
                }, {
                    GeneralErrorHandler(it, this, {})
                })
    }

    private fun showContent(showContent: Boolean) {
        if (showContent) {
            salesFragmentSalesSwipeRefreshLayout?.visibility = View.VISIBLE
            hideLoading()
        } else {
            salesFragmentSalesSwipeRefreshLayout?.visibility = View.GONE
            showLoading()
        }
    }
}
