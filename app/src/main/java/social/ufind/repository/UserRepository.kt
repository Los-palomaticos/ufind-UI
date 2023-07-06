package social.ufind.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import retrofit2.HttpException
import social.ufind.data.datastore.DataStoreManager
import social.ufind.data.model.Payload
import social.ufind.data.model.UserModel
import social.ufind.network.ApiResponse
import social.ufind.network.dto.GeneralResponse
import social.ufind.network.dto.login.LoginRequest
import social.ufind.network.dto.login.LoginResponse
import social.ufind.network.dto.signup.SignUpRequest
import social.ufind.network.dto.signup.SignUpResponse
import social.ufind.network.service.UserService
import social.ufind.utils.JWT
import social.ufind.utils.SerializeErrorBody
import java.io.IOException
import java.net.ConnectException
import kotlin.Exception

class UserRepository(private val api: UserService, private val dataStoreManager: DataStoreManager) {
    private suspend fun saveUserToDataStore(token: String) {
        val jwtDecoded = JWT.decoded(token)

        val payload = Gson().fromJson(jwtDecoded, Payload::class.java)

        dataStoreManager.saveUserData(
            UserModel(
            id = payload.data.id,
            email = payload.data.email,
            username = payload.data.username,
            photo = payload.data.photo,
            token = token
        )
        )
    }
    suspend fun signup(username: String, email: String, password: String): ApiResponse<String> {
        return try {
            val credentials = SignUpRequest(username, email, password)
            val response = api.signup(credentials)
            createUserInFirebase(credentials)
            if (response.ok)
                ApiResponse.Success(response.message)
            else
                ApiResponse.ErrorWithMessage(response.errorMessages)
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(listOf("Error de conexión"))
        } catch(e: HttpException) {
            val errorResponse = SerializeErrorBody.getSerializedError(e, SignUpResponse::class.java)
            ApiResponse.ErrorWithMessage(errorResponse.errorMessages)
        }  catch(e: IOException) {
            ApiResponse.Error(e)
        }
    }
    suspend fun login(email: String, password: String): ApiResponse<String> {
        return try {
            val credentials = LoginRequest(email, password)
            val response = api.login(credentials)
            signInFirebase(credentials)
            if (response.ok) {
                saveUserToDataStore(response.token)
                ApiResponse.Success("Inicio de sesión exitoso")
            } else {
                ApiResponse.ErrorWithMessage(response.errorMessages)
            }
        } catch (e: ConnectException) {
            ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
        } catch(e: HttpException) {
            val errorResponse = SerializeErrorBody.getSerializedError(e, LoginResponse::class.java)

            ApiResponse.ErrorWithMessage(errorResponse.errorMessages)
        } catch(e:IOException) {
            ApiResponse.Error(e)
        }
    }
    suspend fun validateToken(): ApiResponse<Boolean> {
        return try {
            val response = api.validateToken()
            ApiResponse.Success(response.ok)
        } catch (e: Exception) {
            ApiResponse.Success(false)
        }
    }
    suspend fun logout() {
        dataStoreManager.clearDataStore()
    }
    suspend fun getInformationUser():ApiResponse<String>{
      return try {
          val response = api.getInformationUser()
          ApiResponse.Success(response.message)
      } catch(e: HttpException) {
          val errorResponse = SerializeErrorBody.getSerializedError(e, GeneralResponse::class.java)
          ApiResponse.ErrorWithMessage(errorResponse.errorMessages)
      } catch (e: FirebaseAuthInvalidCredentialsException) {
          ApiResponse.ErrorWithMessage(listOf("Invalid"))
      } catch (e: ConnectException) {
          ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
      } catch (e: Exception) {
          ApiResponse.Error(e)
      }
    }
    private fun signInFirebase(credentials: LoginRequest) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener { task ->
                try {
                    if (task.isSuccessful) {
                        Log.d("SignIn", "signInWithEmailAndPassword is successful")
                    } else {
                        Log.d(
                            "SignInError",
                            "signInWithEmailAndPassword: ${task.result.toString()}"
                        )
                    }
                } catch (e: Exception) {
                    Log.d("APP_TAG", "Error al iniciar sesión en firebase")
                }
            }
    }
    private fun createUserInFirebase(credentials: SignUpRequest) {

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(credentials.email, credentials.password)
                .addOnCompleteListener{
                    try {

                        Log.d(ContentValues.TAG, "Inside_OnCompleteListener")
                        Log.d(ContentValues.TAG, "isSuccessful = ${it.isSuccessful}")
                        val displayName = it.result.user?.email?.split("@")?.get(0)
                        createUserInDataBase(displayName)
                    } catch (e: Exception) {
                        Log.d("APP_TAG", "error al registrarse con firebase")
                    }
                }
                .addOnFailureListener(){
                    Log.d(ContentValues.TAG, "Inside_OnFailureListener")
                    Log.d(ContentValues.TAG, "Exception = ${it.message}")
                    Log.d(ContentValues.TAG, "Exception = ${it.localizedMessage}")
                }
    }

    private fun createUserInDataBase(displayName: String?) {

            val userId = FirebaseAuth.getInstance().currentUser?.uid
        val user = mutableMapOf<String, Any>()
        user["user_id"] = userId.toString()
        user["display_name"] = displayName.toString()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                try{
                    Log.d("BDFire", "Creado ${it.id}")
                } catch (e: Exception) {
                    Log.d("BDFire", "Error al crear usuario")
                }
            }
            .addOnFailureListener{
                Log.d("BDFire", "Ocurrió un error")
            }

    }
}