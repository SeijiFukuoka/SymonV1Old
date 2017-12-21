package br.com.symon.ui.saleDetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ScrollView
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.hideKeyboard
import br.com.symon.common.loadUrl
import br.com.symon.common.toast
import br.com.symon.common.widget.EllipsizingTextView
import br.com.symon.data.model.Comment
import br.com.symon.data.model.Constants.Companion.NEED_UPDATE_RESULT
import br.com.symon.data.model.Sale
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.model.requests.SendSaleRequest
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerSaleDetailActivityComponent
import br.com.symon.injection.components.SaleDetailActivityComponent
import br.com.symon.injection.modules.SaleDetailActivityModule
import br.com.symon.ui.ratings.RatingsChildFragment
import br.com.symon.ui.send.SendSaleActivity
import kotlinx.android.synthetic.main.activity_sale_detail.*
import java.text.NumberFormat
import java.util.*


class SaleDetailActivity : BaseActivity(), SaleDetailContract.View, SaleCommentAdapter.OnItemClickListener {

    private lateinit var extraSaleDetail: Sale
    private lateinit var extraUser: UserTokenResponse
    private lateinit var extraUpdatedSale: SendSaleRequest

    private var mUserBlockedId: Int = 0
    private var mEmptyCommentList: Boolean = true
    private var mCurrentSaleIsFavorited: Boolean = false
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    companion object {
        private const val EXTRA_SALE = "EXTRA_SALE"
        private const val EXTRA_USER = "EXTRA_USER"
        const val EXTRA_UPDATED_SALE = "EXTRA_UPDATED_SALE"
        const val REQUEST_UPDATE_SALE = 10412

        fun newIntent(context: Context, sale: Sale, user: UserTokenResponse): Intent {
            val intent = Intent(context, SaleDetailActivity::class.java)
            intent.putExtra(SaleDetailActivity.EXTRA_SALE, sale)
            intent.putExtra(SaleDetailActivity.EXTRA_USER, user)
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
        extraUser = intent.getParcelableExtra(EXTRA_USER)

        setSupportActionBar(saleDetailActivityToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupToolbar()
        setContent()
        verifyComments()
    }

    override fun onBlockUserClick(userId: Int) {
        val userBlockRequest = BlockUserRequest(userId)
        mUserBlockedId = userId
        saleDetailActivityComponent.providePresenter().blockUser(extraUser.token, userBlockRequest)
    }

    override fun onDeleteCommentClick(commentId: Int, position: Int) {
        saleDetailActivityComponent.providePresenter().deleteComment(extraUser.token, commentId, position)
    }

    override fun showComments(commentList: MutableList<Comment>) {
        if (commentList.isNotEmpty()) {
            setUpRecyclerView()
            saleCommentAdapter = SaleCommentAdapter(commentList, extraUser.user?.id!!, this)
            saleDetailActivityCommentRecyclerView.adapter = saleCommentAdapter
            hideShowCommentList(hide = false)
        } else {
            saleDetailActivityCommentListLayout.setPadding(0, resources.getDimensionPixelSize(R.dimen.margin_10_dp), 0, 0)
            hideShowCommentList(hide = true)
        }
    }

    override fun showFavoriteResponse() {
        mCurrentSaleIsFavorited = when {
            mCurrentSaleIsFavorited -> {
                saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_off))
                false
            }
            else -> {
                saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_on))
                true
            }
        }

        setResult(RatingsChildFragment.RESPONSE_SALE_DETAIL_NEED_UPDATE)
    }

    override fun showSendCommentResponse(commentResponse: Comment) {
        if (mEmptyCommentList)
            createAndUpdateCommentList(commentResponse)
        else
            saleCommentAdapter.addItem(commentResponse)

        scrollDown(false)
        setResult(RatingsChildFragment.RESPONSE_SALE_DETAIL_NEED_UPDATE)
    }

    override fun showBlockUserResponse() {
        saleCommentAdapter.removeBlockUserComments(mUserBlockedId)
        if (saleCommentAdapter.itemCount == 0)
            hideShowCommentList(hide = true)

        setResult(RatingsChildFragment.RESPONSE_SALE_DETAIL_NEED_UPDATE)
    }

    override fun showDeleteCommentResponse(position: Int) {
        saleCommentAdapter.remove(position)
        if (saleCommentAdapter.itemCount <= 0)
            hideShowCommentList(hide = true)

        setResult(RatingsChildFragment.RESPONSE_SALE_DETAIL_NEED_UPDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UPDATE_SALE && resultCode == NEED_UPDATE_RESULT) {

            extraUpdatedSale = data?.getParcelableExtra(EXTRA_UPDATED_SALE)!!

            saleDetailActivityToolbarTextView.text = extraUpdatedSale.productName
            saleDetailActivitySaleDetailPriceValue.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(extraUpdatedSale.price)
            saleDetailActivitySaleProductValue.text = extraUpdatedSale.productName
            saleDetailActivitySalePlaceValue.text = extraUpdatedSale.placeName
            mLatitude = extraUpdatedSale.lat
            mLongitude = extraUpdatedSale.lng

            setResult(NEED_UPDATE_RESULT)
        }
    }

    private fun setupToolbar() {
        saleDetailActivityHeaderProgressView.bind(3)
        saleDetailActivityToolbarTextView.text = extraSaleDetail.message

        if (extraSaleDetail.user.id!! == extraUser.user!!.id) {
            saleDetailActivitySaleActionLayout.visibility = View.GONE
            saleDetailActivityEditSaleTextView.visibility = View.VISIBLE

            saleDetailActivityEditSaleTextView.setOnClickListener {
                val uri: Uri = Uri.parse(extraSaleDetail.photo)
                val postInfoIntent = SendSaleActivity.newIntent(this, uri, extraUser.token, extraSaleDetail)
                startActivityForResult(postInfoIntent, REQUEST_UPDATE_SALE)
            }

        } else {
            saleDetailActivitySaleActionLayout.visibility = View.VISIBLE
            saleDetailActivityEditSaleTextView.visibility = View.GONE
            setFavoriteState()

            saleDetailActivityFavoriteImageView.setOnClickListener {
                saleDetailActivityComponent.providePresenter().setFavorite(extraUser.token, extraSaleDetail.id)
            }

            saleDetailActivityCommentImageView.setOnClickListener {
                scrollDown(true)
            }
        }

        saleDetailActivityBackArrowImageView.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setFavoriteState() {
        mCurrentSaleIsFavorited = when {
            extraSaleDetail.favorited -> {
                saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_on))
                true
            }
            else -> {
                saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_off))
                false
            }
        }
    }

    private fun setContent() {
        saleDetailActivitySaleImageView.loadUrl(extraSaleDetail.photo)
        saleDetailActivitySaleDetailPriceValue.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(extraSaleDetail.price)
        saleDetailActivitySaleProductValue.text = extraSaleDetail.message
        saleDetailActivitySalePlaceValue.text = extraSaleDetail.place

        mLatitude = extraSaleDetail.latitude
        mLongitude = extraSaleDetail.longitude

        saleDetailActivitySalePlaceValue.maxLines = 1

        saleDetailActivitySalePlaceValue.addEllipsizeListener(object : EllipsizingTextView.EllipsizeListener {
            override fun ellipsizeStateChanged(ellipsized: Boolean) {
                if (ellipsized)
                    saleDetailActivitySalePlaceValueArrow.visibility = View.VISIBLE
            }
        })

        saleDetailActivitySalePlaceLayout.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$mLatitude,$mLongitude?q=${saleDetailActivitySalePlaceValue.text}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        }

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
        extraSaleDetail.id.let { saleDetailActivityComponent.providePresenter().getComments(extraUser.token, it) }
        saleDetailActivitySendCommentButton.setOnClickListener {
            handleSendCommentButtonClick()
        }

        saleDetailActivityCommentEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                handleSendCommentButtonClick()
                true
            } else false
        }
    }

    private fun setUpRecyclerView() {
        saleDetailActivityCommentRecyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun supportsPredictiveItemAnimations(): Boolean {
                return true
            }
        }
        saleDetailActivityCommentRecyclerView.layoutManager.supportsPredictiveItemAnimations()
        saleDetailActivityCommentRecyclerView.setHasFixedSize(false)
        saleDetailActivityCommentRecyclerView.isNestedScrollingEnabled = false
    }

    private fun createAndUpdateCommentList(comment: Comment) {
        setUpRecyclerView()

        val commentList: MutableList<Comment> = arrayListOf()
        commentList += comment

        saleCommentAdapter = SaleCommentAdapter(commentList, extraUser.user?.id!!, this)
        saleDetailActivityCommentRecyclerView.adapter = saleCommentAdapter

        saleDetailActivityCommentListLayout.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.margin_30_dp))
        hideShowCommentList(hide = false)
    }

    private fun scrollDown(requestFocus: Boolean) {
        saleDetailActivityScrollView.post({
            saleDetailActivityScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            if (requestFocus)
                saleDetailActivityCommentEditText.requestFocus()
        })
    }

    private fun hideShowCommentList(hide: Boolean) {
        if (hide) {
            mEmptyCommentList = true
            saleDetailActivityCommentListLabelTextView.visibility = View.GONE
            saleDetailActivityCommentRecyclerView.visibility = View.GONE
        } else {
            mEmptyCommentList = false
            saleDetailActivityCommentListLabelTextView.visibility = View.VISIBLE
            saleDetailActivityCommentRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun handleSendCommentButtonClick() {
        if (saleDetailActivityCommentEditText.text.isNotEmpty()) {
            val sendSaleCommentRequest = SendSaleCommentRequest(saleDetailActivityCommentEditText.text.toString(), extraSaleDetail.id)
            saleDetailActivityComponent.providePresenter().sendComment(extraUser.token, extraSaleDetail.id, sendSaleCommentRequest)
            saleDetailActivityCommentEditText.hideKeyboard()
            saleDetailActivityCommentEditText.text.clear()
        } else {
            toast("Favor adicionar ao menos 1 caracter para enviar o comentário")
        }
    }
}