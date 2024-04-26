package com.example.gamescout.game_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gamescout.cheapsharkAPI.RetrofitClient
import com.example.gamescout.database.GameApplication
import com.example.gamescout.database.GameEntity
import com.example.gamescout.databinding.FragmentGameDetailBinding
import com.example.gamescout.firebase.AuthManager
import com.example.gamescout.item_data.GameItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class GameDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
    private val gameDao by lazy { (requireActivity().application as GameApplication).db.gameDao() }

    private val auth = AuthManager()
    private val email = auth.getCurrentUser()?.email


    private val binding get() = _binding!!

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
        setPriceAlert(gameItem.gameID.toString(), gameItem.cheapest.toString())
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
        binding.buttonAlertPrice.setOnClickListener{
           setupAlertButton(gameItem)
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

    //in case a button is needed
    private fun setupAlertButton(gameItem: GameItem) {
        binding.buttonAlertPrice.setOnClickListener {
//            val email = FirebaseAuth.getInstance().currentUser?.email
            val gameID =  gameItem.gameID
            val price = "100.00"
            setPriceAlert(gameID.toString(), price)
        }
    }
    private fun setPriceAlert(gameID: String, price:
    String) {

        if (email != null) {
            RetrofitClient.instance.setPriceAlert(email.toString(), gameID,
                price).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response:
                Response<Boolean>
                ) {
                    if (response.isSuccessful && response.body() == true) {
                        Toast.makeText(context, "Alert set successfully",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to set alert",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Timber.d("Email is null!")
        }
    }
}