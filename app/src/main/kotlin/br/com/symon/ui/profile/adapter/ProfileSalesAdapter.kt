package br.com.symon.ui.profile.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.common.inflate
import br.com.symon.common.loadUrl
import br.com.symon.data.model.Sale
import kotlinx.android.synthetic.main.item_user_sale.view.*
import java.text.NumberFormat
import java.util.*


class ProfileSalesAdapter(private val onClick: (Sale) -> Unit) : RecyclerView.Adapter<ProfileSalesAdapter.ViewHolder>() {

    private val saleList = mutableListOf<Sale>()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(saleList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(parent?.inflate(R.layout.item_user_sale), onClick)

    override fun getItemCount() = saleList.size

    fun setList(mutableList: MutableList<Sale>) {
        this.saleList.clear()
        this.saleList.addAll(mutableList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?, private val onClick: (Sale) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(sale: Sale) = with(itemView) {
            itemProfileSaleImageView.loadUrl(sale.photo)
            itemProfileSaleTitleTextView.text = sale.message
            itemProfileSaleValueTextView.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(sale.price)
            itemProfileSaleLikeQuantityTextView.text = sale.likes.toString()
            itemProfileSaleDislikeQuantityTextView.text = sale.dislikes.toString()
            itemProfileSaleCommentQuantityTextView.text = "0"

            itemView.setOnClickListener { onClick(sale) }
        }
    }
}