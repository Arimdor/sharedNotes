package com.arimdor.sharednotes.ui.section

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arimdor.sharednotes.repository.SectionRepository
import com.arimdor.sharednotes.repository.entity.Section

class SectionViewModel : ViewModel() {

    private val sectionRepository = SectionRepository()
    private val sections = MutableLiveData<List<Section>>()

    fun getSections(): LiveData<List<Section>> {
        return sections
    }

    fun loadSections(idBook: String) {
        sections.value = sectionRepository.searchAllSections(idBook)
    }

    fun addSection(title: String, idBook: String) {
        sectionRepository.createSection(title, idBook)
        loadSections(idBook)
    }
}