package com.example.gamescout.deals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamescout.R
import com.example.gamescout.item_data.DealItem
import com.example.gamescout.item_data.GameItem

class DealAdapter(
    private var deals: List<DealItem>,
    private var storeMap: Map<String, String>,
    private val onItemClicked: (GameItem) -> Unit
) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_item, parent, false)
        return DealViewHolder(view, onItemClicked)
    }

    override fun getItemCount(): Int = deals.size

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals[position]
        storeMap[deal.storeID]?.let { holder.bind(deal, it) }
    }

    fun updateData(newDeals: List<DealItem>) {
        deals = newDeals
        notifyDataSetChanged()
    }
    fun updateStoreMap(newStoreMap: Map<String, String>) {
        storeMap = newStoreMap
        notifyDataSetChanged()  // Refresh items to display updated store names
    }

    class DealViewHolder(itemView: View, private val onItemClicked: (GameItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val thumbImageView: ImageView = itemView.findViewById(R.id.thumb)
        private val bestPrice: TextView = itemView.findViewById(R.id.sale_price)
        private val store: TextView = itemView.findViewById(R.id.store)

        fun bind(deal: DealItem, storeName: String?) {
            titleTextView.text = deal.title
            bestPrice.text = deal.salePrice
            store.text = storeName ?: "Unknown Store"
            Glide.with(itemView.context).load(deal.thumb).into(thumbImageView)

            itemView.setOnClickListener {
                val gameItem = GameItem(
                    deal.gameID,
                    deal.steamAppID,
                    deal.salePrice,
                    deal.dealID,
                    deal.title,
                    deal.internalName,
                    deal.thumb
                )
                onItemClicked(gameItem)
            }
        }
    }
}
