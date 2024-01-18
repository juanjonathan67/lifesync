package com.juanjonathan.superapp.user

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    fun findUserByUsername(username: String): User
    fun findUserByEmail(email: String): User
    fun findUserByUsernameOrEmail(username: String, email: String): User
}