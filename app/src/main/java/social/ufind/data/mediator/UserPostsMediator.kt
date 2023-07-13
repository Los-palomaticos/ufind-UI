package social.ufind.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import social.ufind.data.UfindDatabase
import social.ufind.data.model.PhotoModel
import social.ufind.data.model.PostWithAuthorAndPhotos
import social.ufind.data.model.PublisherModel
import social.ufind.data.model.RemoteKey
import social.ufind.network.service.PostService
import social.ufind.repository.PostRepository
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserPostsMediator(
   val database: UfindDatabase,
   val postService: PostService
): RemoteMediator<Int, PostWithAuthorAndPhotos>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostWithAuthorAndPhotos>
    ): MediatorResult {
        val remoteKeyDao = database.remoteKeyDao()
        val postDao = database.postDao()
        val publisherDao = database.publisherDao()
        val photoDao = database.photoDao()
        try {
            val offset = when(loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getRemoteKeyByLabel("user_post")
                    }
                    remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            val userPostsListResponse = postService.getUserPosts(
                state.config.pageSize,
                offset
            )
            val publishers = userPostsListResponse.message.posts.map {
                it.publisher
            }
            database.withTransaction {
                postDao.insertAll(userPostsListResponse.message.posts)

                publisherDao.insertAll(publishers as List<PublisherModel>)

                userPostsListResponse.message.posts.forEach { post ->
                    post.photos?.forEach {
                        photoDao.insert(PhotoModel(photo = it, post_id = post.id))
                    }
                }
                remoteKeyDao.insert(
                    RemoteKey(
                        "saved_post",
                        userPostsListResponse.message.next,
                        userPostsListResponse.message.previous
                    )
                )
            }
            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }
}