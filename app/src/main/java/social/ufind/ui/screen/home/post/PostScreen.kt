package social.ufind.ui.screen.home.post
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.ufind.R
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.NavRoute
import social.ufind.navigation.BottomBarScreen
import social.ufind.ui.screen.home.post.PostScreen.observeLifecycleEvents
import social.ufind.ui.screen.home.post.itempost.ItemPost
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModel
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModelMethods
import social.ufind.ui.screen.home.post.itempost.ItemUiState
import social.ufind.ui.screen.home.post.viewmodel.PostViewModel

object PostScreen: NavRoute<PostViewModel> {
    override val route: String
        get() = BottomBarScreen.Home.route
    @Composable
    override fun viewModel(): PostViewModel = viewModel<PostViewModel>(factory = PostViewModel.Factory)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content(viewModel: PostViewModel) {
        PostScreen(viewModel)
    }
    @Composable
    fun <viewModel: LifecycleObserver> viewModel.observeLifecycleEvents(lifecycle: Lifecycle) {
        DisposableEffect(lifecycle) {
            lifecycle.addObserver(this@observeLifecycleEvents)
            onDispose {
                lifecycle.removeObserver(this@observeLifecycleEvents)
            }
        }
    }
}
@Composable
fun PageHeader() {
    ImageLogo(75, modifier = Modifier)
    PageHeaderLineDivider()
}

@Composable
fun PageHeaderLineDivider() {
    Divider(
        Modifier
            .height(1.dp)
            .fillMaxWidth(),
        color = colorResource(id = R.color.grey01)
    )
}

@Composable
fun ImageLogo(size: Int, modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ufind_logo),
        contentDescription = "Logo",
        modifier = modifier.size(size.dp),
        contentScale = ContentScale.Fit
    )
}

//@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PostScreen(
    viewModel: PostViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle().value
    viewModel.observeLifecycleEvents(lifecycle = lifecycle)
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    val lazyPagingItems = viewModel.listOfPosts.collectAsLazyPagingItems()

    SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.refresh() }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageHeader()
            Text(text = "Publicaciones", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier
                .align(Alignment.Start)
                .padding(0.dp, 16.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    HandleRefreshStatus(lazyPagingItems)
                    PostList(
                        lazyPagingItems = lazyPagingItems,
                        viewModel=viewModel
                    )
                    HandleItemUiState(viewModel)
                }
                AddPostFloatingButton(viewModel = viewModel, Modifier.align(Alignment.BottomEnd))
            }
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
            Text("Error de conexión", Modifier.zIndex(.5f), color = MaterialTheme.colors.onError)
        }
        is LoadState.NotLoading -> {
            if (lazyPagingItems.itemCount == 0) {
                Text("No se encontraron publicaciones")
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
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostList(lazyPagingItems: LazyPagingItems<PostWithAuthorAndPhotos>, viewModel: PostViewModel){
    val scrollState = rememberLazyListState(0, 10)
//    val coroutineScope = rememberCoroutineScope()


//    LaunchedEffect(lazyPagingItems.loadState.mediator?.refresh) {
//        if (scrollState.canScrollBackward) {
//            coroutineScope.launch {
//                scrollState.animateScrollToItem(0)
//            }
//        }
//    }

    if (lazyPagingItems.itemCount == 0) {
        viewModel.refresh()
    }
    HandlePrependStatus(lazyPagingItems = lazyPagingItems)
    LazyColumn(
        state = scrollState
    ){
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.post.id }
        ){index ->
            ItemPost(post = lazyPagingItems[index], viewModel= viewModel, modifier = Modifier.animateItemPlacement())
        }
    }
    HandleAppendStatus(lazyPagingItems = lazyPagingItems)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddPostFloatingButton(
    viewModel: PostViewModel,
    modifier: Modifier
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted)
            viewModel.navigateToAddPost()
        else
            Toast.makeText(context, "UFind necesita acceder a la cámara para poder realizar una publicación", Toast.LENGTH_LONG).show()
    }
    FloatingActionButton(
        onClick = {
            viewModel.checkPermissions(context, launcher)
        },
        modifier = modifier
            .padding(0.dp, 10.dp)
            .size(64.dp),
        backgroundColor = colorResource(id = R.color.text_color)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "", tint = Color.White)

    }
}
