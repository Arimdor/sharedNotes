package com.arimdor.sharednotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arimdor.sharednotes.repository.NoteRepository
import com.arimdor.sharednotes.repository.entity.Note

class NoteViewModel : ViewModel() {

    private val sectionRepository = NoteRepository()
    private val sections = MutableLiveData<List<Note>>()
    var idBook = ""

    fun getSections(): LiveData<List<Note>> {
        return sections
    }

    fun loadSections(idBook: String) {
        sections.value = sectionRepository.searchAllSections(idBook)
    }

    fun addSection(title: String, idBook: String) {
        sectionRepository.createSection(title, idBook)
        loadSections(idBook)
    }

    fun updateNote(idNote: String, title: String) {
        sectionRepository.updateNote(idNote, title)
        loadSections(idBook)
    }

    fun removeNote(idNote: String) {
        sectionRepository.removeNote(idNote)
        loadSections(idBook)
    }
}