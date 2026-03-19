package com.example.micelios

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.ActivityMainBinding
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(applicationContext)
        sessionManager.startSession()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val userRepository =
            UserRepository(MiceliosDatabase.getDatabase(applicationContext).userDao())

        lifecycleScope.launch {
            val startDestination = resolveStartDestination(sessionManager, userRepository)

            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
            navGraph.setStartDestination(startDestination)
            navController.graph = navGraph

            binding.bottomNavigation.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.bottomNavigation.visibility =
                    if (destination.id == R.id.welcomeFragment) View.GONE else View.VISIBLE
            }
        }
    }

    private suspend fun resolveStartDestination(
        sessionManager: SessionManager,
        userRepository: UserRepository
    ): Int {
        val currentUserId = sessionManager.getCurrentUserId()

        if (currentUserId == null) {
            return R.id.welcomeFragment
        }

        val userExists = userRepository.userExists(currentUserId)

        return if (userExists) {
            R.id.homeFragment
        } else {
            sessionManager.clearSession()
            R.id.welcomeFragment
        }
    }
}