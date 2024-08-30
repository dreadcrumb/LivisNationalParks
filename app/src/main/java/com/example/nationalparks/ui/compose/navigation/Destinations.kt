package com.example.nationalparks.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nationalparks.ui.compose.views.TourDetailsScreen
import com.example.nationalparks.ui.compose.views.TourListScreen
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModel
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModelInterface
import com.example.nationalparks.ui.viewmodels.TourListViewModel
import com.example.nationalparks.ui.viewmodels.TourListViewModelInterface
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun TourListDestination(navController: NavHostController) {
    val viewModel: TourListViewModelInterface = hiltViewModel<TourListViewModel>()
    TourListScreen(
        viewModel = viewModel,
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.TOUR_LIST}/${itemId}")
        })
}

@Composable
fun TourDetailDestination(navController: NavHostController) {
    val viewModel: TourDetailsViewModelInterface =  hiltViewModel<TourDetailsViewModel>()
    TourDetailsScreen(viewModel,
        backAction = { navController.navigateUp() })
}