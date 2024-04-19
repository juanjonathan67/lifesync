package com.dicoding.lifesync.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.lifesync.data.local.entity.NoteEntity
import com.dicoding.lifesync.data.local.entity.TaskEntity
import com.dicoding.lifesync.data.local.entity.UserEntity
import com.dicoding.lifesync.data.local.entity.UserWithNoteEntity
import com.dicoding.lifesync.data.local.room.UserDao
import com.dicoding.lifesync.data.remote.response.NoteItem
import com.dicoding.lifesync.data.remote.response.TaskItem
import com.dicoding.lifesync.data.remote.response.UserResponse
import com.dicoding.lifesync.data.remote.retrofit.ApiService
import com.dicoding.lifesync.utils.AppExecutor
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Repository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val appExecutors: AppExecutor
) {
    private val _userResult: MutableLiveData<Result<UserWithNoteEntity>> by lazy { MutableLiveData<Result<UserWithNoteEntity>>() }
    private val userResult: LiveData<Result<UserWithNoteEntity>> = _userResult
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    private var userId: Int = 0

    fun login (
        usernameOrEmail: String,
        password: String
    ) : LiveData<Result<UserWithNoteEntity>> {
        _userResult.postValue(Result.Loading)
        val loginRequest = JsonObject().apply {
            addProperty("usernameOrEmail", usernameOrEmail)
            addProperty("password", password)
        }
        val client = apiService.login(loginRequest)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    if(userData != null) {
                        userId = userData.userId
                        appExecutors.diskIO.execute {
                            userDao.deleteUser()
                            userDao.deleteNote()
                            userDao.deleteTask()

                            val userEntity = UserEntity (
                                userId = userData.userId,
                                username = userData.username,
                                email = userData.email,
                                password = userData.password,
                            )
                            userDao.insertUser(userEntity)
                            for (note in userData.note) {
                                val noteEntity = NoteEntity (
                                    noteId = note.noteId,
                                    title = note.title,
                                    image = note.image,
                                    description = note.description,
                                    userId = userData.userId
                                )
                                userDao.insertNote(noteEntity)
                            }
                            _userResult.postValue(Result.Success(userDao.getUser()))
                        }
                    } else {
                        _userResult.postValue(Result.Error("Response is null"))
                    }
                } else {
                    _userResult.postValue(Result.Error(response.code().toString()))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _userResult.postValue(Result.Error(t.message.toString()))
            }
        })
        return userResult
    }

    fun register(
        username: String,
        email: String,
        password: String
    ) : LiveData<Result<UserWithNoteEntity>> {
        _userResult.postValue(Result.Loading)
        val registerRequest = JsonObject().apply {
            addProperty("username", username)
            addProperty("email", email)
            addProperty("password", password)
        }
        val client = apiService.register(registerRequest)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful) {
                    val userData = response.body()
                    if(userData != null) {
                        userId = userData.userId
                        appExecutors.diskIO.execute {
                            val userEntity = UserEntity (
                                userId = userData.userId,
                                username = userData.username,
                                email = userData.email,
                                password = userData.password
                            )
                            userDao.insertUser(userEntity)
                            _userResult.postValue(Result.Success(userDao.getUser()))
                        }
                    } else {
                        _userResult.postValue(Result.Error("Response is null"))
                    }
                } else {
                    _userResult.postValue(Result.Error(response.code().toString()))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _userResult.postValue(Result.Error(t.message.toString()))
            }
        })
        return userResult
    }

    fun createTask(
        title: String,
        imageUrl: String,
        description: String,
    ) : LiveData<Result<UserWithNoteEntity>> {
        _userResult.postValue(Result.Loading)

        val notesRequest =  JsonObject().apply {
            addProperty("title", title)
            addProperty("image", imageUrl)
            addProperty("description", description)
        }

        val client = apiService.createNote(notesRequest, userId)
        client.enqueue(object : Callback<NoteItem> {
            override fun onResponse(call: Call<NoteItem>, response: Response<NoteItem>) {
                if(response.isSuccessful) {
                    val newNote = response.body()
                    if(newNote != null) {
                        appExecutors.diskIO.execute {
                            val noteEntity = NoteEntity (
                                noteId = newNote.noteId,
                                title = newNote.title,
                                image = newNote.image,
                                description = newNote.description,
                                userId = userId
                            )
                            userDao.insertNote(noteEntity)
                            _userResult.postValue(Result.Success(userDao.getUser()))
                        }
                    } else {
                        _userResult.postValue(Result.Error("Response is null"))
                    }
                } else {
                    _userResult.postValue(Result.Error(response.code().toString()))
                }
            }

            override fun onFailure(call: Call<NoteItem>, t: Throwable) {
                _userResult.postValue(Result.Error(t.message.toString()))
            }
        })
        return userResult
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            appExecutor: AppExecutor
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userDao, appExecutor)
            }.also { instance = it }
    }
}