package com.juanjonathan.superapp.controllers

import com.juanjonathan.superapp.User
import org.springframework.data.repository.CrudRepository
import org.springframework.http.ResponseEntity

interface UserRepository : CrudRepository<User, Int> {
    fun findUserByUsername(username: String): User
    fun findUserByEmail(email: String): User
}