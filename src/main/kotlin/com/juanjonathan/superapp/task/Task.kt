package com.juanjonathan.superapp.task

import com.fasterxml.jackson.annotation.JsonBackReference
import com.juanjonathan.superapp.user.User
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val taskId: Int,
  @Column(nullable = true)
  val title: String?,
  @Column(nullable = true, columnDefinition = "TIMESTAMP")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  val startDateTime: LocalDateTime?,
  @Column(nullable = true, columnDefinition = "TIMESTAMP")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  val endDateTime: LocalDateTime?,
  @Column(nullable = true)
  val description: String?,
  @Column(nullable = true)
  val color: String?,

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference
  val user: User
)