package social.ufind

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import social.ufind.UfindApplication.Companion.setToken
import social.ufind.UfindApplication.Companion.setUser
import social.ufind.navigation.OptionsRoutes
import social.ufind.data.datastore.DataStoreManager
import social.ufind.navigation.NavigationComponent
import social.ufind.ui.screen.main.MainUiState
import social.ufind.ui.theme.UfindTheme
import social.ufind.ui.screen.main.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        auth = Firebase.auth
        Firebase.initialize(this)

        setContent {

            val navController = rememberNavController()
            val viewModel: MainViewModel = viewModel<MainViewModel>(factory = MainViewModel.Factory)
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val startDestination = viewModel.startDestination.collectAsStateWithLifecycle()
            val uiState = viewModel.uiState.collectAsState().value

            LaunchedEffect(dataStoreManager.getUserData()) {
                lifecycleScope.launch {
                    val user = dataStoreManager.getUserData()
                    user.collect{
                        setUser(it)
                        setToken(it.token)
                        if(it.token == "")
                            viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
                        else {
                            viewModel.validateToken()
                        }
                    }
                }
            }
            LaunchedEffect(uiState) {
                handleState(uiState,viewModel)
            }

            UfindTheme(useDarkTheme = isSystemInDarkTheme()) {
                NavigationComponent(navHostController = navController, startDestination = startDestination.value)
            }
        }
    }
    private fun handleState(uiState: MainUiState, viewModel: MainViewModel) {
        when(uiState) {
            is MainUiState.Resume -> {}
            is MainUiState.ConnectionError -> {
                viewModel.updateStartDestination(OptionsRoutes.UserInterface.route)
            }
            is MainUiState.Success -> {
                viewModel.updateStartDestination(OptionsRoutes.UserInterface.route)
            }
            else -> {
                viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
            }
        }
        viewModel.resetState()
    }
}


