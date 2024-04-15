package com.example.gamescout.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamescout.R
import com.example.gamescout.databinding.FragmentStartBinding
import timber.log.Timber


class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("StartFragment onCreateView called")
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("StartFragment onViewCreated called")
        binding.loginButton.setOnClickListener {
            Timber.d("Login button clicked")
            findNavController().navigate(R.id.action_startFragment_to_dealsFragment)
        }
        binding.root.requestLayout()
        // Log visibility state
        Timber.d("TextView visibility: ${binding.startInstructions.visibility}")
        Timber.d("Button visibility: ${binding.loginButton.visibility}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}