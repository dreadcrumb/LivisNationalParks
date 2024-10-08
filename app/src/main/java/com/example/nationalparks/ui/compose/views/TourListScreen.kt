package com.example.nationalparks.ui.compose.views

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberAsyncImagePainter
import com.example.nationalparks.R
import com.example.nationalparks.model.TourItem
import com.example.nationalparks.ui.compose.contracts.LoadingState
import com.example.nationalparks.ui.compose.contracts.ToursContract
import com.example.nationalparks.ui.compose.elements.LoadingBar
import com.example.nationalparks.ui.compose.elements.ToursAppBar
import com.example.nationalparks.ui.compose.utils.hasNetworkComposable
import com.example.nationalparks.ui.theme.AppTheme
import com.example.nationalparks.ui.viewmodels.Sorting
import com.example.nationalparks.ui.viewmodels.TourListViewModel
import com.example.nationalparks.ui.viewmodels.TourListViewModelInterface

@Composable
fun TourListScreen(
    viewModel: TourListViewModelInterface,
    showTopElements: Boolean,
    onNavigationRequested: (itemId: Int) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (showTopElements) {
                Column(Modifier.background(MaterialTheme.colorScheme.background),) {
                    ToursAppBar()
                    HorizontalDivider(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
        ) {
            if (showTopElements) {
                Row {
                    SortButtons(
                        { viewModel.setSorting(Sorting.STANDARD) },
                        { viewModel.setSorting(Sorting.TOP5) },
                        viewModel.state.value.sorting
                    )
                }
            }
            Row(Modifier.padding(horizontal = 6.dp)) {
                ToursList(
                    tourItems = viewModel.state.value.tours,
                    viewModel,
                ) { itemId ->
                    onNavigationRequested(itemId)
                }
            }
            if (viewModel.state.value.loadingState == LoadingState.LOADING) LoadingBar()
        }
    }
}

@Composable
fun SortButtons(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    sorting: Sorting
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(
                    if (sorting == Sorting.STANDARD)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primaryContainer
                )
                .border(
                    border = BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable(onClick = { onLeftClick() }),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "ALL", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
        Box(
            modifier = Modifier
                .weight(1f)  // Take up the other half of the screen width
                .fillMaxSize()
                .background(
                    if (sorting == Sorting.TOP5)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primaryContainer
                )
                .border(
                    border = BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable(onClick = { onRightClick() }),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "FAB 5", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        }
    }
}

@Composable
fun ToursList(
    tourItems: List<TourItem>,
    viewModel: TourListViewModelInterface,
    onItemClicked: (id: Int) -> Unit = { }
) {
    // Handle Empty List
    if (tourItems.isEmpty()) {
        EmptyList()
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 6.dp),
    ) {
        items(tourItems) { item ->
            TourItemRow(item = item, viewModel, onItemClicked = onItemClicked)
        }
    }
}

@Composable
fun EmptyList() {
    if (!hasNetworkComposable()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_internet),
                contentDescription = stringResource(id = R.string.no_internet),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(250.dp)
                    .padding(24.dp),
            )
            Text(
                text = stringResource(id = R.string.no_internet),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.imaginary_logo), // Replace with meaningful image
                contentDescription = stringResource(id = R.string.placeholder_empty_list),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(250.dp)
                    .padding(24.dp),
            )
            Text(
                text = stringResource(id = R.string.no_tours_found),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
    return
}

@Composable
fun TourItemRow(
    item: TourItem,
    viewModel: TourListViewModelInterface,
    onItemClicked: (id: Int) -> Unit = { }
) {
    Row(modifier = Modifier
        .fillMaxSize()
        .animateContentSize()
        .wrapContentHeight()
        .padding(vertical = 6.dp)
        .border(
            border = BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.extraSmall
        )
        .background(MaterialTheme.colorScheme.primaryContainer)
        .clickable {
            Log.i("TourListScreen", "TourItem '${item.title}' with id '${item.id}' clicked")
            onItemClicked(item.id)
        }) {
        Box(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
            TourItemThumbnail(item.thumb, viewModel)
        }
        TourItemDetails(
            item = item
        )
    }

}

@Composable
fun TourItemDetails(
    item: TourItem?
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(0.9f)) {
                Text(
                    text = item?.title ?: "",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp)
                    .weight(1.0f)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = String.format(
                        stringResource(id = R.string.price),
                        item?.price?.toString() ?: "XX"
                    ),
                    textAlign = TextAlign.End
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(end = 6.dp)
        ) {
            Text(
                text = item?.shortDescription ?: "",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = String.format(
                    stringResource(id = R.string.available_till),
                    item?.endDate ?: "XX"
                ),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        }
    }
}

@Composable
fun TourItemThumbnail(
    thumbnailUrl: String,
    viewModel: TourListViewModelInterface
) {
    Box(
        modifier = Modifier
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder or a specific size Box
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(100.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Filled.Face, contentDescription = "loading")
        }

        val cachedImage = viewModel.getCachedImage(thumbnailUrl)
        if (cachedImage != null) {
            Image(
                painter = BitmapPainter(cachedImage.toBitmap().asImageBitmap()),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
                    .wrapContentHeight()
                    .padding(horizontal = 6.dp)
                    .border(
                        border = BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.primary
                        ),
                        shape = MaterialTheme.shapes.extraSmall
                    )
            )
        } else {
            // Load the image using Coil if not cached
            Image(
                painter = rememberAsyncImagePainter(model = thumbnailUrl),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
                    .wrapContentHeight()
                    .padding(horizontal = 6.dp)
                    .border(
                        border = BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.primary
                        ),
                        shape = MaterialTheme.shapes.extraSmall
                    )
            )
        }
//        SubcomposeAsyncImage(
//            model = thumbnailUrl,
//            contentDescription = null,
//            error = {
//                Icon(
//                    imageVector = Icons.Default.Warning,
//                    contentDescription = "Error loading image"
//                )
//            },
//            contentScale = ContentScale.Fit,
//            modifier = Modifier
//                .width(100.dp)
//                .height(50.dp)
//                .wrapContentHeight()
//                .padding(horizontal = 6.dp)
//                .border(
//                    border = BorderStroke(
//                        2.dp,
//                        MaterialTheme.colorScheme.primary
//                    ),
//                    shape = MaterialTheme.shapes.extraSmall
//                )
//        )
//                Image(
//                    painter = painter,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                        .border(
//                            border = BorderStroke(
//                                2.dp,
//                                MaterialTheme.colorScheme.primary
//                            ),
//                            shape = MaterialTheme.shapes.extraSmall
//                        ),
//                    contentDescription = stringResource(id = R.string.tour_thumbnail_description),
//                )

//            Image(
//                painter = rememberImagePainter(
//                    data = thumbnailUrl, builder = iconTransformationBuilder
//                ),
//                modifier = Modifier
//                    .padding(horizontal = 6.dp)
//                    .width(100.dp)
//                    .height(50.dp) // height = width / 2
//                    .border(
//                        border = BorderStroke(
//                            2.dp,
//                            MaterialTheme.colorScheme.primary
//                        ),
//                        shape = MaterialTheme.shapes.extraSmall
//                    ),
//                contentDescription = stringResource(id = R.string.tour_thumbnail_description),
//            )
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    AppTheme {
        val viewModel = providePreviewViewModel(
            list = listOf(
                TourItem(
                    id = 0,
                    title = "Tour 1",
                    shortDescription = "Short Description 1",
                    thumb = "https://dummyimage.com/400x200/ff7f7f/333333?text=Gorilla",
                    startDate = "01.01.2000 15:00",
                    endDate = "01.01.2000 17:00",
                    price = 20.5
                ),
                TourItem(
                    id = 1,
                    title = "Tour 2",
                    shortDescription = "This is a much longer description to see how this looks",
                    thumb = "https://dummyimage.com/400x200/ff7f7f/333333?text=Tiger",
                    startDate = "01.01.2000 15:00",
                    endDate = "01.01.2000 17:00",
                    price = 15.0
                ),
                TourItem(
                    id = 2,
                    title = "Tour with a much longer title",
                    shortDescription = "This is a much longer description to see how this looks",
                    thumb = "https://dummyimage.com/400x200/ff7f7f/333333?text=Pig",
                    startDate = "01.01.2000 15:00",
                    endDate = "01.01.2000 17:00",
                    price = 15.0
                )
            )
        )
        TourListScreen(
            viewModel,
            true,
            { })
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyPreview() {
    val viewModel = providePreviewViewModel()
    AppTheme {
        TourListScreen(
            viewModel,
            true,
            { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    val viewModel = providePreviewViewModel()
    viewModel._state.value.loadingState = LoadingState.LOADING
    AppTheme {
        TourListScreen(
            viewModel,
            true,
            { }
        )
    }
}

@Composable
private fun providePreviewViewModel(list: List<TourItem> = listOf()): TourListViewModel {
    val viewModel = TourListViewModel(null, null)
    viewModel._state = remember {
        mutableStateOf(
            ToursContract.State(
                tours = list,
                loadingState = LoadingState.SUCCESS
            )
        )
    }

    return viewModel
}