package com.juanjonathan.superapp

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Int,
  val userName: String,
  val email: String
)