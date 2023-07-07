package social.ufind.ui.screen.home.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.navigation.OptionsRoutes
import social.ufind.navigation.RouteNavigator
import social.ufind.navigation.UfindNavigator
import social.ufind.network.ApiResponse
import social.ufind.repository.PostRepository
import social.ufind.repository.UserRepository
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModel
import social.ufind.ui.screen.home.post.itempost.ItemPostViewModelMethods

//Aqui debo agregar  las funciones que ocuparemos
class UserProfileViewModel(
    val repository: UserRepository,
    private val postRepository: PostRepository,
    private val routeNavigator: RouteNavigator = UfindNavigator(),
    val itemPostViewModelMethods: ItemPostViewModelMethods = ItemPostViewModel(postRepository)
): ViewModel(),
    RouteNavigator by routeNavigator,
    ItemPostViewModelMethods by itemPostViewModelMethods
{
    private val _userPostsList = MutableStateFlow<PagingData<PostWithAuthorAndPhotos>>(PagingData.empty())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing

    val userPostsList
        get () = _userPostsList

    fun getInformationUser() {
        viewModelScope.launch {
            repository.getInformationUser()
        }
    }
    private fun changeRefreshState() {
        _isRefreshing.value = !_isRefreshing.value
    }
    fun refresh () {
        changeRefreshState()
        getUserPosts()
    }
    private fun getUserPosts() {
        viewModelScope.launch {
            val response = postRepository.getUserPosts(10)
            changeRefreshState()
            when(response) {
                is ApiResponse.Success -> {
                    response.data.cachedIn(viewModelScope).collect{
                        _userPostsList.value = it
                    }
                }
                is ApiResponse.ErrorWithMessage -> {
                    Log.d("APP_TAG", response.messages.toString())
                }
                is ApiResponse.Error -> {

                }
            }
        }
    }
    fun navigateToSettings() {
        routeNavigator.navigateToRoute(OptionsRoutes.SettingsScreen.route)
    }

    fun navigateToWallet() {
        routeNavigator.navigateToRoute(OptionsRoutes.WalletScreen.route)
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as social.ufind.UfindApplication
                UserProfileViewModel(app.userRepository, app.postRepository)
            }
        }
    }
}