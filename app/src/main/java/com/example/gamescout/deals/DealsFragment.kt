package com.example.gamescout.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamescout.cheapsharkAPI.RetrofitClient
import com.example.gamescout.databinding.FragmentDealsBinding
import com.example.gamescout.item_data.DealItem
import com.example.gamescout.item_data.StoreItem
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = DealAdapter(emptyList(), storeMap) { gameItem ->
            val action = DealsFragmentDirections.actionDealsFragmentToGameDetailFragment(gameItem)
            findNavController().navigate(action)
        }
        binding.gamesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.gamesRecyclerView.adapter = adapter

        fetchStores()
        fetchDeals()
    }

    private fun fetchDeals() {
        RetrofitClient.instance.getDeals().enqueue(object : Callback<List<DealItem>> {
            override fun onResponse(call: Call<List<DealItem>>, response: Response<List<DealItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.updateData(it)
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

}