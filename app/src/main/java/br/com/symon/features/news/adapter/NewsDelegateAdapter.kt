package br.com.symon.features.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.commons.RedditNewsItem
import br.com.symon.commons.adapter.ViewType
import br.com.symon.commons.adapter.ViewTypeDelegateAdapter
import br.com.symon.commons.extensions.getFriendlyTime
import br.com.symon.commons.extensions.inflate
import br.com.symon.commons.extensions.loadImg
import kotlinx.android.synthetic.main.news_item.view.*

class NewsDelegateAdapter(val viewActions: onViewSelectedListener) : ViewTypeDelegateAdapter {

    interface onViewSelectedListener {
        fun onItemSelected(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NewsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as NewsViewHolder
        holder.bind(item as RedditNewsItem)
    }

    inner class NewsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.news_item)) {

        private val imgThumbnail = itemView.img_thumbnail
        private val description = itemView.description
        private val author = itemView.author
        private val comments = itemView.comments
        private val time = itemView.time

        fun bind(item: RedditNewsItem) {
            imgThumbnail.loadImg(item.thumbnail)
            description.text = item.title
            author.text = item.author
            comments.text = "${item.numComments} comments"
            time.text = item.created.getFriendlyTime()

            super.itemView.setOnClickListener { viewActions.onItemSelected(item.url)}
        }
    }
}