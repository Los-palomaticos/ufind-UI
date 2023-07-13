package social.ufind.ui.screen.home.post
import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import org.ufind.R
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.NavRoute
import social.ufind.navigation.BottomBarScreen
import social.ufind.ui.screen.home.post.PostScreen.observeLifecycleEvents
import social.ufind.ui.screen.home.post.itempost.ItemPost
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

    // Estado para realizar un seguimiento de si el AlertDialog ya se ha mostrado
    var isAlertDialogShown = viewModel.isAlertDialogShown.collectAsStateWithLifecycle().value

    HandleItemUiState(viewModel)
    if (isAlertDialogShown) {
        AlertDialog(
            onDismissRequest = {
                isAlertDialogShown = false
            },
            title = {
                Text(text = "¿Cómo usar UFind?", textAlign = TextAlign.Center, modifier=Modifier.fillMaxWidth())
            },
            text = {
                Column {
                    Text(
                        text = "Antes de empezar a usar UFind, por favor ten en cuenta las siguientes recomendaciones:",
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Justify
                    )
                    WarningParagraph(text = "No compartas desnudos o contenido explícito", icon = Icons.Default.Warning)
                    WarningParagraph(text = "Realiza publicaciones de acuerdo a la filosofía de la aplicación", icon = Icons.Default.Warning)
                    WarningParagraph(text = "Evita caer en el spam o información falsa", icon = Icons.Default.Warning)
                    WarningParagraph(text = "No compartas información sensible o personal", icon = Icons.Default.Warning)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setPostTutorialTrue()
                        isAlertDialogShown = false
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.text_color))
                ) {
                    Text(text = "Aceptar")
                }
            },
            modifier = Modifier.width(300.dp)
        )
    }

    SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.refresh() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageHeader()
            Text(text = "Publicaciones", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 16.dp),
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                PostList(
                    lazyPagingItems = lazyPagingItems,
                    viewModel=viewModel
                )
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
fun WarningParagraph(text: String, icon: ImageVector) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = "Warning Icon",
            tint = Color.Red,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
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
    val scrollState = rememberLazyListState()

    if (lazyPagingItems.itemCount == 0) {
        viewModel.refresh()
    }

    LazyColumn(
        modifier=Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState
    ){
        item {
            HandleRefreshStatus(lazyPagingItems)
            HandlePrependStatus(lazyPagingItems = lazyPagingItems)
        }
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.post.id }
        ){index ->

            ItemPost(post = lazyPagingItems[index], viewModel= viewModel, modifier = Modifier.animateItemPlacement())

        }
        item {
            HandleAppendStatus(lazyPagingItems = lazyPagingItems)
        }
    }
}

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
