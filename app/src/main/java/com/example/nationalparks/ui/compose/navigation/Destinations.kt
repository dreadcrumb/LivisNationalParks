package com.example.nationalparks.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nationalparks.ui.compose.navigation.NavigationKeys
import com.example.nationalparks.ui.compose.views.TourListScreen
import com.example.nationalparks.ui.viewmodels.TourListViewModel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun TourListDestination(navController: NavHostController) {
    val viewModel: TourListViewModel = hiltViewModel()
    TourListScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        viewModel = viewModel,
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.TOUR_LIST}/${itemId}")
        })
}