package com.juanjonathan.superapp.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tasks")
class TaskController (@Autowired private val taskRepository: TaskRepository){

  /**
   * Get all tasks
   * @returns List of all tasks
   */
  @GetMapping("")
  fun getAllTasks(): List<Task> {
    return taskRepository.findAll().toList()
  }

  /**
   * Find a task by its id
   * @return Responds with either the task or not found status
   */
  @GetMapping("/{id}")
  fun findTaskById(@PathVariable("id") taskId: Int): ResponseEntity<Task>{
    val task = taskRepository.findById(taskId).orElse(null)

    if (task == null) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    return ResponseEntity(task, HttpStatus.OK)
  }

  /**
   * Create a task and save it to database
   * @returns Respond with successfully saved task or error status
   */
  @PostMapping("")
  fun createTask(@RequestBody task: Task): ResponseEntity<Task> {
    try {
      val savedTask = taskRepository.save(task)
      return ResponseEntity(savedTask, HttpStatus.OK)
    } catch (error: Exception) {
      return ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
    }
  }

  /**
   * Updates a task's information
   * @return Responds with either the updated task or not found status
   */
  @PutMapping("/{id}")
  fun updateTaskById(@PathVariable("id") taskId : Int, @RequestBody task: Task): ResponseEntity<Task> {
    val existingTask = taskRepository.findById(taskId).orElse(null)

    if(existingTask == null){
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    try {
      val updatedTask = existingTask.copy(
        title = task.title,
        startDateTime = task.startDateTime,
        endDateTime = task.endDateTime,
        description = task.description,
        color = task.color
      )

      taskRepository.save(updatedTask)
      return ResponseEntity(updatedTask, HttpStatus.OK)
    } catch (error: Exception) {
      return ResponseEntity(HttpStatus.CONFLICT)
    }
  }

  /**
   * Delete a task
   * @return Responds with status
   */
  @DeleteMapping("/{id}")
  fun deleteTaskById(@PathVariable("id") taskId: Int): ResponseEntity<Task>{
    if (!taskRepository.existsById(taskId)){
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    taskRepository.deleteById(taskId)
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }


}