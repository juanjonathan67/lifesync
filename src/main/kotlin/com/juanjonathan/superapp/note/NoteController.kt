package com.juanjonathan.superapp.note

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notes")
class NoteController (@Autowired private val noteRepository: NoteRepository){

  /**
   * Get all tasks
   * @returns List of all tasks
   */
  @GetMapping("")
  fun getAllNotes(): List<Note> {
    return noteRepository.findAll().toList()
  }

  /**
   * Find a task by its id
   * @return Responds with either the task or not found status
   */
  @GetMapping("/{id}")
  fun findNoteById(@PathVariable("id") noteId: Int): ResponseEntity<Note>{
    val note = noteRepository.findById(noteId).orElse(null)

    if (note == null) {
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    return ResponseEntity(note, HttpStatus.OK)
  }

  /**
   * Create a task and save it to database
   * @returns Respond with successfully saved task or error status
   */
  @PostMapping("")
  fun createNote(@RequestBody note: Note): ResponseEntity<Note> {
    try {
      val savedNote = noteRepository.save(note)
      return ResponseEntity(savedNote, HttpStatus.OK)
    } catch (error: Exception) {
      return ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
    }
  }

  /**
   * Updates a task's information
   * @return Responds with either the updated task or not found status
   */
  @PutMapping("/{id}")
  fun updateNoteById(@PathVariable("id") noteId : Int, @RequestBody note: Note): ResponseEntity<Note> {
    val existingNote = noteRepository.findById(noteId).orElse(null)

    if(existingNote == null){
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    try {
      val updatedNote = existingNote.copy(
        title = note.title,
        image = note.image,
        description = note.description,
      )

      noteRepository.save(updatedNote)
      return ResponseEntity(updatedNote, HttpStatus.OK)
    } catch (error: Exception) {
      return ResponseEntity(HttpStatus.CONFLICT)
    }
  }

  /**
   * Delete a task
   * @return Responds with status
   */
  @DeleteMapping("/{id}")
  fun deleteNoteById(@PathVariable("id") noteId: Int): ResponseEntity<Note>{
    if (!noteRepository.existsById(noteId)){
      return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    noteRepository.deleteById(noteId)
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }


}