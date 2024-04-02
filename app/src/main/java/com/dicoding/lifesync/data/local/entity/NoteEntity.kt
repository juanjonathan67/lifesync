package com.dicoding.lifesync.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity (
    @ColumnInfo(name = "noteId")
    @PrimaryKey
    val noteId: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "image")
    val image: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "userId")
    val userId: Int
) : Parcelable