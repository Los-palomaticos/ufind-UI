package social.ufind.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import social.ufind.data.model.PublisherModel

@Dao
interface PublisherDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(publishers: List<PublisherModel>)

    @Query ("DELETE FROM publisher")
    suspend fun clearAll()
}