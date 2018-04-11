package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.SectionDao
import com.arimdor.sharednotes.repository.entity.Section

class SectionRepository {
    private val sectionDao = SectionDao()

    fun createSection(title: String, idBook: String) {
        sectionDao.insertSection(title, idBook)
    }

    fun searchAllSections(idBook: String): MutableList<Section> {
        return sectionDao.findAllSections(idBook)
    }
}