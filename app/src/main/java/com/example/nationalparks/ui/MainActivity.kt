package com.example.nationalparks.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nationalparks.model.data.TourRemoteSource
import com.example.nationalparks.ui.compose.navigation.NavigationKeys
import com.example.nationalparks.ui.compose.navigation.TourDetailDestination
import com.example.nationalparks.ui.compose.navigation.TourListDestination
import com.example.nationalparks.ui.compose.navigation.TourListLandscapeDestination
import com.example.nationalparks.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var remoteSource: TourRemoteSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cacheContactDetails()
        setContent {
            AppTheme {
                NationalParksApp()
            }
        }
    }

    private fun cacheContactDetails() {
        lifecycleScope.launch {
            remoteSource.getContactDetails()
        }
    }
}

@Composable
private fun NationalParksApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.TOUR_LIST) {
        composable(route = NavigationKeys.Route.TOUR_LIST) {
            TourListDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.TOUR_DETAIL,
            arguments = listOf(navArgument(NavigationKeys.Arg.TOUR_ID) {
                type = NavType.IntType
            })
        ) {
            TourDetailDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.TOURS_LANDSCAPE_DETAIL,
            arguments = listOf(navArgument(NavigationKeys.Arg.TOUR_ID) {
                type = NavType.IntType
            })
        ) {
            TourListLandscapeDestination(navController)
        }
    }
}
