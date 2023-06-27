package org.ufind.ui.screen.home.post
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ufind.R
import org.ufind.data.BottomBarScreen
import org.ufind.data.model.PostModel
import org.ufind.navigation.NavRoute
import org.ufind.ui.screen.home.post.viewmodel.PostViewModel
import org.ufind.ui.screen.home.post.PostScreen.observeLifecycleEvents

object PostScreen: NavRoute<PostViewModel> {
    override val route: String
        get() = BottomBarScreen.Home.route
    @Composable
    override fun viewModel(): PostViewModel {
        val vm = viewModel<PostViewModel>(factory = PostViewModel.Factory)
        return vm
    }
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
        painter = painterResource(id = R.drawable.ic_ufind),
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
    viewModel.observeLifecycleEvents(lifecycle = lifecycle)
    val listPosts = viewModel.listOfPosts.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageHeader()
        Text(text = "Publicaciones", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Start).padding(0.dp, 16.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            PostList(listPosts.value)
            AddPostFloatingButton(viewModel = viewModel, Modifier.align(Alignment.BottomEnd))


        }
    }
}

@Composable
fun PostList(posts: List<PostModel>) {

    LazyColumn{
        items(posts){post ->
            ItemPost(post = post)
        }
    }
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
        modifier = modifier.padding(0.dp, 10.dp).size(64.dp),
        backgroundColor = colorResource(id = R.color.text_color)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "", tint = Color.White)

    }
}
