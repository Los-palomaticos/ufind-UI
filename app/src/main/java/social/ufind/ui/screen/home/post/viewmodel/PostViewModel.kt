package social.ufind.ui.screen.home.post.viewmodel


import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import social.ufind.UfindApplication
import social.ufind.navigation.OptionsRoutes
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.repository.PostRepository

class PostViewModel(
    private val repository: PostRepository,
    private val routeNavigator: RouteNavigator = UfindNavigator()
): ViewModel(), RouteNavigator by routeNavigator, DefaultLifecycleObserver {
//    private var _posts = MutableStateFlow<PagingData<PostWithAuthorAndPhotos>>(PagingData.empty())
    private var _posts = MutableStateFlow<PagingData<PostWithAuthorAndPhotos>>(PagingData.empty())
    val listOfPosts: Flow<PagingData<PostWithAuthorAndPhotos>>
        get() = _posts
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d("APP_TAG", "CREATED")
//        viewModelScope.launch {
//            listOfPosts.collect {
//                Log.d("APP_TAG", it.)
//                if (it == PagingData.empty<PagingData<PostWithAuthorAndPhotos>>()) {
//                    getAll()
//                }
//            }
//        }
    }

    fun refresh() {
        Log.d("APP_TAG", "Refrescando")
        getAll()
    }
    private fun getAll() {
        viewModelScope.launch {
            when(val response = repository.getAll(10)) {
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
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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