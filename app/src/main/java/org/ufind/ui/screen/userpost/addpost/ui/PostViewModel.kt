package org.ufind.ui.screen.userpost.addpost.ui


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import org.ufind.ui.screen.userpost.addpost.ui.model.PostModel
import javax.inject.Inject

class PostViewModel @Inject constructor(): ViewModel() {
    private val _post = mutableStateListOf<PostModel>()
    val post:List<PostModel> = _post

    fun onPostCreated(title: String, description: String ) {
        _post.add(PostModel(title = title, description = description))
    }
}