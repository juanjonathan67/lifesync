package com.juanjonathan.superapp.note

import org.springframework.data.repository.CrudRepository

interface NoteRepository : CrudRepository<Note, Int> {


}