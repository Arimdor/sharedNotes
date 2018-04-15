package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.NoteDao
import com.arimdor.sharednotes.repository.entity.Content

class ContentRepository {

    private val noteDao = NoteDao()

    fun createNote(content: String, idSection: String, type: Int = 0) {
        noteDao.insertNote(content, idSection, type)
    }

    fun searchAllNotes(idSection: String): MutableList<Content> {
        return noteDao.findAllNotes(idSection)
    }

    fun deleteAllNotes(idSection: String) {
        noteDao.removeAllNotes(idSection)
    }
}