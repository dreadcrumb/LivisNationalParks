package com.example.nationalparks.ui.compose.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nationalparks.R
import com.example.nationalparks.ui.compose.elements.ToursAppBar
import com.example.nationalparks.ui.theme.AppTheme
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModel
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModelInterface
import com.example.nationalparks.ui.viewmodels.TourListViewModel
import com.example.nationalparks.ui.viewmodels.TourListViewModelInterface

@Composable
fun LandscapeScreen(
    listViewModel: TourListViewModelInterface,
    tourId: Int? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                ToursAppBar()
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
    ) { paddingValues ->

        Row(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            var selectedTour by remember {
                mutableStateOf(tourId)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Column {
                    TourListScreen(viewModel = listViewModel, showTopElements = false) { itemId ->
                        selectedTour = itemId
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.imaginary_logo),
                    contentDescription = stringResource(
                        id = R.string.logo
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
                if (selectedTour != null) {
                    val detailViewModel: TourDetailsViewModelInterface =  hiltViewModel<TourDetailsViewModel>()
                    detailViewModel.initializeManually(selectedTour!!)
                    Column {
                        TourDetailsScreen(viewModel = detailViewModel, false) {}
                    }
                }
            }
        }
    }
}

@Preview(
    widthDp = 640,
    heightDp = 360,
    uiMode = android.content.res.Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480"
)
@Composable
fun LandscapePreview() {
    AppTheme {
        LandscapeScreen(
            listViewModel = TourListViewModel(null, null),
        )
    }
}