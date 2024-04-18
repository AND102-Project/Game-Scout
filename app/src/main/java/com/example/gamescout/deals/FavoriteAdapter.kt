package com.example.gamescout.deals

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamescout.R
import com.example.gamescout.item_data.GameItem

class FavoriteAdapter(private val context: Context, private val favGameList: List<GameItem>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        val favGame = favGameList[position]
        holder.bind(favGame)
    }

    override fun getItemCount() = favGameList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        private val favoriteTitleTv = itemView.findViewById<TextView>(R.id.favTitleTv)
        private val favoritePriceTv = itemView.findViewById<TextView>(R.id.favPriceTv)
        private val thumbImageView = itemView.findViewById<TextView>(R.id.favThumbIv)

        init {
            itemView.setOnClickListener(this)
        }

//        Start the GameDetails activity and pass the selected game

        override fun onClick(v: View?) {

            val favGame = favGameList[adapterPosition]

//            val intent = Intent(context, GameDetails::class.java)
//            intent.putExtra("GAME_EXTRA", favGame)
//            context.startActivity(intent)

        }

        fun bind(favGameList: GameItem) {
            favoriteTitleTv.text = favGameList.title
            favoritePriceTv.text = favGameList.salePrice
        }
    }

}