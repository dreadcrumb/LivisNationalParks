package com.example.nationalparks.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nationalparks.ui.compose.navigation.NavigationKeys
import com.example.nationalparks.ui.compose.navigation.TourListDestination
import com.example.nationalparks.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NationalparksApp()
            }
        }
    }

}

@Composable
private fun NationalparksApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.TOUR_LIST) {
        composable(route = NavigationKeys.Route.TOUR_LIST) {
            TourListDestination(navController)
        }
//        composable(
//            route = NavigationKeys.Route.CLUB_DETAIL,
//            arguments = listOf(navArgument(NavigationKeys.Arg.CLUB_ID) {
//                type = NavType.StringType
//            })
//        ) {
//            ClubDetailDestination(navController)
//        }
    }
}
