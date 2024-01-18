package com.juanjonathan.superapp.user

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.juanjonathan.superapp.note.Note
import com.juanjonathan.superapp.task.Task
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val userId: Int,
  @Column(unique = true)
  val username: String,
  @Column(unique = true)
  val email: String,
  val password: String,

  @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", orphanRemoval = true)
//  @JoinColumn(name = "fk_task_id", referencedColumnName = "user_id")
  @JsonManagedReference
  @Column(nullable = true)
  val task: List<Task>?,

  @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", orphanRemoval = true)
//  @JoinColumn(name = "fk_note_id", referencedColumnName = "user_id")
  @JsonManagedReference
  @Column(nullable = true)
  val note: List<Note>?,
)

data class SearchData (
  val username: String?,
  val email: String?
)

data class LoginCredentials (
  val usernameOrEmail: String,
  val password: String
)
