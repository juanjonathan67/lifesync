package com.juanjonathan.superapp.controllers

import com.juanjonathan.superapp.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int>