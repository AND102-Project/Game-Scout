
package com.example.gamescout.top_rated

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gamescout.R
import com.example.gamescout.cheapsharkAPI.RetrofitClient
import com.example.gamescout.database.GameApplication
import com.example.gamescout.databinding.FragmentDealsBinding
import com.example.gamescout.databinding.FragmentTopRatedBinding
import com.example.gamescout.deals.DealsFragmentDirections
import com.example.gamescout.item_data.DealItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopRatedFragment : Fragment() {

    private lateinit var adapter: TopRatedAdapter
    private var storeMap: Map<String, String> = emptyMap()

    private var _binding: FragmentTopRatedBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTopRatedBinding.inflate(inflater, container, false)

        Log.d("TopRatedFragment", "Fragment started")

        return binding.root

    }

    private fun setupRecyclerView() {
        val gameDao = (requireActivity().application as GameApplication).db.gameDao()

        adapter = TopRatedAdapter(emptyList(), storeMap, { gameItem ->
            val action = TopRatedFragmentDirections.actionTopRatedFragmentToGameDetailFragment(gameItem)
            findNavController().navigate(action)
        }, gameDao, viewLifecycleOwner.lifecycleScope)


        binding.topratedRV.adapter = adapter


        fetchTopRatedGames()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

    }

    private fun fetchTopRatedGames() {
        RetrofitClient.instance.getTopRated(60, 70).enqueue(object : Callback<List<DealItem>> {
            override fun onResponse(call: Call<List<DealItem>>, response: Response<List<DealItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { games ->
                        adapter.updateGames(games)
                    }
                }
            }

            override fun onFailure(call: Call<List<DealItem>>, t: Throwable) {
                Log.d("TopRatedFragment", "Not loading")

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}