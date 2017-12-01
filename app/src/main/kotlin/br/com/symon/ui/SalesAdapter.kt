package br.com.symon.ui

import android.annotation.SuppressLint
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.common.inflate
import br.com.symon.common.loadUrl
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.data.model.Sale
import br.com.symon.data.model.User
import kotlinx.android.synthetic.main.item_sale.view.*
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
            if (saleToBeChanged.hasLiked!!) {
                saleToBeChanged.likes = saleToBeChanged.likes?.minus(1)
                saleToBeChanged.hasLiked = false
            } else {
                saleToBeChanged.likes = saleToBeChanged.likes?.plus(1)
                saleToBeChanged.hasLiked = true

                if (saleToBeChanged.hasDisliked!!) {
                    saleToBeChanged.dislikes = saleToBeChanged.dislikes?.minus(1)
                    saleToBeChanged.hasDisliked = false
                }
            }
        } else {
            if (saleToBeChanged.hasDisliked!!) {
                saleToBeChanged.dislikes = saleToBeChanged.dislikes?.minus(1)
                saleToBeChanged.hasDisliked = false
            } else {
                saleToBeChanged.dislikes = saleToBeChanged.dislikes?.plus(1)
                saleToBeChanged.hasDisliked = true

                if (saleToBeChanged.hasLiked!!) {
                    saleToBeChanged.likes = saleToBeChanged.likes?.minus(1)
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

                //                TODO("Aguardando resposta na API")
//                if (isSponsored!!) {
//                    itemSaleCardLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_item_sale_label_sponsored, null)
//                    var padding = dpToPixels(resources.getDimension(R.dimen.margin_3_dp))
//                    itemSaleCardLayout.setPadding(padding, padding, padding, padding)
//                    itemSaleSponsoredLabelTextView.visibility = View.VISIBLE
//                } else {
                itemSaleCardLayout.background = ResourcesCompat.getDrawable(resources, R.color.orange_F5A623, null)
                itemSaleCardLayout.setPadding(0, 0, 0, 0)
                itemSaleSponsoredLabelTextView.visibility = View.GONE
//                }

                with(itemSaleImageView) {
                    sale.photo?.let { loadUrl(it) }
                }

                itemSaleSaleTitleTextView.text = sale.message
                itemSaleSaleValueTextView.text = String.format(Locale.getDefault(), resources.getString(R.string.item_sale_price_formatted), sale.price)

                itemSaleSaleTimeTextView.text = sale.updatedAt
                itemSaleLikeQuantityTextView.text = sale.likes.toString()
                itemSaleDislikeQuantityTextView.text = sale.dislikes.toString()

                hasLiked?.let {
                    itemSaleLikeLayout.isSelected = hasLiked as Boolean
                    itemSaleLikeImageView.isSelected = hasLiked as Boolean
                    itemSaleLikeQuantityTextView.isSelected = hasLiked as Boolean
                }

                hasDisliked?.let {
                    itemSaleDislikeLayout.isSelected = hasDisliked as Boolean
                    itemSaleDislikeImageView.isSelected = hasDisliked as Boolean
                    itemSaleDislikeQuantityTextView.isSelected = hasDisliked as Boolean
                }

                itemSaleImageView.setOnClickListener { listener.onSaleImageClick(sale) }
                itemSaleLikeLayout.setOnClickListener { listener.onLikeSaleClick(position, sale) }
                itemSaleDislikeLayout.setOnClickListener { listener.onDislikeSaleClick(position, sale) }
                itemSaleUserOptionsReportSaleTextView.setOnClickListener {
                    itemSaleUserOptionsCardView.visibility = View.GONE
                    listener.onReportSaleClick(sale)
                }
            }

            with(sale.user)
            {
                with(itemSaleUserPhotoImageView) {
                    photo?.let { loadUrlToBeRounded(it) }
                }

                itemSaleUserNameTextView.text = sale.user.name
                itemSaleUserOptionsImageView.setOnClickListener { itemSaleUserOptionsCardView.visibility = View.VISIBLE }
                itemSaleUserOptionsBlockUserTextView.setOnClickListener {
                    itemSaleUserOptionsCardView.visibility = View.GONE
                    listener.onBlockUserClick(sale.user)
                }
            }

            if (currentUser.id == sale.user.id) {
                itemSaleUserOptionsBlockUserTextView.visibility = View.GONE
            }
        }
    }
}