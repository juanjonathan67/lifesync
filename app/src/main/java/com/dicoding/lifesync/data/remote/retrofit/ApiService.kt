package com.dicoding.lifesync.data.remote.retrofit

import com.dicoding.lifesync.data.remote.response.NoteItem
import com.dicoding.lifesync.data.remote.response.UserResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("users/login")
    fun login(
        @Body loginRequest: JsonObject
    ) : Call<UserResponse>

    @POST("users/register")
    fun register(
        @Body registerRequest: JsonObject
    ) : Call<UserResponse>

    @POST("notes/{id}")
    fun createNote(
        @Body notesRequest: JsonObject,
        @Path("id") id: Int
    ) : Call<NoteItem>
}