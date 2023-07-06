package social.ufind.firebase

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import social.ufind.firebase.model.User

val authViewModel = AuthenticationViewModel()

class AuthenticationViewModel {

    fun registerWithEmailAndPass(
        email: String,
        password: String,
        username: String,
    ) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener {
            Log.d(ContentValues.TAG, "Inside_OnCompleteListener")
            Log.d(ContentValues.TAG, "isSuccessful = ${it.isSuccessful}")
            val user = User(
                Firebase.auth.currentUser?.email!!,
                username
            )
            firebaseViewModel.saveUser(user)

        }
    }

    fun loginWithEmailAndPass(
        email: String,
        password: String,
        navigate: (successful: Boolean) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener {
            Log.d("SignIn", "signInWithEmailAndPassword is successful")
        }
    }
}