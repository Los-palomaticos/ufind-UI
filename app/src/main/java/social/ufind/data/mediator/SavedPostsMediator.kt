package social.ufind.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import social.ufind.data.UfindDatabase
import social.ufind.data.dao.PostDao
import social.ufind.data.model.PhotoModel
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.data.model.PublisherModel
import social.ufind.data.model.RemoteKey
import social.ufind.network.service.PostService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SavedPostsMediator(
    private val database: UfindDatabase,
    private val postService: PostService
): RemoteMediator<Int, PostWithAuthorAndPhotos>() {
    private val postDao: PostDao = database.postDao()
    private val publisherDao = database.publisherDao()
    private val photoDao = database.photoDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostWithAuthorAndPhotos>
    ): MediatorResult {
        try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getRemoteKeyByLabel("saved_post")
                    }
                    remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            val savedPostsListResponse = postService.getSavedPosts(
                state.config.pageSize,
                offset
            )
            val publishers = savedPostsListResponse.message.posts.map {
                it.publisher
            }

            database.withTransaction {
                postDao.insertAll(savedPostsListResponse.message.posts)

                publisherDao.insertAll(publishers as List<PublisherModel>)

                savedPostsListResponse.message.posts.forEach { post ->
                    post.photos?.forEach {
                        photoDao.insert(PhotoModel(photo = it, post_id = post.id))
                    }
                }
                remoteKeyDao.insert(RemoteKey(
                    "saved_post",
                    savedPostsListResponse.message.next,
                    savedPostsListResponse.message.previous
                ))
            }
            return MediatorResult.Success(
                endOfPaginationReached = savedPostsListResponse.message.next == null
            )

        } catch (e: HttpException) {
            return RemoteMediator.MediatorResult.Error(e)
        } catch (e: IOException) {
            return RemoteMediator.MediatorResult.Error(e)
        }
    }
}