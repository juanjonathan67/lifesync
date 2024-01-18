package com.juanjonathan.superapp.note

import com.fasterxml.jackson.annotation.JsonBackReference
import com.juanjonathan.superapp.user.User
import jakarta.persistence.*

@Entity
@Table(name = "notes")
data class Note (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val noteId: Int,
  @Column(nullable = true)
  val title: String?,
  @Column(nullable = true)
  val image: String?,
  @Column(nullable = true)
  val description: String?,

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference
  val user: User
)