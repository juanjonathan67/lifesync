package com.dicoding.lifesync.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserResponse(

	@field:SerializedName("note")
	val note: List<NoteItem>,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("task")
	val task: List<TaskItem>,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable

@Parcelize
data class TaskItem(

	@field:SerializedName("startDateTime")
	val startDateTime: String,

	@field:SerializedName("color")
	val color: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("endDateTime")
	val endDateTime: String,

	@field:SerializedName("taskId")
	val taskId: Int
) : Parcelable

@Parcelize
data class NoteItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("noteId")
	val noteId: Int,

	@field:SerializedName("title")
	val title: String
) : Parcelable
