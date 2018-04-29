package com.arimdor.sharednotes.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.NoteRepository
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.utils.Constants

class NoteViewModel : ViewModel() {

    private val noteRepository = NoteRepository()
    private val notes = MutableLiveData<List<Note>>()
    var idBook = ""

    fun getNotes(): LiveData<List<Note>> {
        return notes
    }

    fun loadNotes(idBook: String = this.idBook) {
        notes.value = noteRepository.searchAllNotes(idBook)
    }

    fun createNote(title: String, idBook: String): Note? {
        val note = noteRepository.createNote(title, idBook)
        loadNotes(idBook)
        return note
    }

    fun updateNote(idNote: String, title: String) {
        noteRepository.updateNote(idNote, title)
        loadNotes(idBook)
    }

    fun removeNote(idNote: String) {
        noteRepository.removeNote(idNote)
        loadNotes(idBook)
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
        return uri
    }
}