package com.arimdor.sharednotes.ui.content

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arimdor.sharednotes.repository.ContentRepository
import com.arimdor.sharednotes.repository.entity.Content

class ContentViewModel : ViewModel() {

    private val contentRepository = ContentRepository()
    private val notes = MutableLiveData<List<Content>>()
    var photoUri: String = ""
    var idSection = ""

    fun getContents(): LiveData<List<Content>> {
        return notes
    }

    fun loadCotents(idSection: String = this.idSection) {
        notes.value = contentRepository.searchAllNotes(idSection)
    }

    fun addContent(content: String, idSection: String, type: Int = 0) {
        contentRepository.createNote(content, idSection, type)
        loadCotents(idSection)
    }

    fun updateContent(idContent: String, contentValue: String) {
        contentRepository.updateContent(idContent, contentValue)
        loadCotents(idSection)
    }

    fun removeContent(idContent: String) {
        contentRepository.deleteContent(idContent)
        loadCotents(idSection)
    }

    fun removeAllContents(idSection: String) {
        contentRepository.deleteAllNotes(idSection)
        loadCotents(this.idSection)
    }
}