package com.dicoding.lifesync.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithNotes(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )

    val note: List<NoteEntity>
)
