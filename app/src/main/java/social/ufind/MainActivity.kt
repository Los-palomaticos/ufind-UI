package social.ufind


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
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
import com.ramcosta.composedestinations.DestinationsNavHost
import kotlinx.coroutines.launch
import social.ufind.UfindApplication.Companion.setToken
import social.ufind.data.OptionsRoutes
import social.ufind.data.datastore.DataStoreManager
import social.ufind.navigation.NavigationComponent
import social.ufind.viewmodel.MainViewModel



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

            LaunchedEffect(dataStoreManager.getUserData()) {
                lifecycleScope.launch {
                    val user = dataStoreManager.getUserData()
                    user.collect{
                        setToken(it.token)
                        if(it.token == "")
                            viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
                        else {
                            viewModel.validateToken()
                            if (!viewModel.isUserValid.value)
                                viewModel.updateStartDestination(OptionsRoutes.LogInOrSignUpOptions.route)
                            else
                                viewModel.updateStartDestination(OptionsRoutes.UserInterface.route)
                        }
                    }
                }
            }
            NavigationComponent(navHostController = navController, startDestination = startDestination.value)
        }
    }
}
