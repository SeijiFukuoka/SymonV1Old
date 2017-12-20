package br.com.symon.ui.sales

import android.annotation.SuppressLint
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.common.dpToPixels
import br.com.symon.common.inflate
import br.com.symon.common.loadUrl
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.data.model.Sale
import br.com.symon.data.model.User
import kotlinx.android.synthetic.main.item_sale.view.*
import kotlinx.android.synthetic.main.view_item_author_bottom_layout.view.*
import java.text.NumberFormat
import java.util.*


class SalesAdapter(private val list: MutableList<Sale>,
                   private val currentUser: User,
                   private val listener: OnItemClickListener)
    : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_sale), listener)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(list[position], position, currentUser)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onSaleImageClick(sale: Sale)
        fun onLikeSaleClick(position: Int, sale: Sale)
        fun onDislikeSaleClick(position: Int, sale: Sale)
        fun onReportSaleClick(sale: Sale)
        fun onBlockUserClick(user: User)
    }

    fun addList(list: MutableList<Sale>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, isLike: Boolean) {
        val saleToBeChanged = list[position]

        if (isLike) {
            if (saleToBeChanged.hasLiked) {
                saleToBeChanged.likes = saleToBeChanged.likes.minus(1)
                saleToBeChanged.hasLiked = false
            } else {
                saleToBeChanged.likes = saleToBeChanged.likes.plus(1)
                saleToBeChanged.hasLiked = true

                if (saleToBeChanged.hasDisliked) {
                    saleToBeChanged.dislikes = saleToBeChanged.dislikes.minus(1)
                    saleToBeChanged.hasDisliked = false
                }
            }
        } else {
            if (saleToBeChanged.hasDisliked) {
                saleToBeChanged.dislikes = saleToBeChanged.dislikes.minus(1)
                saleToBeChanged.hasDisliked = false
            } else {
                saleToBeChanged.dislikes = saleToBeChanged.dislikes.plus(1)
                saleToBeChanged.hasDisliked = true

                if (saleToBeChanged.hasLiked) {
                    saleToBeChanged.likes = saleToBeChanged.likes.minus(1)
                    saleToBeChanged.hasLiked = false
                }
            }
        }

        list[position] = saleToBeChanged
        notifyItemChanged(position)
    }

    class ViewHolder(itemView: View?,
                     private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("NewApi")
        fun bind(sale: Sale, position: Int, currentUser: User) = with(itemView) {
            with(sale) {

                if (currentUser.id == sale.user.id) {
                    itemSaleCardLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_item_sale_label_mine, null)
                    itemSaleMineSponsoredLabelTextView.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_item_sale_label_mine, null)
                    itemSaleMineSponsoredLabelTextView.setTextColor(ResourcesCompat.getColor(resources, R.color.gray_737373, null))
                    val padding = dpToPixels(resources.getDimension(R.dimen.margin_2_dp))
                    itemSaleCardLayout.setPadding(padding, padding, padding, padding)
                    itemSaleMineSponsoredLabelTextView.text = resources.getString(R.string.item_sale_mine_label)
                    itemSaleMineSponsoredLabelTextView.visibility = View.VISIBLE
                } else {
                    itemSaleCardLayout.background = ResourcesCompat.getDrawable(resources, R.color.orange_F5A623, null)
                    itemSaleCardLayout.setPadding(0, 0, 0, 0)
                    itemSaleMineSponsoredLabelTextView.visibility = View.GONE
                }
                //                TODO("Aguardando resposta na API")
//                if (isSponsored!!) {
//                    itemSaleCardLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_item_sale_label_sponsored, null)
//                    var padding = dpToPixels(resources.getDimension(R.dimen.margin_3_dp))
//                    itemSaleCardLayout.setPadding(padding, padding, padding, padding)
//                    itemSaleSponsoredLabelTextView.visibility = View.VISIBLE
//                }

                with(itemSaleImageView) {
                    sale.photo?.let { loadUrl(it) }
                }

                itemSaleSaleTitleTextView.text = sale.message
                itemSaleSaleValueTextView.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(sale.price)

                viewItemAuthorBottomSaleTimeTextView.text = sale.updatedAt
                itemSaleLikeQuantityTextView.text = sale.likes.toString()
                itemSaleDislikeQuantityTextView.text = sale.dislikes.toString()

                hasLiked.let {
                    itemSaleLikeLayout.isSelected = hasLiked
                    itemSaleLikeImageView.isSelected = hasLiked
                    itemSaleLikeQuantityTextView.isSelected = hasLiked
                }

                hasDisliked.let {
                    itemSaleDislikeLayout.isSelected = hasDisliked
                    itemSaleDislikeImageView.isSelected = hasDisliked
                    itemSaleDislikeQuantityTextView.isSelected = hasDisliked
                }

                itemSaleImageView.setOnClickListener { listener.onSaleImageClick(sale) }
                itemSaleLikeLayout.setOnClickListener { listener.onLikeSaleClick(position, sale) }
                itemSaleDislikeLayout.setOnClickListener { listener.onDislikeSaleClick(position, sale) }
            }

            with(sale.user)
            {
                with(viewItemAuthorBottomUserPhotoImageView) {
                    photoUri?.let { loadUrlToBeRounded(it) }
                }

                viewItemAuthorBottomUserNameTextView.text = sale.user.name
                viewItemAuthorBottomUserOptionsImageView.setOnClickListener {
                    val popup = PopupMenu(context, viewItemAuthorBottomUserOptionsImageView, Gravity.TOP)
                    popup.inflate(R.menu.user_options_menu)

                    if (currentUser.id == sale.user.id) {
                        popup.menu.getItem(1).isVisible = false
                    }

                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.userOptionsMenuReportSale -> {
                                listener.onReportSaleClick(sale)
                            }
                            R.id.userOptionsMenuBlockUser -> {
                                listener.onBlockUserClick(sale.user)
                            }
                        }
                        false
                    }
                    popup.show()
                }
            }
        }
    }
}