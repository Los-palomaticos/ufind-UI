package social.ufind.ui.screen.home.savedpost

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.BottomBarScreen
import social.ufind.navigation.NavRoute
import social.ufind.ui.screen.home.post.PageHeader
import social.ufind.ui.screen.home.post.itempost.ItemPost
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModelMethods
import social.ufind.ui.screen.home.post.itempost.ItemUiState
import social.ufind.ui.screen.home.savedpost.viewmodel.SavedPostViewModel

object SavedPostScreen: NavRoute<SavedPostViewModel> {
    override val route: String
        get() = BottomBarScreen.SavedPosts.route
    @Composable
    override fun viewModel(): SavedPostViewModel = viewModel(factory = SavedPostViewModel.Factory)
    @Composable
    override fun Content(viewModel: SavedPostViewModel) {
        SavedPostScreen(viewModel = viewModel)
    }

}
@Composable
fun SavedPostScreen(viewModel: SavedPostViewModel){
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle().value
    val lazyPagingItems = viewModel.postList.collectAsLazyPagingItems()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.refresh() }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageHeader()
            Text(text = "Publicaciones Guardadas", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 16.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    HandleItemUiState(viewModel)
                    PostList(lazyPagingItems = lazyPagingItems, viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostList(lazyPagingItems: LazyPagingItems<PostWithAuthorAndPhotos>, viewModel: SavedPostViewModel) {
    val state = rememberLazyListState(0)
    if (lazyPagingItems.itemCount == 0) {
        viewModel.refresh()

    }
    LazyColumn(
        state = state
    ) {
        item {
            HandleRefreshStatus(lazyPagingItems)
            HandlePrependStatus(lazyPagingItems = lazyPagingItems)
        }
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey{it.post.id}
        ) {index ->
            ItemPost(post = lazyPagingItems[index], viewModel = viewModel, modifier = Modifier.animateItemPlacement())
        }
        item {
            HandleAppendStatus(lazyPagingItems = lazyPagingItems)
        }
    }
}

@Composable
fun HandleItemUiState (viewModel: ItemPostViewModelMethods) {
    val state = viewModel.itemUiState.collectAsState()
    val context = LocalContext.current
    when(state.value) {
        is ItemUiState.Resume -> {}
        is ItemUiState.Success -> {
            Toast.makeText(context, (state.value as ItemUiState.Success).message, Toast.LENGTH_SHORT).show()
        }
        is ItemUiState.ErrorWithMessage -> {
            Toast.makeText(context,
                (state.value as ItemUiState.ErrorWithMessage).errorMessages[0], Toast.LENGTH_SHORT).show()
        }
        is ItemUiState.Error -> {
            Toast.makeText(context, "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show()
        }
    }
    viewModel.resetState()
}

@Composable
fun HandleRefreshStatus(lazyPagingItems: LazyPagingItems<PostWithAuthorAndPhotos>) {
    when(lazyPagingItems.loadState.refresh){
        is LoadState.Loading -> {
            Row(modifier= Modifier
                .zIndex(.75f)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error-> {
            Text("Error de conexión...", Modifier.zIndex(.5f), color = MaterialTheme.colors.onError)
        }
        is LoadState.NotLoading -> {
            if (lazyPagingItems.itemCount == 0) {
                Text("No tienes publicaciones guardadas", Modifier.zIndex(.5f))
            }
        }
    }
}


@Composable
fun HandlePrependStatus(lazyPagingItems: LazyPagingItems<PostWithAuthorAndPhotos>) {
    when(lazyPagingItems.loadState.prepend){
        is LoadState.Loading -> {
            Row(modifier= Modifier
                .zIndex(.75f)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error-> {
            Text("Error de conexión...", Modifier.zIndex(.5f))
        }
        is LoadState.NotLoading -> {
        }
    }
}
@Composable
fun HandleAppendStatus(lazyPagingItems: LazyPagingItems<PostWithAuthorAndPhotos>) {
    when(lazyPagingItems.loadState.append){
        is LoadState.Loading -> {
            Row(modifier= Modifier
                .zIndex(.75f)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error-> {
            Text("Error de conexión...", Modifier.zIndex(.5f))
        }
        is LoadState.NotLoading -> {
        }
    }
}

