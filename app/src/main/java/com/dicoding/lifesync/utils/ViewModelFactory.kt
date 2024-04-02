package com.dicoding.lifesync.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lifesync.data.Repository
import com.dicoding.lifesync.di.Injection
import com.dicoding.lifesync.ui.landing.signin.SignInViewModel
import com.dicoding.lifesync.ui.landing.signup.SignUpViewModel
import com.dicoding.lifesync.ui.main.tabs.notes.NotesViewModel
import com.dicoding.lifesync.ui.main.tabs.schedule.ScheduleViewModel

class ViewModelFactory private constructor (private val repository: Repository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        SignInViewModel::class.java -> SignInViewModel(repository)
        SignUpViewModel::class.java -> SignUpViewModel(repository)
        NotesViewModel::class.java -> NotesViewModel(repository)
        ScheduleViewModel::class.java -> ScheduleViewModel(repository)
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    } as T

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}