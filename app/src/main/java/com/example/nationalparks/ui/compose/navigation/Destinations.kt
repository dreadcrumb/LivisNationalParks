package com.example.nationalparks.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.livisnationalparks.ui.viewmodels.TourListViewModel

@Composable
fun TourListDestination(navController: NavHostController) {
    val viewModel: TourListViewModel = hiltViewModel()

}