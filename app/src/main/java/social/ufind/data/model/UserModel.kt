package social.ufind.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Data (
    val id: Int,
    val email: String,
    val username: String,
    val photo: String
)
data class Payload(
    val data: Data,
    val iat: Int,
    val exp: Int,
)
@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey
    @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="token") val token: String,
    @ColumnInfo(name="email") val email: String,
    @ColumnInfo(name="username") val username: String,
    @ColumnInfo(name="photo") val photo: String
)
