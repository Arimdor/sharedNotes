package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.SectionDao
import com.arimdor.sharednotes.repository.entity.Note

class NoteRepository {
    private val sectionDao = SectionDao()

    fun createSection(title: String, idBook: String) {
        sectionDao.insertSection(title, idBook)
    }

    fun searchAllSections(idBook: String): MutableList<Note> {
        return sectionDao.findAllSections(idBook)
    }
}