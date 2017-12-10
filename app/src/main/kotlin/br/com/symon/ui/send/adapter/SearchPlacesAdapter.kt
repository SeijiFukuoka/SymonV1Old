package br.com.symon.ui.send.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.common.inflate
import br.com.symon.data.model.PlaceInfo
import kotlinx.android.synthetic.main.item_search_place.view.*


class SearchPlacesAdapter(private val onItemClick: (PlaceInfo) -> Unit) : RecyclerView.Adapter<SearchPlacesAdapter.ViewHolder>() {

    private val addressList: MutableList<PlaceInfo> = arrayListOf()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(addressList[position], onItemClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(parent?.inflate(R.layout.item_search_place))

    override fun getItemCount(): Int = addressList.size

    fun updateAddressList(newAddressList: List<PlaceInfo>) {
        addressList.clear()
        addressList.addAll(newAddressList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(address: PlaceInfo, onItemClick: (PlaceInfo) -> Unit) = with(itemView) {

            with(itemView){
                itemSearchPlaceContainerConstraintLayout.setOnClickListener {
                    onItemClick(address)
                }
                itemSearchPlaceNameTextView.text = address.name
                itemSearchPlaceAddressTextView.text = address.address
            }
        }
    }
}