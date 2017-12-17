package br.com.symon.ui.saleDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.loadUrl
import br.com.symon.common.toast
import br.com.symon.common.widget.EllipsizingTextView
import br.com.symon.data.model.Comment
import br.com.symon.data.model.Sale
import br.com.symon.injection.components.DaggerSaleDetailActivityComponent
import br.com.symon.injection.components.SaleDetailActivityComponent
import br.com.symon.injection.modules.SaleDetailActivityModule
import kotlinx.android.synthetic.main.activity_sale_detail.*
import java.text.NumberFormat
import java.util.*


class SaleDetailActivity : BaseActivity(), SaleDetailContract.View, SaleCommentAdapter.OnItemClickListener {

    private lateinit var extraSaleDetail: Sale

    companion object {
        private const val EXTRA_SALE = "EXTRA_SALE"

        fun newIntent(context: Context, sale: Sale): Intent {
            val intent = Intent(context, SaleDetailActivity::class.java)
            intent.putExtra(SaleDetailActivity.EXTRA_SALE, sale)
            return intent
        }
    }

    private val saleDetailActivityComponent: SaleDetailActivityComponent
        get() = DaggerSaleDetailActivityComponent
                .builder()
                .applicationComponent((application as CustomApplication).applicationComponent)
                .saleDetailActivityModule(SaleDetailActivityModule(this))
                .build()

    private lateinit var saleCommentAdapter: SaleCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_detail)

        saleDetailActivityComponent.inject(this)

        extraSaleDetail = intent.getParcelableExtra(EXTRA_SALE)

        setSupportActionBar(saleDetailActivityToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupToolbar()
        setContent()
        verifyComments()
    }

    override fun showComments(commentList: MutableList<Comment>) {
        if (commentList.isNotEmpty()) {

            var linearLayoutManager = LinearLayoutManager(this)
            saleDetailActivityCommentRecyclerView.setHasFixedSize(true)
            saleDetailActivityCommentRecyclerView.layoutManager = linearLayoutManager
            saleDetailActivityCommentRecyclerView.isNestedScrollingEnabled = false

            saleCommentAdapter = SaleCommentAdapter(commentList, this)
            saleDetailActivityCommentRecyclerView.adapter = saleCommentAdapter

            saleDetailActivityCommentListLabelTextView.visibility = View.VISIBLE
            saleDetailActivityCommentRecyclerView.visibility = View.VISIBLE
        } else {
            saleDetailActivityCommentListLayout.setPadding(0, resources.getDimensionPixelSize(R.dimen.margin_10_dp), 0, 0)
            saleDetailActivityCommentListLabelTextView.visibility = View.GONE
            saleDetailActivityCommentRecyclerView.visibility = View.GONE
        }
    }

    override fun showFavoriteResponse() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSendCommentResponse() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBlockUserClick(userId: Int) {
        toast("onBlockUserClick")
    }

    private fun setupToolbar() {
        saleDetailActivityHeaderProgressView.bind(3)
        saleDetailActivityToolbarTextView.text = extraSaleDetail.message

//        TODO("Falta a API devolver se o sale é favorito ou não")
        if (extraSaleDetail.hasLiked)
            saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_off))
        else
            saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_on))

        saleDetailActivityBackArrowImageView.setOnClickListener {
            onBackPressed()
        }

        saleDetailActivityFavoriteImageView.setOnClickListener {
            toast("saleDetailActivityFavoriteImageView")
        }

        saleDetailActivityCommentImageView.setOnClickListener {
            toast("saleDetailActivityCommentImageView")
        }
    }

    private fun setContent() {
        saleDetailActivitySaleImageView.loadUrl(extraSaleDetail.photo)
        saleDetailActivitySaleDetailPriceValue.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(extraSaleDetail.price)
        saleDetailActivitySaleProductValue.text = extraSaleDetail.message
        saleDetailActivitySalePlaceValue.text = extraSaleDetail.place

        saleDetailActivitySalePlaceValue.maxLines = 1

        saleDetailActivitySalePlaceValue.addEllipsizeListener(object : EllipsizingTextView.EllipsizeListener {
            override fun ellipsizeStateChanged(ellipsized: Boolean) {
                if (ellipsized)
                    saleDetailActivitySalePlaceValueArrow.visibility = View.VISIBLE
            }
        })

        saleDetailActivityLikeLayout.isClickable = false
        itemSaleLikeQuantityTextView.text = extraSaleDetail.likes.toString()

        if (extraSaleDetail.hasLiked) {
            saleDetailActivityLikeLayout.isSelected = true
            itemSaleLikeImageView.isSelected = true
            itemSaleLikeQuantityTextView.isSelected = true
        }

        saleDetailActivityDislikeLayout.isClickable = false
        itemSaleDislikeQuantityTextView.text = extraSaleDetail.dislikes.toString()

        if (extraSaleDetail.hasDisliked) {
            saleDetailActivityDislikeLayout.isSelected = true
            itemSaleDislikeImageView.isSelected = true
            itemSaleDislikeQuantityTextView.isSelected = true
        }
    }

    private fun verifyComments() {
        extraSaleDetail.id.let { saleDetailActivityComponent.providePresenter().getComments(it) }
    }
}