package com.juanjonathan.superapp.task

import org.springframework.data.repository.CrudRepository

interface TaskRepository : CrudRepository<Task, Int> {


}