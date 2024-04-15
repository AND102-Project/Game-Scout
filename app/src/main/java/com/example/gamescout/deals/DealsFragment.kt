package com.example.gamescout.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamescout.R
import com.example.gamescout.databinding.FragmentDealsBinding
import com.example.gamescout.item_data.GameItem


class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = DealAdapter(getMockGames())
        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getMockGames(): List<GameItem> {
        return listOf(
            GameItem("Game Title 1", "123", "http://example.com/thumb1.jpg"),
            GameItem("Game Title 2", "124", "http://example.com/thumb2.jpg"),
            GameItem("Game Title 3", "125", "http://example.com/thumb3.jpg")
        )
    }


}