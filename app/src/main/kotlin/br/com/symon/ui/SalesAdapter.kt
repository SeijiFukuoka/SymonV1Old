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
                   private val listener: onItemClickListener)
    : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_sale), listener)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface onItemClickListener {
        fun onSaleImageClick(sale: Sale)
        fun onLikeSaleClick(sale: Sale)
        fun onDislikeSaleClick(sale: Sale)
        fun onOptionsSaleClick(user: User)
    }

    fun addList(list: MutableList<Sale>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?,
                     private val listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

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
                itemSaleLikeQuantityTextView.text = sale.likes
                itemSaleDislikeQuantityTextView.text = sale.dislikes

//                TODO("Aguardando comportamento da API")
//                itemSaleLikeLayout.isSelected = isLiked!!
//                itemSaleLikeImageView.isSelected = isLiked!!
//                itemSaleLikeQuantityTextView.isSelected = isLiked!!
//
//                itemSaleDislikeLayout.isSelected = isDisliked!!
//                itemSaleDislikeImageView.isSelected = isDisliked!!
//                itemSaleDislikeQuantityTextView.isSelected = isDisliked!!

                itemSaleImageView.setOnClickListener { listener.onSaleImageClick(sale) }
                itemSaleLikeLayout.setOnClickListener { listener.onLikeSaleClick(sale) }
                itemSaleDislikeLayout.setOnClickListener { listener.onDislikeSaleClick(sale) }
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