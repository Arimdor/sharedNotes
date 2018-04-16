package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.NoteDao
import com.arimdor.sharednotes.repository.entity.Note

class NoteRepository {
    private val sectionDao = NoteDao()

    fun createSection(title: String, idBook: String) {
        sectionDao.insertSection(title, idBook)
    }

    fun updateNote(idNote: String, title: String) {
        sectionDao.updateNote(idNote, title)
    }

    fun removeNote(idNote: String) {
        sectionDao.removeNote(idNote)
    }

    fun searchAllSections(idBook: String): MutableList<Note> {
        return sectionDao.findAllNotesInBook(idBook)
    }
}