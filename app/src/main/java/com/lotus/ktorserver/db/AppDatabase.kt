package com.lotus.ktorserver.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lotus.ktorserver.dao.UrunDao
import com.lotus.ktorserver.models.Urun

@Database(entities = [Urun::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun urunDao(): UrunDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "urun_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}