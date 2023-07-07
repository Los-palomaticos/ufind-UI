package social.ufind.firebase

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import social.ufind.data.model.Payload
import social.ufind.firebase.model.User
import social.ufind.network.dto.signup.SignUpRequest
import social.ufind.utils.JWT

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
        token: String
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener {
            Log.d("SignIn", "signInWithEmailAndPassword is successful")
        }.addOnFailureListener {
            try {
                throw  it
            } catch (e: FirebaseAuthInvalidUserException) {
                Log.d("APP_TAG", "Usario sin cuenta en firebase, creando cuenta en firebase...")
                val jwtDecoded = JWT.decoded(token)
                val payload = Gson().fromJson(jwtDecoded, Payload::class.java)

                registerWithEmailAndPass(email, password, payload.data.username)
            } catch (e: Exception) {
                Log.d("APP_TAG", e.toString())
            }
        }
    }
}