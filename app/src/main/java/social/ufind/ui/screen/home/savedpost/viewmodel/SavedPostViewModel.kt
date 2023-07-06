package social.ufind.ui.screen.home.savedpost.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.UfindApplication
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.repository.PostRepository
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModel
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModelMethods

class SavedPostViewModel(
    val repository: PostRepository,
    private val routeNavigator: RouteNavigator = UfindNavigator(),
    val itemPostMethods: ItemPostViewModelMethods = ItemPostViewModel(repository)
): ViewModel(), RouteNavigator by routeNavigator, ItemPostViewModelMethods by itemPostMethods{
    private val _postsList = MutableStateFlow<PagingData<PostWithAuthorAndPhotos>>(PagingData.empty())
    val postList
        get() = _postsList

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing


    fun refresh() {
        getSavedPosts()
    }
    private fun getSavedPosts() {
        viewModelScope.launch {
            when(val response = repository.getSavedPosts(10)) {
                is ApiResponse.Success -> {
                    response.data.cachedIn(viewModelScope).collect{
                        _postsList.value = it
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

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as UfindApplication
                SavedPostViewModel(app.postRepository)
            }
        }
    }
}