package br.com.symon.ui.saleDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ScrollView
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.hideKeyboard
import br.com.symon.common.loadUrl
import br.com.symon.common.toast
import br.com.symon.common.widget.EllipsizingTextView
import br.com.symon.data.model.Comment
import br.com.symon.data.model.Sale
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.model.responses.SendSaleCommentResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerSaleDetailActivityComponent
import br.com.symon.injection.components.SaleDetailActivityComponent
import br.com.symon.injection.modules.SaleDetailActivityModule
import kotlinx.android.synthetic.main.activity_sale_detail.*
import java.text.NumberFormat
import java.util.*


class SaleDetailActivity : BaseActivity(), SaleDetailContract.View, SaleCommentAdapter.OnItemClickListener {

    private lateinit var extraSaleDetail: Sale
    private lateinit var extraUser: UserTokenResponse

    private var mUserBlockedId: Int = 0
    private var mEmptyCommentList: Boolean = true

    companion object {
        private const val EXTRA_SALE = "EXTRA_SALE"
        private const val EXTRA_USER = "EXTRA_USER"

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

    override fun showComments(commentList: MutableList<Comment>) {
        if (commentList.isNotEmpty()) {
            setUpRecyclerView()
            saleCommentAdapter = SaleCommentAdapter(commentList, this)
            saleDetailActivityCommentRecyclerView.adapter = saleCommentAdapter

            saleDetailActivityCommentListLabelTextView.visibility = View.VISIBLE
            saleDetailActivityCommentRecyclerView.visibility = View.VISIBLE
            mEmptyCommentList = false
        } else {
            saleDetailActivityCommentListLayout.setPadding(0, resources.getDimensionPixelSize(R.dimen.margin_10_dp), 0, 0)
            saleDetailActivityCommentListLabelTextView.visibility = View.GONE
            saleDetailActivityCommentRecyclerView.visibility = View.GONE
            mEmptyCommentList = true
        }
    }

    override fun showFavoriteResponse() {
        if (extraSaleDetail.hasLiked)
            saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_on))
        else
            saleDetailActivityFavoriteImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sale_toolbar_favorite_off))
    }

    override fun showSendCommentResponse(sendSaleCommentResponse: SendSaleCommentResponse) {
        val commentSendResponse = Comment(sendSaleCommentResponse.id, sendSaleCommentResponse.message, "Agora", "Agora", sendSaleCommentResponse.saleId, sendSaleCommentResponse.userId)

        if (mEmptyCommentList)
            createAndUpdateCommentList(commentSendResponse)
        else
            saleCommentAdapter.addItem(commentSendResponse)

        scrollDown(false)
    }

    override fun showBlockUserResponse() {
        saleCommentAdapter.removeBlockUserComments(mUserBlockedId)
        if (saleCommentAdapter.itemCount == 0) {
            saleDetailActivityCommentListLabelTextView.visibility = View.GONE
            saleDetailActivityCommentRecyclerView.visibility = View.GONE
        }
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
            saleDetailActivityComponent.providePresenter().setFavorite(extraUser.token, extraSaleDetail.id)
        }

        saleDetailActivityCommentImageView.setOnClickListener {
            scrollDown(true)
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
        extraSaleDetail.id.let { saleDetailActivityComponent.providePresenter().getComments(extraUser.token, it) }
        saleDetailActivitySendCommentButton.setOnClickListener {
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

        saleCommentAdapter = SaleCommentAdapter(commentList, this)
        saleDetailActivityCommentRecyclerView.adapter = saleCommentAdapter

        saleDetailActivityCommentListLayout.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.margin_30_dp))
        saleDetailActivityCommentListLabelTextView.visibility = View.VISIBLE
        saleDetailActivityCommentRecyclerView.visibility = View.VISIBLE
        mEmptyCommentList = false
    }

    private fun scrollDown(requestFocus: Boolean) {
        saleDetailActivityScrollView.post({
            saleDetailActivityScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            if (requestFocus)
                saleDetailActivityCommentEditText.requestFocus()
        })
    }
}