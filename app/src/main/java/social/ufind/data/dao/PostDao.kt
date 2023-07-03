package social.ufind.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import social.ufind.data.model.PostModel
import social.ufind.data.model.PostWithAuthorAndPhotos

@Dao
interface PostDao {
    @Query("SELECT * FROM post ORDER BY id DESC")
    fun getAll() : PagingSource<Int, PostWithAuthorAndPhotos>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(post: List<PostModel>)

    @Query ("DELETE FROM post")
    suspend fun clearAll()
}