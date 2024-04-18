package com.example.bitfit


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamescout.R
import com.example.gamescout.deals.FavoriteAdapter
import com.example.gamescout.item_data.GameApplication
import com.example.gamescout.item_data.GameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {


    private val favGames = mutableListOf<GameItem>()
    private lateinit var favGameAdapter: RecyclerView
    private lateinit var gameDisplay : FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favGameAdapter = view.findViewById(R.id.favoriteRV)
        gameDisplay = FavoriteAdapter(requireContext(), favGames)
        favGameAdapter.adapter = gameDisplay

        favGameAdapter.layoutManager = LinearLayoutManager(requireContext()).also {
            val dividerItemDecoration = DividerItemDecoration(requireContext(), it.orientation)
            favGameAdapter.addItemDecoration(dividerItemDecoration)
        }
        return view
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            (requireActivity().application as GameApplication).db.gameDao().getAll()
                .collect { databaseList ->
                    favGames.clear()
                    databaseList.map { entity ->
                        GameItem(
                            entity.title,
                            entity.description,
                            entity.salePrice,
                            entity.normalPrice,
                            entity.thumb,
                            entity.steamAppID,
                            entity.storeID

                            )
                    }.also { mappedList ->
                        favGames.addAll(mappedList)
                        launch(Dispatchers.Main) {
                            Log.d("test", "update")

                            gameDisplay.notifyDataSetChanged()

                        }
                    }


                }
        }

    }
}