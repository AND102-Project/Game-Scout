package com.example.gamescout.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamescout.R
import com.example.gamescout.item_data.GameItem
import timber.log.Timber

class FavoriteAdapter(
    private val context: Context,
    private val favGameList: List<GameItem>,
    private val onItemClick: (GameItem) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favGame = favGameList[position]
        holder.bind(favGame)
        holder.itemView.setOnClickListener {
            onItemClick(favGame)
        }
    }

    override fun getItemCount() = favGameList.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val favoriteTitleTv = itemView.findViewById<TextView>(R.id.favTitleTv)
        private val favoritePriceTv = itemView.findViewById<TextView>(R.id.favPriceTv)
        private val thumbIv: ImageView = itemView.findViewById(R.id.favThumbIv)

        fun bind(favGame: GameItem) {
            Timber.d("Binding game: ${favGame.internalName}")
            favoriteTitleTv.text = favGame.external
            favoritePriceTv.text = favGame.cheapest
            Glide.with(itemView)
                .load(favGame.thumb)
                .into(thumbIv)
        }
    }
}

