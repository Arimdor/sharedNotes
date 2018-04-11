package com.arimdor.sharednotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arimdor.sharednotes.repository.NoteRepository
import com.arimdor.sharednotes.repository.entity.Note

class NoteViewModel : ViewModel() {

    private val noteRepository = NoteRepository()
    private val notes = MutableLiveData<List<Note>>()

    fun getNotes(): LiveData<List<Note>> {
        return notes
    }

    fun loadNotes(idSection: String) {
        notes.value = noteRepository.searchAllNotes(idSection)
    }

    fun addNotes(title: String, idSection: String) {
        noteRepository.createNote(title, idSection)
        loadNotes(idSection)
    }

    fun removeAllNotes(idSection: String) {
        noteRepository.deleteAllNotes(idSection)
        loadNotes(idSection)
    }
}