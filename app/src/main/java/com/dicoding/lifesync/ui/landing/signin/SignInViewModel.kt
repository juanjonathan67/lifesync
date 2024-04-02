package com.dicoding.lifesync.ui.landing.signin

import androidx.lifecycle.ViewModel
import com.dicoding.lifesync.data.Repository

class SignInViewModel (private val repository: Repository) : ViewModel() {

    fun login(usernameOrEmail: String = "", password: String = "") = repository.login(usernameOrEmail, password)
}