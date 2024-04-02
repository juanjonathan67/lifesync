package com.dicoding.lifesync.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.lifesync.data.local.entity.NoteEntity
import com.dicoding.lifesync.data.local.entity.TaskEntity
import com.dicoding.lifesync.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, NoteEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getUserDbInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "User.db"
                ).build()
            }

    }
}