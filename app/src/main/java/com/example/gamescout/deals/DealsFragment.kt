package com.example.gamescout.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamescout.cheapsharkAPI.RetrofitClient
import com.example.gamescout.database.GameApplication
import com.example.gamescout.databinding.FragmentDealsBinding
import com.example.gamescout.item_data.DealItem
import com.example.gamescout.item_data.StoreItem
import androidx.appcompat.widget.SearchView
import com.example.gamescout.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DealAdapter
    private var storeMap: Map<String, String> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() {
        val gameDao = (requireActivity().application as GameApplication).db.gameDao()

        adapter = DealAdapter(emptyList(), storeMap, { gameItem ->
            val action = DealsFragmentDirections.actionDealsFragmentToGameDetailFragment(gameItem)

            findNavController().navigate(action)
        }, gameDao, viewLifecycleOwner.lifecycleScope)
        binding.gamesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.gamesRecyclerView.adapter = adapter

        fetchStores()
        fetchDeals()
    }

    private fun fetchDeals() {
        RetrofitClient.instance.getDeals().enqueue(object : Callback<List<DealItem>> {
            override fun onResponse(call: Call<List<DealItem>>, response: Response<List<DealItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { deals ->
                        val dealItems = deals.map { mapDealItemToGameItem(it) }
                        adapter.updateData(dealItems)
                    }
                } else {
                    Timber.e("API Request Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DealItem>>, t: Throwable) {
                Timber.e("API Request Error: ${t.message}")
            }
        })
    }

    private fun fetchStores() {
        RetrofitClient.instance.getStores().enqueue(object : Callback<List<StoreItem>> {
            override fun onResponse(call: Call<List<StoreItem>>, response: Response<List<StoreItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { stores ->
                        storeMap = stores.associate { it.storeID to it.storeName }
                        adapter.updateStoreMap(storeMap)
                    }
                } else {
                    Timber.e("API Request Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<StoreItem>>, t: Throwable) {
                Timber.e("API Request Error: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchGames(name: String) {
        RetrofitClient.instance.searchGames(name).enqueue(object : Callback<List<DealItem>> {
            override fun onResponse(call: Call<List<DealItem>>, response: Response<List<DealItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Timber.i("Received search results: $it")
                        adapter.clearData()
                        adapter.updateData(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Timber.e("API Request Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DealItem>>, t: Throwable) {
                Timber.e("API Request Error: ${t.message}")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchGames(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }


    fun mapDealItemToGameItem(deal: DealItem): DealItem {
        return DealItem(
            gameID = deal.gameID ?: "",
            steamAppID = deal.steamAppID ?: "",
            salePrice = deal.salePrice ?: "",
            dealID = deal.dealID ?: "",
            title = deal.title ?: "",
            internalName = deal.internalName ?: "",
            thumb = deal.thumb ?: "",
            metacriticLink = deal.metacriticLink ?: "",
            storeID = deal.storeID ?: "",
            cheapest = deal.cheapest ?: "",
            cheapestDealID = deal.cheapestDealID ?: "",
            external = deal.external ?: "",
            normalPrice = deal.normalPrice ?: "",
            isOnSale = deal.isOnSale ?: "",
            savings = deal.savings ?: "",
            metacriticScore = deal.metacriticScore ?: "",
            steamRatingText = deal.steamRatingText ?: "",
            steamRatingPercent = deal.steamRatingPercent ?: "",
            steamRatingCount = deal.steamRatingCount ?: "",
            releaseDate = deal.releaseDate ?: "",
            lastChange = deal.lastChange ?: "",
            dealRating = deal.dealRating ?: ""
        )
    }

}