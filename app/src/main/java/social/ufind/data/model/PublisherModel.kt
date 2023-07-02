package social.ufind.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "publisher")
data class PublisherModel(
    @PrimaryKey
    @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="username") val username: String,
    @ColumnInfo(name="reported") val reported: Int,
    @ColumnInfo(name="email") val email: String,
    @ColumnInfo(name="banned") val banned: Boolean
)
