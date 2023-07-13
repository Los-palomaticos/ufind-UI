package social.ufind.ui.screen.home.post.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.State
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.UfindApplication
import social.ufind.navigation.OptionsRoutes
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.repository.PostRepository
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModel
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModelMethods

class PostViewModel(
    private val repository: PostRepository,
    val itemPostMethods: ItemPostViewModelMethods = ItemPostViewModel(repository),
    private val routeNavigator: RouteNavigator = UfindNavigator(),
): ViewModel(), RouteNavigator by routeNavigator, ItemPostViewModelMethods by itemPostMethods, DefaultLifecycleObserver {
    private var _posts = MutableStateFlow<PagingData<PostWithAuthorAndPhotos>>(PagingData.empty())
    val listOfPosts: Flow<PagingData<PostWithAuthorAndPhotos>>
        get() = _posts
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing

    private val _isAlertDialogShown = MutableStateFlow<Boolean>(false)
    val isAlertDialogShown: StateFlow<Boolean>
        get() = _isAlertDialogShown

    private val pageSize = 5

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getPostTutorialState()
        Log.d("APP_TAG", "CREATED")
    }
    private fun changeRefreshState() {
        _isRefreshing.value = !_isRefreshing.value
    }
    fun refresh() {
        changeRefreshState()
        Log.d("APP_TAG", "Refrescando")
        getAll()
    }
    fun setPostTutorialTrue () {
        viewModelScope.launch {
            repository.setPostTutorialTrue()
        }
    }
    fun getPostTutorialState() {
        viewModelScope.launch {
            val postTutorialState = repository.getPostTutorial()
            postTutorialState.collect{
                _isAlertDialogShown.value = (it == "0")
            }
        }
    }
    private fun getAll() {
        viewModelScope.launch {
            val response = repository.getAll(pageSize)
            changeRefreshState()
            when(response) {
                is ApiResponse.Success -> {
                    response.data.cachedIn(viewModelScope).collect{
                        _posts.value = it
                    }
                }
                is ApiResponse.ErrorWithMessage -> {
                    Log.d("APP_TAG", response.messages.toString())
                }
                is ApiResponse.Error -> {
                    Log.d("APP_TAG", response.exception.toString())
                }
            }
        }
//        _isRefreshing.value = false
    }
    fun checkPermissions(context: Context, launcher: ManagedActivityResultLauncher<String, Boolean>) {
        val permission = android.Manifest.permission.CAMERA
        if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            navigateToAddPost()
        else
            launcher.launch(permission)
    }
    fun navigateToAddPost() {
        routeNavigator.navigateToRoute(OptionsRoutes.AddPostScreen.route)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                PostViewModel(app.postRepository)
            }
        }
    }
}