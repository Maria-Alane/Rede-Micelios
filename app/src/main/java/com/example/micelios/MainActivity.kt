package com.example.micelios

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userRepository: UserRepository

    private val destinationsWithoutBottomNav = setOf(
        R.id.welcomeFragment,
        R.id.hyphaDetailFragment,
        R.id.chatFragment,
        R.id.createHyphaFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        lifecycleScope.launch {
            val startDestination = resolveStartDestination(FirebaseAuth.getInstance())

            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
            navGraph.setStartDestination(startDestination)
            navController.graph = navGraph

            binding.bottomNavigation.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.bottomNavigation.visibility =
                    if (destination.id in destinationsWithoutBottomNav) View.GONE else View.VISIBLE
            }
        }
    }

    private suspend fun resolveStartDestination(auth: FirebaseAuth): Int {
        val currentUserId = auth.currentUser?.uid ?: return R.id.welcomeFragment

        return if (userRepository.userExists(currentUserId)) {
            R.id.homeFragment
        } else {
            auth.signOut()
            R.id.welcomeFragment
        }
    }
}