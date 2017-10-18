package br.com.symon.features.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.symon.commons.adapter.ViewType
import br.com.symon.commons.adapter.ViewTypeDelegateAdapter
import br.com.symon.R
import br.com.symon.commons.extensions.inflate

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.news_item_loading))
}