package social.ufind.ui.screen.home.user

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.ufind.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import social.ufind.UfindApplication
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.data.model.UserModel
import social.ufind.navigation.BottomBarScreen
import social.ufind.navigation.NavRoute
import social.ufind.ui.screen.home.post.itempost.ItemPost
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModelMethods
import social.ufind.ui.screen.home.post.itempost.ItemUiState
import social.ufind.ui.screen.home.user.viewmodel.UserProfileViewModel
import social.ufind.ui.screen.home.user.settings.ProfileGoToButtons


object UserProfileScreen: NavRoute<UserProfileViewModel> {
    override val route: String
        get() = BottomBarScreen.Profile.route
    @Composable
    override fun viewModel(): UserProfileViewModel = viewModel(factory = UserProfileViewModel.Factory)
    @Composable
    override fun Content(viewModel: UserProfileViewModel) {
        UserProfileScreen(viewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel
) {
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle().value
    val user = UfindApplication.getUser()
    val lazyPagingItems = viewModel.userPostsList.collectAsLazyPagingItems()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.refresh() }) {
        // Iniciar una columna con scroll y la informaci'on de usuario
        val scrollState = rememberLazyListState(0, 10)
        if (lazyPagingItems.itemCount == 0) {
            viewModel.refresh()
        }
        HandleItemUiState(viewModel)
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                ProfileHeader(user, viewModel)
                Text(text = "Publicaciones", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(0.dp, 16.dp),
                    textAlign = TextAlign.Center
                )
                HandlePrependStatus(lazyPagingItems = lazyPagingItems)
                HandleRefreshStatus(lazyPagingItems = lazyPagingItems)
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
}
@Composable
fun ProfileHeader(
    user: UserModel,
    viewModel: UserProfileViewModel,
) {
    Row(modifier = Modifier.padding(horizontal = 0.dp, vertical = 16.dp)){
        UserInfo(modifier= Modifier
            .weight(11f)
            .fillMaxWidth(), user = user)
        ProfileGoToButtons(
            modifier = Modifier.weight(1f),
            { viewModel.navigateToSettings() },
            { viewModel.navigateToWallet() }
        )

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserInfo(modifier: Modifier = Modifier, user: UserModel) {
    Row(modifier = modifier) {
        // Image
        GlideImage(
            model = user.photo, // Replace with your image resource
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        ) {
            it.error(R.drawable.no_image)
                .thumbnail(
                    it.clone()
                        .load(R.drawable.default_image_loading)
                )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Column
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = user.username, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Institución")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Residencia")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = user.email)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "10000 UPoints")

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
                Text("No tienes publicaciones", Modifier.zIndex(.5f))
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