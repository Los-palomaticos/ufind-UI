package social.ufind.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import social.ufind.data.model.PhotoModel

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photoModel: PhotoModel)

    @Query("DELETE FROM photo")
    suspend fun clearAll()
}