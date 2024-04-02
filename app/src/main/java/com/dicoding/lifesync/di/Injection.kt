package com.dicoding.lifesync.di

import android.content.Context
import com.dicoding.lifesync.data.Repository
import com.dicoding.lifesync.data.local.room.AppDatabase
import com.dicoding.lifesync.data.remote.retrofit.ApiConfig
import com.dicoding.lifesync.utils.AppExecutor

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val appDatabase = AppDatabase.getUserDbInstance(context)
        val dao = appDatabase.userDao()
        val appExecutors = AppExecutor()
        return Repository.getInstance(apiService, dao, appExecutors)
    }
}