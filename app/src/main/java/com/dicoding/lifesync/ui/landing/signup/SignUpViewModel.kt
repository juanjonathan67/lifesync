package com.dicoding.lifesync.ui.landing.signup

import androidx.lifecycle.ViewModel
import com.dicoding.lifesync.data.Repository

class SignUpViewModel (private val repository: Repository) : ViewModel() {

    fun register(username: String = "", email: String = "", password: String = "") = repository.register(username, email, password)
}