package com.example.gamescout.game_detail

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gamescout.cheapsharkAPI.RetrofitClient
import com.example.gamescout.databinding.FragmentGameDetailBinding
import com.example.gamescout.item_data.GameItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.auth.FirebaseAuth

class GameDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
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

    private fun addGameToFavorites(gameItem: GameItem) {
        val favoritesJson = sharedPreferences.getString("favorites", "[]")
        val type = object : TypeToken<MutableList<GameItem>>() {}.type
        val favorites: MutableList<GameItem> =
            gson.fromJson(favoritesJson, type)
        if (!favorites.any { it.steamAppID == gameItem.steamAppID }) {
            favorites.add(gameItem)
            setPriceAlert("saniz.sth123@gmail.com", gameItem.gameID.toString(), gameItem.cheapest.toString())
            sharedPreferences.edit().putString("favorites",
                gson.toJson(favorites)).apply()
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
            val price = "0.00"
            setPriceAlert("saniz.sth123@gmail.com", gameID.toString(), price)
        }
    }
    private fun setPriceAlert(email: String, gameID: String, price:
    String) {
        RetrofitClient.instance.setPriceAlert(email, gameID,
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
    }
}