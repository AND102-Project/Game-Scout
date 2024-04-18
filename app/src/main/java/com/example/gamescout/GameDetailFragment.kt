package com.example.gamescout

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gamescout.databinding.FragmentGameDetailBinding
import com.example.gamescout.item_data.Dealtem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * A simple [Fragment] subclass.
 * Use the [GameDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dealtem: Dealtem
    private val gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container,
            false)
        sharedPreferences =
            requireActivity().getSharedPreferences("GameScoutPreferences",
                Context.MODE_PRIVATE)
// TODO: Retrieve the GameItem from arguments
        dealtem = Dealtem("Sample Game", "123",
            "http://example.com/thumb.jpg", "29.99", "1", "deal-id")
        displayGameDetails(dealtem)
        return binding.root
    }

    private fun displayGameDetails(dealtem: Dealtem) {
        Glide.with(this)
            .load(dealtem.thumb)
            .into(binding.imageGameThumb)
        binding.textGameTitle.text = dealtem.title
        binding.textGamePrice.text = getString(R.string.best_price,
            dealtem.salePrice)
        binding.buttonBuyNow.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.cheapshark.com/redirect?dealID=${dealtem.dealID}")
            )
            startActivity(intent)
        }
        binding.buttonAddToFavorites.setOnClickListener {
            addGameToFavorites(dealtem)
        }
        binding.buttonTrackPrice.setOnClickListener {
            trackGamePrice(dealtem)
        }
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addGameToFavorites(dealtem: Dealtem) {
        val favoritesJson = sharedPreferences.getString("favorites", "[]")
        val type = object : TypeToken<MutableList<Dealtem>>() {}.type
        val favorites: MutableList<Dealtem> =
            gson.fromJson(favoritesJson, type)
        if (!favorites.any { it.steamAppID == dealtem.steamAppID }) {
            favorites.add(dealtem)
            sharedPreferences.edit().putString("favorites",
                gson.toJson(favorites)).apply()
        }
    }

    private fun trackGamePrice(dealtem: Dealtem) {
        // Here I don’t know how can we track Game Price change and change link to a cheaper vendor and send a push notification
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}