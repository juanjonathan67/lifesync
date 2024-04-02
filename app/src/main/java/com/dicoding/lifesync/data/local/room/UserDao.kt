package com.dicoding.lifesync.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dicoding.lifesync.data.local.entity.NoteEntity
import com.dicoding.lifesync.data.local.entity.UserEntity
import com.dicoding.lifesync.data.local.entity.UserWithNoteEntity

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM users")
    fun getUser(): UserWithNoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg users: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(vararg note: NoteEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Query("DELETE FROM users")
    fun deleteUser()
}