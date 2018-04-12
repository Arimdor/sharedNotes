package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.NoteDao
import com.arimdor.sharednotes.repository.entity.Note

class NoteRepository {

    private val noteDao = NoteDao()

    fun createNote(content: String, idSection: String, type: Int = 0) {
        noteDao.insertNote(content, idSection, type)
    }

    fun searchAllNotes(idSection: String): MutableList<Note> {
        return noteDao.findAllNotes(idSection)
    }

    fun deleteAllNotes(idSection: String) {
        noteDao.removeAllNotes(idSection)
    }
}