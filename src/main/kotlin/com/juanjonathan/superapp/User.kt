package com.juanjonathan.superapp

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
  val password: String
)