package com.example.gamescout.game_detail

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gamescout.database.GameApplication
import com.example.gamescout.database.GameEntity
import com.example.gamescout.databinding.FragmentGameDetailBinding
import com.example.gamescout.item_data.GameItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
    private val gameDao by lazy { (requireActivity().application as GameApplication).db.gameDao() }

    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        val args = GameDetailFragmentArgs.fromBundle(requireArguments())
        args.gameItem?.let { displayGameDetails(it) }
        return binding.root
    }

    private fun addGameToFavorites(gameItem: GameItem) {
        val gameEntity = GameEntity(
            gameID = gameItem.gameID,
            steamAppID = gameItem.steamAppID,
            cheapest = gameItem.cheapest,
            cheapestDealID = gameItem.cheapestDealID,
            external = gameItem.external,
            internalName = gameItem.internalName,
            thumb = gameItem.thumb
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val existingGames = gameDao.getAll()
            existingGames.collect { games ->
                val existingGame = games.firstOrNull { it.gameID == gameEntity.gameID }
                if (existingGame == null) {
                    gameDao.insert(gameEntity)
                    withContext(Dispatchers.Main) {
                        binding.buttonAddToFavorites.text = "Favorited"
                    }
                }
            }
        }
    }

    private fun displayGameDetails(gameItem: GameItem) {
        Glide.with(this)
            .load(gameItem.thumb)
            .into(binding.imageGameThumb)
        binding.textGameTitle.text = gameItem.internalName
        binding.textGamePrice.text = gameItem.cheapest
        binding.buttonBuyNow.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.cheapshark.com/redirect?dealID=${gameItem.cheapestDealID}")
            )
            startActivity(intent)
        }


        lifecycleScope.launch(Dispatchers.IO) {
            val existingGames = gameDao.getAll()
            existingGames.collect { games ->
                val existingGame = games.firstOrNull { it.gameID == gameItem.gameID }
                if (existingGame != null) {
                    withContext(Dispatchers.Main) {
                        binding.buttonAddToFavorites.text = "Favorited"
                        binding.fullHeart.visibility = View.VISIBLE

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.fullHeart.visibility = View.INVISIBLE
                        binding.fullHeart.bringToFront()
                    }
                }
            }
        }

        binding.buttonAddToFavorites.setOnClickListener {
            addGameToFavorites(gameItem)
        }
        binding.buttonTrackPrice.setOnClickListener {
            trackGamePrice(gameItem)
        }
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun trackGamePrice(gameItem: GameItem) {
    //TODO
        // Here I don’t know how can we track Game Price change and change link to a cheaper vendor and send a push notification
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}