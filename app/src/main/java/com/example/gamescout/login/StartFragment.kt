package com.example.gamescout.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamescout.R
import com.example.gamescout.databinding.FragmentStartBinding
import com.example.gamescout.firebase.AuthManager
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber


class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private lateinit var authManager: AuthManager

    override fun onStart() {
        super.onStart()
        Timber.d("onStart called in StartFragment")

    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop called in StartFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager()

        binding.googleSignInButton.setOnClickListener {
            val signInIntent = authManager.signInWithGoogle(requireActivity())
            signInLauncher.launch(signInIntent)
        }
        binding.emailSignInButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authManager.signInWithEmailPassword(email, password,
                    onSuccess = {
                        navigateToDealsFragment() // Navigate on success
                    },
                    onFailure = {
                        // Display toast message for failure
                        Toast.makeText(requireContext(), "Wrong email and/or password", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                // Handle empty email or password fields
            }
        }

        binding.newUserButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authManager.createUserWithEmailAndPassword(email, password,
                    onSuccess = {
                        navigateToDealsFragment() // Navigate on success
                    },
                    onFailure = {
                        // Display toast message for failure
                        Toast.makeText(requireContext(), "Failed to create new user", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                // Handle empty email or password fields
            }
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        val response = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Timber.d("Firebase log in success with user " + user.toString())
            // Proceed with your logic here
            navigateToDealsFragment()
        } else {
            // Sign in failed
            Timber.e("Sign-in failed: ${response?.error?.message}")
        }
    }

    private fun navigateToDealsFragment() {
        findNavController().navigate(R.id.action_startFragment_to_dealsFragment)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

