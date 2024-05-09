package com.example.gamescout.top_rated

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
import com.example.gamescout.database.GameDao
import com.example.gamescout.database.GameEntity
import com.example.gamescout.item_data.DealItem
import com.example.gamescout.item_data.GameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class TopRatedAdapter(
    private var games: List<DealItem>,
    private var storeMap: Map<String, String>,
    private val onItemClicked: (GameItem) -> Unit,
    private val gameDao: GameDao,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<TopRatedAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_item, parent, false)
        return GameViewHolder(view, onItemClicked, gameDao, lifecycleScope)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        val storeName = storeMap[game.storeID]

        holder.bind(game, storeName)
    }

    class GameViewHolder(
        view: View,
        private val onItemClicked: (GameItem) -> Unit,
        private val gameDao: GameDao,
        private val lifecycleScope: LifecycleCoroutineScope
    ) : RecyclerView.ViewHolder(view){
        private val gameTitle: TextView = view.findViewById(R.id.title)
        private val gamePrice: TextView = view.findViewById(R.id.sale_price)
        private val gameStore: TextView = view.findViewById(R.id.store)
        private val gameImage: ImageView = view.findViewById(R.id.thumb)

        val favoriteButton = itemView.findViewById<Button>(R.id.fav_button)

        fun bind(game: DealItem, storeName: String?) {
            gameTitle.text = game.title
            gamePrice.text = game.salePrice
            gameStore.text = storeName ?: "Unknown Store"

            Glide.with(itemView)
                .load(game.thumb)
                .into(gameImage)

            itemView.setOnClickListener {
                val gameItem = GameItem(
                    game.gameID,
                    game.steamAppID,
                    game.salePrice,
                    game.dealID,
                    game.title,
                    game.internalName,
                    game.thumb
                )
                onItemClicked(gameItem)
            }

            favoriteButton.setOnClickListener {
                val gameItem = GameItem(
                    game.gameID,
                    game.steamAppID,
                    game.salePrice,
                    game.dealID,
                    game.title,
                    game.internalName,
                    game.thumb
                )
                insertGameIntoDatabase(gameItem)
            }

        }

            private fun insertGameIntoDatabase(gameItem: GameItem) {
                val gameEntity = GameEntity(
                    gameID = gameItem.gameID,
                    steamAppID = gameItem.steamAppID,
                    cheapest = gameItem.cheapest,
                    cheapestDealID = gameItem.cheapestDealID,
                    external = gameItem.external,
                    internalName = gameItem.internalName,
                    thumb = gameItem.thumb

                )

                Timber.i("Inserting into database: Title - ${gameItem.external}")

                lifecycleScope.launch(Dispatchers.IO) {
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


    }

    fun updateGames(newGames: List<DealItem>) {
        games = newGames
        notifyDataSetChanged()
    }
}