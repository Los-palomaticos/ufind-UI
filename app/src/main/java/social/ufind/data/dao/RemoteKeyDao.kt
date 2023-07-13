package social.ufind.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import social.ufind.data.model.RemoteKey

@Dao
interface RemoteKeyDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)
    @Query("SELECT * FROM remote_key WHERE label = :label")
    suspend fun getRemoteKeyByLabel(label: String): RemoteKey

    @Query("DELETE FROM remote_key WHERE label = :label")
    suspend fun deleteRemoteKeyByLabel(label: String)
}