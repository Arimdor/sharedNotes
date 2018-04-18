package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.NoteDao
import com.arimdor.sharednotes.repository.entity.Note

class NoteRepository {
    private val noteDao = NoteDao()

    fun createNote(title: String, idBook: String): Note? {
        return noteDao.insertNote(title, idBook)
    }

    fun findNoteByID(idNote: String): Note? {
        return noteDao.findNoteByID(idNote)
    }

    fun updateNote(idNote: String, title: String) {
        noteDao.updateNote(idNote, title)
    }

    fun removeNote(idNote: String) {
        noteDao.removeNote(idNote)
    }

    fun searchAllNotes(idBook: String): MutableList<Note> {
        return noteDao.findAllNotesInBook(idBook)
    }
}