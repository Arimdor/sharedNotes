package com.arimdor.sharednotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.arimdor.sharednotes.repository.NoteRepository
import com.arimdor.sharednotes.repository.entity.Note

class NoteViewModel : ViewModel() {

    private val noteRepository = NoteRepository()
    private val notes = MutableLiveData<List<Note>>()
    val tempPhoto = MutableLiveData<Bitmap>()


    fun getNotes(): LiveData<List<Note>> {
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