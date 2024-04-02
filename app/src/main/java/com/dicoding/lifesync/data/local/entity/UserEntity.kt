package com.dicoding.lifesync.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class UserEntity (
    @ColumnInfo(name = "userId")
    @PrimaryKey
    val userId: Int = 0,

    @ColumnInfo(name = "username")
    val username: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "password")
    val password: String = "",
) : Parcelable