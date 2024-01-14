package com.juanjonathan.superapp.controllers

import com.juanjonathan.superapp.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.text.Regex
import java.math.BigInteger
import java.security.MessageDigest

@RestController
@RequestMapping("/api/users")
class UserController (@Autowired private val userRepository: UserRepository){
  private final val regexEmail = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
  private final val regexPassword = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&+=;'])(?=\\S+\$).{8,}\$")
  private final val md = MessageDigest.getInstance("MD5")

  /**
   * Get all users from database
   * @return List of all users
   */
  @GetMapping("")
  fun getAllUsers(): List<User> {
    return userRepository.findAll().toList()
  }

  /**
   * Create a user and save it to database
   * @return Respond with status
   */
  @PostMapping("")
  fun createUser(@RequestBody user: User): ResponseEntity<User>{
    if(!regexPassword.matches(user.password) || !regexEmail.matches(user.email)){
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }

    try {
      val hashedUser = User(
              user.userId,
              user.username,
              user.email,
              BigInteger(1, md.digest(user.password.toByteArray())).toString(16).padStart(32, '0')
      )
      val savedUser = userRepository.save(hashedUser)
      return ResponseEntity(savedUser, HttpStatus.CREATED)
    } catch (error: Exception) {
      return ResponseEntity(HttpStatus.CONFLICT)
    }
  }

  /**
   * Get a user by its id
   * @return Responds with either the user or not found status
   */
  @GetMapping("/{id}")
  fun findUserById(@PathVariable("id") userId: Int): ResponseEntity<User>{
    val user = userRepository.findById(userId).orElse(null)

    if (user == null) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    return ResponseEntity(user, HttpStatus.OK)
  }

  @GetMapping("/findByEmail")
  fun findUserByEmail(@RequestParam(name = "email") email: String): ResponseEntity<User> {
    try {
      val user = userRepository.findUserByEmail(email)
      return ResponseEntity(user, HttpStatus.OK)
    } catch (error : Exception) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping("/findByUsername")
  fun findUserByUsername(@RequestParam(name = "username") username: String): ResponseEntity<User>{
    try {
      val user = userRepository.findUserByEmail(username)
      return ResponseEntity(user, HttpStatus.OK)
    } catch (error : Exception) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  /**
   * Update a user's credentials
   * @return Responds with either the updated user or not found status
   */
  @PutMapping("/{id}")
  fun updateUserById(@PathVariable("id") userId: Int, @RequestBody user: User): ResponseEntity<User> {
    if(!regexPassword.matches(user.password) || !regexEmail.matches(user.email)){
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }

    val existingUser = userRepository.findById(userId).orElse(null)

    if (existingUser == null) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    try {
      val updatedUser = existingUser.copy(
              username = user.username,
              email = user.email,
              password = BigInteger(1, md.digest(user.password.toByteArray())).toString(16).padStart(32, '0')
      )
      userRepository.save(updatedUser)
      return ResponseEntity(updatedUser, HttpStatus.OK)
    } catch (error: Exception) {
      return ResponseEntity(HttpStatus.CONFLICT)
    }
  }

  /**
   * Delete a user
   * @return Responds with status
   */
  @DeleteMapping("/{id}")
  fun deleteUserById(@PathVariable("id") userId: Int): ResponseEntity<User>{
    if (!userRepository.existsById(userId)){
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    userRepository.deleteById(userId)
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }
}
