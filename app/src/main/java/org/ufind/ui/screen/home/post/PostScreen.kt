package org.ufind.ui.screen.home.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ufind.R
import org.ufind.data.BottomBarScreen
import org.ufind.data.model.PostModel
import org.ufind.navigation.NavRoute
import org.ufind.ui.screen.home.post.viewmodel.PostViewModel

object PostScreen: NavRoute<PostViewModel> {
    override val route: String
        get() = BottomBarScreen.Home.route
    @Composable
    override fun viewModel(): PostViewModel = viewModel(factory = PostViewModel.Factory)
    @Composable
    override fun Content(viewModel: PostViewModel) {
        PostScreen(viewModel)
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
        color = Color(0xFF02092E)
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
@Composable
fun PostScreen(
    viewModel: PostViewModel,
) {
    viewModel.getAll()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 8.dp)) {
        PageHeader()
        Spacer(modifier = Modifier.size(32.dp))
        Text(text = "Publicaciones", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)) {
            PostList(viewModel.listOfPosts)
            AddPostFloatingButton(modifier = Modifier.align(Alignment.BottomEnd)) {
                viewModel.navigateToAddPost()
            }
        }
    }
}

@Composable
fun PostList(posts: List<PostModel>) {

    LazyColumn() {
        items(posts){post ->
            ItemPost(post = post)
        }
    }
}

@Composable
fun PostImage(size: Int) {
    Image(
        imageVector = Icons.Filled.AddAPhoto,
        contentDescription = "",
        modifier = Modifier.size(size.dp),
        alignment = Alignment.Center
    )

}

@Composable
fun BottomBarPostIcons() {
    Row(
        Modifier
            .fillMaxWidth()
    ) {
        Image(
            imageVector = Icons.Filled.Comment,
            contentDescription = "",
            Modifier.padding(16.dp, 0.dp)
        )

        Image(
            imageVector = Icons.Filled.BookmarkBorder,
            contentDescription = "",
            Modifier.padding(16.dp, 0.dp)
        )
        Image(
            imageVector = Icons.Filled.Share, contentDescription = "", Modifier.padding(16.dp, 0.dp)
        )


    }
}

@Composable
fun AddPostFloatingButton(modifier: Modifier,onClickAddPostScreen: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClickAddPostScreen,
        modifier = modifier.padding(0.dp, 42.dp),
        containerColor = colorResource(id = R.color.text_color)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "", tint = Color.White)

    }
}
