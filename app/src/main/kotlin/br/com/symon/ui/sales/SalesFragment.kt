package br.com.symon.ui.sales

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.common.toast
import br.com.symon.common.whenNotNullNorEmpty
import br.com.symon.data.model.Constants
import br.com.symon.data.model.Sale
import br.com.symon.data.model.User
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.injection.components.DaggerSalesFragmentComponent
import br.com.symon.injection.components.SalesFragmentComponent
import br.com.symon.injection.modules.SalesFragmentModule
import br.com.symon.ui.SalesAdapter
import kotlinx.android.synthetic.main.fragment_sales.*

class SalesFragment : BaseFragment(), SalesContract.View, SalesAdapter.onItemClickListener {

    private val salesFragmentComponent: SalesFragmentComponent
        get() = DaggerSalesFragmentComponent.builder()
                .applicationComponent((activity.application as CustomApplication).applicationComponent)
                .salesFragmentModule(SalesFragmentModule(this))
                .build()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var salesAdapter: SalesAdapter
    private var currentPage: Int = Constants.FIRST_PAGE

    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    private var loading = true
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        salesFragmentComponent.inject(this)
        fragmentId = SalesFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_sales)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        setUpRecyclersViews()
        fetchData(Constants.FIRST_PAGE)
//        setRecyclerViewScrollListener()
    }

    override fun showSales(salesListResponse: SalesListResponse) {
        salesListResponse.salesList.whenNotNullNorEmpty {
            salesAdapter = SalesAdapter(salesListResponse.salesList, this)
            salesFragmentSalesRecyclerView.adapter = salesAdapter

            salesFragmentSalesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = linearLayoutManager.childCount
                        totalItemCount = linearLayoutManager.itemCount
                        pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            fetchData(currentPage)
                            loading = false
                        }
                    }
                }
            })

            hideLoading()
            salesFragmentSalesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onSaleImageClick(sale: Sale) {
        activity.toast("onSaleImageClick")
    }

    override fun onLikeSaleClick(sale: Sale) {
        activity.toast("onLikeSaleClick")
    }

    override fun onDislikeSaleClick(sale: Sale) {
        activity.toast("onDislikeSaleClick")
    }

    override fun onOptionsSaleClick(user: User) {
        activity.toast("onOptionsSaleClick")
    }

    private fun fetchData(page: Int) {
        salesFragmentComponent.salesPresenter().loadSales(
                if (page > 1) page else Constants.FIRST_PAGE,
                Constants.RESULTS_PER_PAGE
        )
    }

    private fun setUpRecyclersViews() {
        linearLayoutManager = LinearLayoutManager(context)
        salesFragmentSalesRecyclerView.setHasFixedSize(true)
        salesFragmentSalesRecyclerView.layoutManager = linearLayoutManager
        salesFragmentSalesRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setRecyclerViewScrollListener() {
        salesFragmentSalesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager.itemCount
                if (totalItemCount == lastVisibleItemPosition + 1) {
                    currentPage++
                    fetchData(currentPage)
                }
            }
        })
    }
}
