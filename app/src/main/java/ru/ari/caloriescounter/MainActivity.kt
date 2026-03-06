package ru.ari.caloriescounter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import ru.ari.caloriescounter.core.navigation.DefaultRouteMapper
import ru.ari.caloriescounter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val routeMapper = DefaultRouteMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainContainer) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            binding.bottomNav.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            val route = routeMapper.fromMenuItemId(item.itemId)
            val destinationId = routeMapper.toDestinationId(route)

            val options = navOptions {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }

            navController.navigate(destinationId, null, options)
            true
        }

        binding.bottomNav.setOnItemReselectedListener {
            // No-op for MVP.
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.diaryFragment -> binding.bottomNav.menu.findItem(R.id.menu_diary).isChecked = true
                R.id.statsFragment -> binding.bottomNav.menu.findItem(R.id.menu_stats).isChecked = true
                R.id.recipesFragment -> binding.bottomNav.menu.findItem(R.id.menu_recipes).isChecked = true
            }
        }
    }
}
