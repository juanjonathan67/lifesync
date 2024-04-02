package com.dicoding.lifesync.data.local.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserWithNoteEntity(
    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val note: List<NoteEntity>
) : Parcelable
