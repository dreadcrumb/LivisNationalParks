package com.example.nationalparks.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nationalparks.ui.compose.views.LandscapeScreen
import com.example.nationalparks.ui.compose.views.TourDetailsScreen
import com.example.nationalparks.ui.compose.views.TourListScreen
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModel
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModelInterface
import com.example.nationalparks.ui.viewmodels.TourListViewModel
import com.example.nationalparks.ui.viewmodels.TourListViewModelInterface

@Composable
fun TourListDestination(navController: NavHostController) {
    val viewModel: TourListViewModelInterface = hiltViewModel<TourListViewModel>()
    OrientationChangedComposable(
        changedToLandscape = { navController.navigate("${NavigationKeys.Route.TOURS_LANDSCAPE}/${-1}") },
        changedToPortrait = {}
    )
    TourListScreen(
        viewModel = viewModel,
        true,
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.TOUR_LIST}/${itemId}")
        })
}

@Composable
fun TourDetailDestination(navController: NavHostController) {
    val viewModel: TourDetailsViewModelInterface = hiltViewModel<TourDetailsViewModel>()
    OrientationChangedComposable(
        changedToLandscape = { navController.navigate("${NavigationKeys.Route.TOURS_LANDSCAPE}/${viewModel.state.value.tour?.id ?: -1}") },
        changedToPortrait = {}
    )
    TourDetailsScreen(viewModel, true,
        backAction = { navController.navigate(NavigationKeys.Route.TOUR_LIST) })
}

@Composable
fun TourListLandscapeDestination(navController: NavHostController) {
    val listViewModel: TourListViewModelInterface = hiltViewModel<TourListViewModel>()
    val detailViewModel: TourDetailsViewModelInterface = hiltViewModel<TourDetailsViewModel>()

    OrientationChangedComposable(
        changedToLandscape = {},
        changedToPortrait = {
            val itemId = detailViewModel.state.value.tour?.id ?: -1
            if (itemId == -1) {
                navController.navigate(NavigationKeys.Route.TOUR_LIST)
            } else {
                navController.navigate("${NavigationKeys.Route.TOUR_LIST}/${itemId}")
            }
        }
    )
    LandscapeScreen(
        listViewModel,
        detailViewModel
    )
}
