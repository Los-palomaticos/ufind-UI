package social.ufind.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoModel(
    @PrimaryKey
    @ColumnInfo(name = "photo") val photo: String,
    @ColumnInfo(name = "post_id") val post_id: Int
)
