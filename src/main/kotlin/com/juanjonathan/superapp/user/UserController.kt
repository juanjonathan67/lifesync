package com.juanjonathan.superapp.user

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
   * Find a user by its id
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

  /**
   * Find a user by its email
   * @return Responds with either the user or not found status
   */
  @PostMapping("/findByEmail")
  fun findUserByEmail(@RequestBody searchData: SearchData): ResponseEntity<User> {
    try {
      if(searchData.email != null){
        val user = userRepository.findUserByEmail(searchData.email)
        return ResponseEntity(user, HttpStatus.OK)
      }
      return ResponseEntity(HttpStatus.NOT_FOUND)
    } catch (error : Exception) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  /**
   * Find a user by its username
   * @return Responds with either the user or not found status
   */
  @PostMapping("/findByUsername")
  fun findUserByUsername(@RequestBody searchData: SearchData): ResponseEntity<User>{
    try {
      if(searchData.username != null){
        val user = userRepository.findUserByUsername(searchData.username)
        return ResponseEntity(user, HttpStatus.OK)
      }
      return ResponseEntity(HttpStatus.NOT_FOUND)
    } catch (error : Exception) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  /**
   * Create a user and save it to database
   * @return Respond with successfully saved user or error status
   */
  @PostMapping("/register")
  fun register(@RequestBody user: User): ResponseEntity<User>{
    if(!regexPassword.matches(user.password) || !regexEmail.matches(user.email)){
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }

    try {
      val hashedUser = User(
        userId = user.userId,
        username = user.username,
        email = user.email,
        password = BigInteger(1, md.digest(user.password.toByteArray())).toString(16).padStart(32, '0'),
        task = emptyList(),
        note = emptyList()
      )
      val savedUser = userRepository.save(hashedUser)
      return ResponseEntity(savedUser, HttpStatus.CREATED)
    } catch (error: Exception) {
      return ResponseEntity(HttpStatus.CONFLICT)
    }
  }

  /**
   * Login function either by username or email and password
   * @return Responds with the user if successful, unauthorized if wrong password, not found if user doesn't exist
   */
  @PostMapping("/login")
  fun login(@RequestBody loginCredentials: LoginCredentials): ResponseEntity<User>{
    try {

      val user = userRepository.findUserByUsernameOrEmail(loginCredentials.usernameOrEmail, loginCredentials.usernameOrEmail)
      val inputPassword = BigInteger(1, md.digest(loginCredentials.password.toByteArray())).toString(16).padStart(32, '0')

      if(user.password == inputPassword){
        return ResponseEntity(user, HttpStatus.OK)
      }

      return ResponseEntity(HttpStatus.UNAUTHORIZED)

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
        password = BigInteger(1, md.digest(user.password.toByteArray())).toString(16).padStart(32, '0'),
        task = user.task,
        note = user.note
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
