package com.example.gamescout.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamescout.R
import com.example.gamescout.cheapsharkAPI.RetrofitClient
import com.example.gamescout.databinding.FragmentDealsBinding
import com.example.gamescout.item_data.GameItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DealAdapter

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
        adapter = DealAdapter(emptyList())
        binding.gamesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.gamesRecyclerView.adapter = adapter

        fetchDeals()
    }

    private fun fetchDeals() {
        RetrofitClient.instance.getDeals().enqueue(object : Callback<List<GameItem>> {
            override fun onResponse(call: Call<List<GameItem>>, response: Response<List<GameItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.updateData(it)
                    }
                } else {
                    Timber.e("API Request Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GameItem>>, t: Throwable) {
                Timber.e("API Request Error: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}