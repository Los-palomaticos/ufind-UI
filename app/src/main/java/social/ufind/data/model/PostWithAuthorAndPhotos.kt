package social.ufind.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class PostWithAuthorAndPhotos(
    @Embedded val post: PostModel,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id",
        entity = PublisherModel::class
    ) val publisher: PublisherModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "post_id",
        entity = PhotoModel::class,
//        projection = ["photo"]
    ) val photos: List<PhotoModel>
)
