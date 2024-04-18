package com.example.gamescout.deals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamescout.R
import com.example.gamescout.item_data.Dealtem

class DealAdapter(private var deals: List<Dealtem>, private var storeMap: Map<String, String>) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return DealViewHolder(view)
    }

    override fun getItemCount(): Int = deals.size

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals[position]
        val storeName = storeMap[deal.storeID] ?: "Unknown Store"
        holder.bind(deal, storeName)
    }

    fun updateData(newDeals: List<Dealtem>) {
        deals = newDeals
        notifyDataSetChanged()
    }
    fun updateStoreMap(newStoreMap: Map<String, String>) {
        storeMap = newStoreMap
        notifyDataSetChanged()  // Refresh items to display updated store names
    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val thumbImageView: ImageView = itemView.findViewById(R.id.thumb)
        private val bestPrice: TextView = itemView.findViewById(R.id.sale_price)
        private val store: TextView = itemView.findViewById(R.id.store)

        fun bind(game: Dealtem, storeName: String) {
            titleTextView.text = game.title
            bestPrice.text = game.salePrice
            store.text = storeName
            Glide.with(itemView.context)
                .load(game.thumb)
                .into(thumbImageView)
        }
    }
}
