package br.com.symon.ui.saleDetail

import android.annotation.SuppressLint
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.common.inflate
import br.com.symon.data.model.Comment
import kotlinx.android.synthetic.main.item_sale_comment.view.*
import kotlinx.android.synthetic.main.view_item_author_bottom_layout.view.*

class SaleCommentAdapter(private val list: MutableList<Comment>,
                         private val currentUserId: Int,
                         private val listener: OnItemClickListener)
    : RecyclerView.Adapter<SaleCommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_sale_comment), listener)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(list[position], position, currentUserId)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onBlockUserClick(userId: Int)
        fun onDeleteCommentClick(commentId: Int, position: Int)
    }

    fun addItem(comment: Comment) {
        this.list.add(comment)
        notifyItemInserted(list.size - 1)
    }

    fun remove(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeBlockUserComments(blockedUserId: Int) {
        val filteredList: List<Comment> = list.filter {
            it.user.id != blockedUserId
        }
        this.list.clear()
        this.list.addAll(filteredList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?,
                     private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("NewApi")
        fun bind(comment: Comment, position: Int, currentUserId: Int) = with(itemView) {
            with(comment) {
                itemSaleCommentTextView.text = message

//                TODO("Aguardando API - Usuário")
//                with(viewItemAuthorBottomUserPhotoImageView)
                viewItemAuthorBottomUserNameTextView.text = user.name
                viewItemAuthorBottomSaleTimeTextView.text = formattedDate
            }

            viewItemAuthorBottomUserOptionsImageView.setOnClickListener {
                val popup = PopupMenu(context, viewItemAuthorBottomUserOptionsImageView, Gravity.TOP)
                popup.inflate(R.menu.sale_comment_menu)

                if (comment.user.id == currentUserId) {
                    popup.menu.getItem(0).isVisible = false
                } else {
                    popup.menu.getItem(1).isVisible = false
                }

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.saleCommentMenuBlockUser -> comment.user.id?.let { it1 -> listener.onBlockUserClick(it1) }
                        R.id.saleCommentMenuDeleteComment -> listener.onDeleteCommentClick(comment.id, adapterPosition)
                    }
                    false
                }
                popup.show()
            }
        }
    }
}