package com.dicoding.lifesync.ui.main.tabs.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.lifesync.data.Repository

class NotesViewModel (private val repository: Repository) : ViewModel() {

    fun createNote(title: String, imageUrl: String, description: String) = repository.createTask(title, imageUrl, description)
}