package com.arimdor.sharednotes.ui.content

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.arimdor.sharednotes.repository.ContentRepository
import com.arimdor.sharednotes.repository.entity.Content

class ContentViewModel : ViewModel() {

    private val noteRepository = ContentRepository()
    private val notes = MutableLiveData<List<Content>>()
    var photoUri: String = ""

    init {
        Log.e("test", "ContentViewModel Init")
    }


    fun getNotes(): LiveData<List<Content>> {
        return notes
    }

    fun loadNotes(idSection: String) {
        notes.value = noteRepository.searchAllNotes(idSection)
    }

    fun addNote(content: String, idSection: String, type: Int = 0) {
        noteRepository.createNote(content, idSection, type)
        loadNotes(idSection)
    }

    fun removeAllNotes(idSection: String) {
        noteRepository.deleteAllNotes(idSection)
        loadNotes(idSection)
    }
}