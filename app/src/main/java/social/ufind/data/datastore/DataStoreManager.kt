package social.ufind.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import social.ufind.data.model.UserModel

private const val USER_DATASTORE="USER"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager(private val context: Context) {
    suspend fun saveUserData(userModel: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[ID] = userModel.id.toString()
            preferences[EMAIL] = userModel.email
            preferences[TOKEN] = userModel.token
            preferences[PHOTO] = userModel.photo
            preferences[USERNAME] = userModel.username
            preferences[TUTORIAL] = "0"
        }
    }
    fun getUserData(): Flow<UserModel> = context.dataStore.data.map { user ->
        UserModel(
            id = user[ID]?.toInt() ?: 0,
            email = user[EMAIL] ?: "",
            token = user[TOKEN] ?: "",
            photo = user[PHOTO] ?: "",
            username = user[USERNAME] ?: ""
        )
    }
    suspend fun setPostTutorialTrue () {
        context.dataStore.edit { preferences ->
            preferences[TUTORIAL] = "1"
        }
    }

    fun getPostTutorial(): Flow<String> {
        return context.dataStore.data.map{
            it[TUTORIAL] ?: "0"
        }
    }
    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }
    companion object {
        val ID = stringPreferencesKey("ID")
        val EMAIL = stringPreferencesKey("EMAIL")
        val PHOTO = stringPreferencesKey("PHOTO")
        val TOKEN = stringPreferencesKey("TOKEN")
        val USERNAME = stringPreferencesKey("USERNAME")
        val POST_PHOTO_URI = stringPreferencesKey("POST_PHOTO_NAME")
        val TUTORIAL = stringPreferencesKey("TUTORIAL")
    }
}