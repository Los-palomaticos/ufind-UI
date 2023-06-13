package org.ufind.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.ufind.data.model.UserModel

@Dao
interface UserDao {
    @Query("SELECT token FROM user")
    suspend fun getToken() : String

    @Query("SELECT * FROM user")
    suspend fun getUser(): UserModel

    @Transaction
    @Insert
    suspend fun insert(user: UserModel)

}