package com.dicoding.lifesync.ui.main.tabs.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.lifesync.data.Repository

class ScheduleViewModel (private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is schedule Fragment"
    }
    val text: LiveData<String> = _text
}