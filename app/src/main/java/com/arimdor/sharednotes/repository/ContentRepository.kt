package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.ContentDao
import com.arimdor.sharednotes.repository.entity.Content

class ContentRepository {

    private val noteDao = ContentDao()

    fun createNote(content: String, idSection: String, type: Int = 0) {
        noteDao.insertNote(content, idSection, type)
    }

    fun searchAllNotes(idSection: String): MutableList<Content> {
        return noteDao.findAllNotes(idSection)
    }

    fun updateContent(idContent: String, contentValue: String) {
        noteDao.updateContent(idContent, contentValue)
    }

    fun deleteContent(idContent: String) {
        noteDao.removeContent(idContent)
    }

    fun deleteAllNotes(idSection: String) {
        noteDao.removeAllContents(idSection)
    }
}