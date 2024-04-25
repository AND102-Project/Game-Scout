package com.example.gamescout

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gamescout.databinding.ActivityMainBinding
import com.example.gamescout.firebase.AuthManager
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.d("MainActivity onCreate started")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.d("Navigated to ${destination.label}")
            invalidateOptionsMenu()
        }
        setSupportActionBar(findViewById(R.id.toolbar))

        // Check if a user is already signed in
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            Timber.d("User is already signed in, navigating to DealsFragment")
            navController.navigate(R.id.dealsFragment)
        } else {
            Timber.d("User is not signed in, navigating to StartFragment")
        }


        Timber.d("MainActivity onCreate completed")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Get the current destination
        val navController = findNavController(R.id.nav_host_fragment)
        val currentDestination = navController.currentDestination?.id

        // Hide or show menus based on the current destination
        when (currentDestination) {
            R.id.startFragment -> {
                // Hide menus
                menu.findItem(R.id.action_logout)?.isVisible = false
                binding.bottomNavigation.visibility = View.GONE
            }
            else -> {
                // Show menus
                menu.findItem(R.id.action_logout)?.isVisible = true
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Perform logout operation
                AuthManager().signOut()
                // Navigate back to the StartFragment
                findNavController(R.id.nav_host_fragment).navigate(R.id.startFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
