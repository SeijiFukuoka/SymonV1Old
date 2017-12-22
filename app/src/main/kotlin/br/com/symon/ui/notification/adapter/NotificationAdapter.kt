package br.com.symon.ui.notification.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.common.inflate
import br.com.symon.common.loadUrl
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.data.model.Notification
import kotlinx.android.synthetic.main.item_notification.view.*


class NotificationAdapter(private val notifications: MutableList<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(notifications[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(parent?.inflate(R.layout.item_notification))

    override fun getItemCount() = notifications.size

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        fun bind(notification: Notification) = with(itemView) {

            with(notification) {
                itemNotificationUserImageView.loadUrlToBeRounded(user.photoUri)
                itemNotificationUserNameTextView.text = user.name
                itemNotificationCreateAtTextView.text = formattedDate
                itemNotificationSaleImageView.loadUrl(salePhoto)

                when(typeId) {
                    Notification.NotificationTypeEnum.LIKE.value -> {
                        itemNotificationTypeImageView.setImageResource(R.drawable.ic_like_green_18dp)
                    }
                    Notification.NotificationTypeEnum.DISLIKE.value -> {
                        itemNotificationTypeImageView.setImageResource(R.drawable.ic_dislike_grey_18dp)
                    }
                    Notification.NotificationTypeEnum.COMMENT.value -> {
                        itemNotificationTypeImageView.setImageResource(R.drawable.ic_comments_outline_grey_18dp)
                    }
                }
            }
        }
    }
}