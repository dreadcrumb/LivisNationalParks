package com.example.nationalparks.ui.compose.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberAsyncImagePainter
import com.example.nationalparks.R
import com.example.nationalparks.model.TourDetailsItem
import com.example.nationalparks.ui.compose.contracts.TourDetailsContract
import com.example.nationalparks.ui.theme.AppTheme
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModel
import com.example.nationalparks.ui.viewmodels.TourDetailsViewModelInterface

@Composable
fun TourDetailsScreen(
    viewModel: TourDetailsViewModelInterface,
    backAction: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TourDetailsAppBar(
                    viewModel.state.value.tour?.title,
                    backAction
                )
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            TourImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .border(
                        border = BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.primary
                        ),
                        shape = MaterialTheme.shapes.extraSmall
                    ),
                viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourDetailsAppBar(title: String?, backAction: () -> Unit) {
    TopAppBar(
        colors = TopAppBarColors(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier,
        navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(
                        id = R.string.navigate_back
                    ),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Row {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = title ?: stringResource(id = R.string.not_found)
                )
            }
        }
    )
}

@Composable
fun TourImage(
    modifier: Modifier = Modifier,
    viewModel: TourDetailsViewModelInterface
) {
    val cachedImage = viewModel.getImage()
    if (cachedImage != null) {
        Image(
            painter = BitmapPainter(cachedImage.toBitmap().asImageBitmap()),
            contentDescription = null,
            modifier = modifier
        )
    } else {
        // Load the image using Coil if not cached
        Image(
            painter = rememberAsyncImagePainter(model = viewModel.state.value.tour?.image),
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun DetailsPreview() {
    AppTheme {
        TourDetailsScreen(viewModel = providePreviewViewModel(), {})
    }
}

@Composable
private fun providePreviewViewModel(): TourDetailsViewModel {
    val viewModel = TourDetailsViewModel(null, null, null)
    viewModel._state = remember {
        mutableStateOf(
            TourDetailsContract.State(
                tour = TourDetailsItem(
                    id = 0,
                    title = "MONKEH",
                    shortDescription = "short",
                    description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                    thumb = "https://dummyimage.com/400x200/ff7f7f/333333?text=Rhino",
                    image = "https://dummyimage.com/400x200/ff7f7f/333333?text=Rhino",
                    startDate = "01.01.2000 15:00",
                    endDate = "01.01.2000 15:00",
                    price = 85.0
                )
            )
        )
    }

    return viewModel
}