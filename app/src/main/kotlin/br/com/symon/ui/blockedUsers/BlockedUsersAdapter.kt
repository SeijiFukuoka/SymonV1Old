package br.com.symon.ui.blockedUsers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.common.inflate
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.data.model.BlockedUser
import kotlinx.android.synthetic.main.item_blocked_user.view.*

class BlockedUsersAdapter(private val list: MutableList<BlockedUser>,
                          private val listener: OnItemClickListener)
    : RecyclerView.Adapter<BlockedUsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_blocked_user), listener)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onUnblockUserClick(blockedUser: BlockedUser)
    }

    fun remove(blockedUser: BlockedUser) {
        val position = list.indexOf(blockedUser)
        this.list.removeAt(position)
        this.notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View?,
                     private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(blockedUser: BlockedUser) = with(itemView) {
            blockedUser.user.apply {
                itemBlockedUserImageView.loadUrlToBeRounded(photoUri)
                itemBlockedUserNameTextView.text = name
            }

            blockedUser.apply {
                itemBlockedUserTimeTextView.text = formattedDate
                itemBlockedUserUnblockTextView.setOnClickListener {
                    listener.onUnblockUserClick(blockedUser)
                    itemBlockedUserLayout.isPressed = true
                }
                itemBlockedUserUnblockImageView.setOnClickListener {
                    listener.onUnblockUserClick(blockedUser)
                    itemBlockedUserLayout.isPressed = true
                }
            }
        }
    }
}