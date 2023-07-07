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

    @Query("SELECT * FROM post WHERE isSaved = 1 ORDER BY id DESC")
    fun getSavedPosts(): PagingSource<Int, PostWithAuthorAndPhotos>
    @Query("SELECT * FROM post WHERE user_id = :id ORDER BY id DESC")
    fun getUserPosts(id: Int): PagingSource<Int, PostWithAuthorAndPhotos>
    @Query("DELETE FROM post WHERE isSaved = 1")
    suspend fun clearSaved()

    @Query("UPDATE post SET isSaved = 1 WHERE id=:id")
    suspend fun savePost(id: Int)

    @Query("UPDATE post SET isSaved = 0 WHERE id=:id")
    suspend fun deleteSavedPost(id: Int)
}