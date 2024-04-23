package com.example.gamescout.favorites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamescout.database.GameApplication
import com.example.gamescout.databinding.FragmentFavoriteBinding
import com.example.gamescout.item_data.GameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favGames = mutableListOf<GameItem>()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val repository by lazy {
        (requireActivity().application as GameApplication).repository
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter(requireContext(), favGames) { gameItem ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToGameDetailFragment(gameItem)
            findNavController().navigate(action)
        }

        binding.favoriteRV.adapter = adapter
        binding.favoriteRV.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.favoriteRV.addItemDecoration(dividerItemDecoration)

        fetchFavoriteGames()
    }

    private fun fetchFavoriteGames() {
        lifecycleScope.launch(Dispatchers.IO) {
            (requireActivity().application as GameApplication).db.gameDao().getAll()
                .collect { databaseList ->
                    favGames.clear()
                    databaseList.map { entity ->
                        GameItem(
                            entity.gameID,
                            entity.steamAppID,
                            entity.cheapest,
                            entity.cheapestDealID,
                            entity.external,
                            entity.internalName,
                            entity.thumb

                        )
                    }
                        .also { mappedList ->
                            favGames.addAll(mappedList)
                            launch(Dispatchers.Main) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Important to avoid memory leaks
    }

}
