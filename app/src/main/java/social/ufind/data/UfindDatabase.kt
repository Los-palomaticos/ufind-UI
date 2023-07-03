package social.ufind.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import social.ufind.data.dao.PhotoDao
import social.ufind.data.dao.PostDao
import social.ufind.data.dao.PublisherDao
import social.ufind.data.dao.RemoteKeyDao
import social.ufind.data.dao.UserDao
import social.ufind.data.model.PhotoModel
import social.ufind.data.model.PostModel
import social.ufind.data.model.PublisherModel
import social.ufind.data.model.RemoteKey
import social.ufind.data.model.UserModel

@Database(
    entities=[
        UserModel::class,
        PostModel::class,
        PhotoModel::class,
        PublisherModel::class,
        RemoteKey::class
    ], version=5)
abstract class UfindDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun publisherDao(): PublisherDao
    abstract fun photoDao(): PhotoDao
    companion object {
        @Volatile
        private var INSTANCE: UfindDatabase? = null
        fun getInstance(app: Application): UfindDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    app.applicationContext,
                    UfindDatabase::class.java,
                    "ufind"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}