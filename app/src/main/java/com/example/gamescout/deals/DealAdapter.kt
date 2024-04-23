package com.example.gamescout.deals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamescout.R
import com.example.gamescout.database.GameDao
import com.example.gamescout.database.GameEntity
import com.example.gamescout.item_data.DealItem
import com.example.gamescout.item_data.GameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DealAdapter(
    private var deals: List<DealItem>,
    private var storeMap: Map<String, String>,
    private val onItemClicked: (GameItem) -> Unit,
    private val gameDao: GameDao,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_item, parent, false)
        return DealViewHolder(view, onItemClicked, gameDao,lifecycleScope)
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
        notifyDataSetChanged()  
    }

    class DealViewHolder(
        itemView: View,
        private val onItemClicked: (GameItem) -> Unit,
        private val gameDao: GameDao,
        private val lifecycleScope: LifecycleCoroutineScope
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val thumbImageView: ImageView = itemView.findViewById(R.id.thumb)
        private val bestPrice: TextView = itemView.findViewById(R.id.sale_price)
        private val store: TextView = itemView.findViewById(R.id.store)

        val favoriteButton = itemView.findViewById<Button>(R.id.fav_button)
        fun bind(deal: DealItem, storeName: String?) {
            titleTextView.text = deal.title
            bestPrice.text = deal.salePrice
            store.text = storeName ?: "Unknown Store"
            Glide.with(itemView.context).load(deal.thumb).into(thumbImageView)

            favoriteButton.setOnClickListener {
                // Insert game into favorite database
                val dealItem = GameItem(
                    deal.gameID,
                    deal.steamAppID,
                    deal.salePrice,
                    deal.dealID,
                    deal.title,
                    deal.internalName,
                    deal.thumb
                )
                insertGameIntoDatabase(dealItem)
            }

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
        private fun insertGameIntoDatabase(dealItem: GameItem) {
            // Convert DealItem to a database entity
            val gameEntity = GameEntity(
                gameID = dealItem.gameID,
                steamAppID = dealItem.steamAppID,
                cheapest = dealItem.cheapest,
                cheapestDealID = dealItem.cheapestDealID,
                external = dealItem.external,
                internalName = dealItem.internalName,
                thumb = dealItem.thumb
            )
            lifecycleScope.launch(Dispatchers.IO) {
                gameDao.insert(gameEntity)
            }
        }
    }
}
