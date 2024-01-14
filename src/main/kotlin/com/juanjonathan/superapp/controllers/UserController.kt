package com.juanjonathan.superapp.controllers

import com.juanjonathan.superapp.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController (@Autowired private val userRepository: UserRepository){

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
    val savedUser = userRepository.save(user)
    return ResponseEntity(savedUser, HttpStatus.CREATED)
  }

  /**
   * Get a user by its id
   * @return Responds with either the user or not found status
   */
  @GetMapping("/{id}")
  fun getUserById(@PathVariable("id") userId: Int): ResponseEntity<User>{
    val user = userRepository.findById(userId).orElse(null)

    if (user == null) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    return ResponseEntity(user, HttpStatus.OK)
  }

  /**
   * Update a user's credentials
   * @return Responds with either the updated user or not found status
   */
  @PutMapping("/{id}")
  fun updateUserById(@PathVariable("id") userId: Int, @RequestBody user: User): ResponseEntity<User> {
    val existingUser = userRepository.findById(userId).orElse(null)

    if (existingUser == null) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    val updatedUser = existingUser.copy(userName = user.userName, email = user.email)
    userRepository.save(updatedUser)
    return ResponseEntity(updatedUser, HttpStatus.OK)
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
