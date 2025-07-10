package com.silverkey.task.ui.home

import android.view.LayoutInflater
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.silverkey.task.R
import com.silverkey.task.base.BaseActivity
import com.silverkey.task.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override val bindingFactory: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    private lateinit var navController: NavController

    override fun initialization() {
        setupHomeNavGraph()
    }

    override fun setListeners() {
        handleOnBackPressed()
    }

    private fun setupHomeNavGraph() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setOnItemSelectedListener { item ->
            val currentDestination = navController.currentDestination?.id
            if (currentDestination == item.itemId) {
                return@setOnItemSelectedListener true
            }
            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(navController.graph.startDestinationId, false)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .build()

            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home, null, options)
                    true
                }

                R.id.navigation_favorite -> {
                    navController.navigate(R.id.navigation_favorite, null, options)
                    true
                }

                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile, null, options)
                    true
                }

                else -> false
            }
        }
    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this) {
            val currentDest = navController.currentDestination?.id

            when (currentDest) {
                R.id.navigation_home -> {
                    finish()
                }

                R.id.article_details_fragment -> {
                    navController.popBackStack()
                }

                else -> {
                    binding.bottomNav.selectedItemId = R.id.navigation_home
                    navController.navigate(
                        R.id.navigation_home,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(
                                R.id.nav_graph_home,
                                true
                            )
                            .setEnterAnim(R.anim.fade_in)
                            .setExitAnim(R.anim.fade_out)
                            .setPopEnterAnim(R.anim.fade_in)
                            .setPopExitAnim(R.anim.fade_out)
                            .build()
                    )
                }
            }
        }
    }
}
