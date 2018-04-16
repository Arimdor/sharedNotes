package com.arimdor.sharednotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.NoteRepository
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.util.Constants

class NoteViewModel : ViewModel() {

    private val noteRepository = NoteRepository()
    private val notes = MutableLiveData<List<Note>>()
    var idBook = ""

    fun getSections(): LiveData<List<Note>> {
        return notes
    }

    fun loadSections(idBook: String) {
        notes.value = noteRepository.searchAllSections(idBook)
    }

    fun addSection(title: String, idBook: String) {
        noteRepository.createSection(title, idBook)
        loadSections(idBook)
    }

    fun updateNote(idNote: String, title: String) {
        noteRepository.updateNote(idNote, title)
        loadSections(idBook)
    }

    fun removeNote(idNote: String) {
        noteRepository.removeNote(idNote)
        loadSections(idBook)
    }

    fun generateResumeText(note: Note): String {
        var resume = ""
        for (content in note.contents) {
            if (content.type == Constants.TYPE_TEXT) {
                if (resume.length < 80) {
                    resume += " ${content.content}"
                } else {
                    resume += "${content.content}..."
                    break
                }
            }
        }
        return resume
    }

    fun generateResumeImage(note: Note): String {
        var uri: String = ""
        for (content in note.contents) {
            if (content.type == Constants.TYPE_IMAGE) {
                uri = content.content
                break
            }
        }
        if (uri.isEmpty()) {
            uri = "android.resource://com.arimdor.sharednotes/drawable/${R.drawable.logo_splash}"
        }
        return uri
    }
}