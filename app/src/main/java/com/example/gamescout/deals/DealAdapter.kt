package com.example.gamescout.deals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamescout.R
import com.example.gamescout.item_data.GameItem

class DealAdapter(private var games: List<GameItem>) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return DealViewHolder(view)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        holder.bind(games[position])
    }

    fun updateData(newGames: List<GameItem>) {
        games = newGames
        notifyDataSetChanged()
    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val thumbImageView: ImageView = itemView.findViewById(R.id.thumb)

        fun bind(game: GameItem) {
            titleTextView.text = game.title
            Glide.with(itemView.context)
                .load(game.thumb)
                .into(thumbImageView)
        }
    }
}
