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
                   private val listener: OnItemClickListener)
    : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_sale), listener)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onSaleImageClick(sale: Sale)
        fun onLikeSaleClick(position: Int, sale: Sale)
        fun onDislikeSaleClick(position: Int, sale: Sale)
        fun onOptionsSaleClick(user: User)
    }

    fun addList(list: MutableList<Sale>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, isLike: Boolean, isAdd: Boolean) {
        val saleToBeChanged = list[position]

        if (isLike) {
            if (isAdd) {
                saleToBeChanged.likes!! + 1
            } else {
                saleToBeChanged.likes!! - 1
                if (saleToBeChanged.likes!! < 0)
                    saleToBeChanged.likes = 0
            }
            saleToBeChanged.hasLiked = isAdd
        } else {
            if (isAdd) {
                saleToBeChanged.dislikes!! + 1
            } else {
                saleToBeChanged.dislikes!! - 1
                if (saleToBeChanged.dislikes!! < 0)
                    saleToBeChanged.dislikes = 0
            }
            saleToBeChanged.hasDisliked = isAdd
        }
        list[position] = saleToBeChanged
        notifyItemChanged(position)
    }

    class ViewHolder(itemView: View?,
                     private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("NewApi")
        fun bind(sale: Sale, position: Int) = with(itemView) {
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

                itemSaleLikeLayout.isSelected = hasLiked!!
                itemSaleLikeImageView.isSelected = hasLiked!!
                itemSaleLikeQuantityTextView.isSelected = hasLiked!!

                itemSaleDislikeLayout.isSelected = hasDisliked!!
                itemSaleDislikeImageView.isSelected = hasDisliked!!
                itemSaleDislikeQuantityTextView.isSelected = hasDisliked!!

                itemSaleImageView.setOnClickListener { listener.onSaleImageClick(sale) }
                itemSaleLikeLayout.setOnClickListener { listener.onLikeSaleClick(position, sale) }
                itemSaleDislikeLayout.setOnClickListener { listener.onDislikeSaleClick(position, sale) }
            }

            with(sale.user) {
                with(itemSaleUserPhotoImageView) {
                    photo?.let { loadUrlToBeRounded(it) }
                }

                itemSaleUserNameTextView.text = sale.user.name
                itemSaleUserOptionsImageView.setOnClickListener { listener.onOptionsSaleClick(sale.user) }
            }
        }
    }
}