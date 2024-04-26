package com.example.gamescout.deals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamescout.R
import com.example.gamescout.cheapsharkAPI.RetrofitClient
import com.example.gamescout.database.GameDao
import com.example.gamescout.database.GameEntity
import com.example.gamescout.firebase.AuthManager
import com.example.gamescout.item_data.DealItem
import com.example.gamescout.item_data.GameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

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


        private val favoriteButton = itemView.findViewById<Button>(R.id.fav_button)
        private val auth = AuthManager()
        private val email = auth.getCurrentUser()?.email
        fun bind(deal: DealItem, storeName: String?) {
            titleTextView.text = deal.title
            bestPrice.text = deal.salePrice
            store.text = storeName ?: "Unknown Store"
            Glide.with(itemView.context).load(deal.thumb).into(thumbImageView)
            val gameItem = GameItem(
                deal.gameID,
                deal.steamAppID,
                deal.salePrice,
                deal.dealID,
                deal.title,
                deal.internalName,
                deal.thumb
            )

            favoriteButton.setOnClickListener {
                insertGameIntoDatabase(gameItem)
                setPriceAlert(gameItem.gameID.toString(), deal.normalPrice.toString())
            }

            itemView.setOnClickListener {
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
                // Check if the game already exists in the database
                val existingGame = gameEntity.gameID?.let { gameDao.getGameById(it) }
                if (existingGame != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(itemView.context, "Game is already in favorites", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    gameDao.insert(gameEntity)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(itemView.context, "Game added to favorites", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        private fun setPriceAlert(gameID: String, price:
        String) {
            if (email != null) {
                RetrofitClient.instance.setPriceAlert(email, gameID,
                    price).enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response:
                    Response<Boolean>
                    ) {
                        if (response.isSuccessful && response.body() == true) {
                            Toast.makeText(itemView.context, "Alert set successfully",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(itemView.context, "Failed to set alert",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Toast.makeText(itemView.context, "Error: ${t.localizedMessage}",
                            Toast.LENGTH_SHORT).show()
                    }
                })
            }else {
                Timber.d("Email is null!")
            }
        }
    }

}

