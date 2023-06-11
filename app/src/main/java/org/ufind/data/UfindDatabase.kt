package org.ufind.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.ufind.data.dao.UserDao
import org.ufind.data.model.UserModel

@Database(entities=[UserModel::class], version=2)
abstract class UfindDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
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