package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.NoteDao
import com.arimdor.sharednotes.repository.entity.Note

class NoteRepository {

    private val noteDao = NoteDao()

    fun createNote(title: String, idSection: String) {
        noteDao.insertNote(title, idSection)
    }

    fun searchAllNotes(idSection: String): MutableList<Note> {
        return noteDao.findAllNotes(idSection)
    }

    fun deleteAllNotes(idSection: String) {
        noteDao.removeAllNotes(idSection)
    }
}