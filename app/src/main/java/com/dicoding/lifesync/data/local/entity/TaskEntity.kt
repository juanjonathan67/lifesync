package com.dicoding.lifesync.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "tasks")
data class TaskEntity (
    @ColumnInfo(name = "taskId")
    @PrimaryKey
    val taskId: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "startDateTime")
    val startDateTime: String = "",

    @ColumnInfo(name = "endDateTime")
    val endDateTime: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "color")
    val color: String= "Red",

    @ColumnInfo(name = "userId")
    val userId: Int
) : Parcelable