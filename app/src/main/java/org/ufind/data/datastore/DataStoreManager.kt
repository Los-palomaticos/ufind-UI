package org.ufind.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ufind.R
import org.ufind.data.model.UserModel

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
    suspend fun savePostPhotoUri (name: String) {
        context.dataStore.edit {
            it[POST_PHOTO_URI] = name
        }
    }
    fun getPostPhotoUri(): Flow<String> = context.dataStore.data.map{
        it[POST_PHOTO_URI]?:""
    }
    suspend fun clearPostPhotoUri() {
        context.dataStore.edit {
            it[POST_PHOTO_URI] = ""
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
    }
}