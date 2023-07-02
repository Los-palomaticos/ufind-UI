package social.ufind.repository

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
import java.lang.Exception
import java.net.ConnectException

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
            val response = api.signup(SignUpRequest(username, email, password))
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
    suspend fun login(loginRequest: LoginRequest): ApiResponse<String> {
        return try {
            val response = api.login(loginRequest)
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
      } catch (e: ConnectException) {
          ApiResponse.ErrorWithMessage(ApiResponse.connectionErrorMessage)
      } catch (e: Exception) {
          ApiResponse.Error(e)
      }
    }

}