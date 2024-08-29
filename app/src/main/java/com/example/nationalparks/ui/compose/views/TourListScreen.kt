package com.example.livisnationalparks.ui.compose.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.example.livisnationalparks.ui.compose.contracts.ToursContract
import com.example.livisnationalparks.model.TourItem
import com.example.nationalparks.R
import com.example.nationalparks.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.Date

@Composable
fun TourListScreen(
    state: ToursContract.State,
    effectFlow: Flow<ToursContract.Effect>?,
    onNavigationRequested: (itemId: String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            if (effect is ToursContract.Effect.DataWasLoaded) snackbarHostState.showSnackbar(
                message = "", // context.getString(R.string.clubs_loaded),
                duration = SnackbarDuration.Short
            )
        }?.collect()
    }
    // SORT here?
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ToursAppBar()
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ToursList(
                tourItems = state.tours, loading = state.isLoading//, sorting = sorting
            ) { itemId ->
                onNavigationRequested(itemId)
            }
            if (state.isLoading) LoadingBar()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToursAppBar() {
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    TopAppBar(title = {
        Icon(painter = painterResource(id = R.drawable.imaginary_logo), contentDescription = "REPLACE")
        Text(text = stringResource(id = R.string.app_name))
                      },
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More",
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                // 6
                DropdownMenuItem(
                    text = { Text("All Items")},
                    onClick = { /* TODO */ },
                )
                DropdownMenuItem(
                    text = { Text("Fab 5")},
                    onClick = { /* TODO */ },
                )
            }
        }
        )
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(Modifier.size(150.dp))
    }
}


@Composable
fun ToursList(tourItems: List<TourItem>, loading: Boolean, onItemClicked: (id: String) -> Unit = { }) {

}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
fun EmptyPreview() {
    AppTheme {
        TourListScreen(ToursContract.State(tours = listOf()), null, { })
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
fun ListPreview() {
    AppTheme {
        TourListScreen(ToursContract.State(
            tours = listOf(
                TourItem(
                    id = 0,
                    title = "Tour 1",
                    shortDescription = "Short Description 1",
                    thumb = "https://dummyimage.com/400x200/ff7f7f/333333?text=Gorilla",
                    startDate = Date(),
                    endDate = Date(),
                    price = 20.5
                ),
                TourItem(
                    id = 0,
                    title = "Tour 2",
                    shortDescription = "Short Description 2",
                    thumb = "https://dummyimage.com/400x200/ff7f7f/333333?text=Tiger",
                    startDate = Date(),
                    endDate = Date(),
                    price = 15.0
                )
            )
        ), null, { })
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    AppTheme {
        TourListScreen(ToursContract.State(isLoading = true), null, { })
    }
}