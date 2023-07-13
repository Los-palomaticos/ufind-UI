package social.ufind.data.mediator

import android.util.Log
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
import social.ufind.network.dto.GeneralResponse
import social.ufind.network.service.PostService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostMediator(
    private val database: UfindDatabase,
    private val postService: PostService
): RemoteMediator<Int, PostWithAuthorAndPhotos>() {
    private val remoteKeyDao = database.remoteKeyDao()
    private val postDao = database.postDao()
    private val publisherDao = database.publisherDao()
    private val photoDao = database.photoDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostWithAuthorAndPhotos>
    ): MediatorResult {
        try {
            val offset = when (loadType) {
                LoadType.REFRESH -> {
                    0
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getRemoteKeyByLabel("post")
                    }
                    if (remoteKey.nextKey == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                    remoteKey.nextKey
                }
            }
            val postsListResponse = postService.getAll(
                state.config.pageSize,
                offset!!
            )

            val publishers = postsListResponse.message.posts.map {
                it.publisher
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postDao.clearAll()
                    publisherDao.clearAll()
                    photoDao.clearAll()
                    remoteKeyDao.getRemoteKeyByLabel("post")
                }
                postDao.insertAll(postsListResponse.message.posts)
                publisherDao.insertAll(publishers as List<PublisherModel>)
                postsListResponse.message.posts.forEach { post ->
                    post.photos?.forEach {
                        photoDao.insert(PhotoModel(photo = it, post_id = post.id))
                    }
                }
                remoteKeyDao.insert(RemoteKey(
                    "post",
                    postsListResponse.message.next,
                    postsListResponse.message.previous
                ))
            }
            return MediatorResult.Success(
                endOfPaginationReached = postsListResponse.message.next == null,
            )
        }catch (e: HttpException) {
            return RemoteMediator.MediatorResult.Error(e)
        } catch (e: IOException) {
            return RemoteMediator.MediatorResult.Error(e)
        }
    }
}