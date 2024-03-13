package app.electronics.com.testtask.presentation.home


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import app.electronics.com.testtask.R
import app.electronics.com.testtask.presentation.common.ui.BoxWithSwipeRefresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetails: (id: Int) -> Unit,
    viewModel: HomeViewModel
) {
    val curatedPhotos = viewModel.curatedPhotosState.collectAsLazyPagingItems()
    val isRefreshing = curatedPhotos.loadState.refresh is LoadState.Loading

    Scaffold(
        topBar = {
            Surface(shadowElevation = 3.dp) {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        }
    ) {
        BoxWithSwipeRefresh(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            onSwipe = { curatedPhotos.refresh() },
            isRefreshing = isRefreshing,
        ) {
            val loadState = curatedPhotos.loadState
            if (curatedPhotos.itemCount < 1) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        Modifier
                            .align(Alignment.Center)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center),
                            text = "No Items"
                        )
                    }
                }
            } else {
                LazyColumn {
                    item { Spacer(modifier = Modifier.padding(4.dp)) }
                    items(
                        curatedPhotos.itemCount,
                        contentType = curatedPhotos.itemContentType(),
                    ) { index ->
                        curatedPhotos[index]?.let { item ->
                            ItemCard(
                                photographerName = item.photographerName,
                                onClick = { onNavigateToDetails(item.id) }
                            )
                        }
                    }
                    curatedPhotos.apply {
                        when {
                            loadState.refresh is LoadState.Error -> {
                                val error = curatedPhotos.loadState.refresh as LoadState.Error
                                item {
                                    ErrorMessage(
                                        modifier = Modifier.fillParentMaxSize(),
                                        message = error.error.localizedMessage.orEmpty(),
                                        onClickRetry = { retry() })
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item { LoadingNextPageItem(modifier = Modifier) }
                            }

                            loadState.append is LoadState.Error -> {
                                val error = curatedPhotos.loadState.append as LoadState.Error
                                item {
                                    ErrorMessage(
                                        modifier = Modifier,
                                        message = error.error.localizedMessage.orEmpty(),
                                        onClickRetry = { retry() })
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.padding(4.dp)) }
                }
            }
        }
    }
}
