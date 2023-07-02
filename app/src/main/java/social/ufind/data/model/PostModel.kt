package social.ufind.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
@Entity(tableName = "post")
data class PostModel(
    @PrimaryKey
    @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="likes") val likes: Int,
    @ColumnInfo(name="title") val title: String,
    @ColumnInfo(name="description") val description: String,
    @ColumnInfo(name="location") val location: String? = "Universidad José Simeón Cañas",
    @ColumnInfo(name="locationDescription") val locationDescription: String?,
    @ColumnInfo(name="complete") val complete: Int,
    @ColumnInfo(name="reported") val reported: Int,
    @ColumnInfo(name="user_id") val user_id: Int,
    // Campos útiles para obtener la respuesta de la API
    @Ignore val publisher: PublisherModel?,
    @Ignore val photos: List<String>?
) {
    constructor(id: Int, likes: Int, title: String, description: String, location: String?, locationDescription: String?, complete: Int, reported: Int, user_id: Int) :
        this(id, likes, title, description, location, locationDescription, complete, reported, user_id, null, null)
}
