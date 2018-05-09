package com.arimdor.sharednotes.ui.content

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.arimdor.sharednotes.repository.ContentRepository
import com.arimdor.sharednotes.repository.entity.Content

class ContentViewModel : ViewModel() {

    private var contentRepository = ContentRepository()
    private var contents = MutableLiveData<List<Content>>()
    var photoUri: String = ""
    var idSection = ""

    fun getContents(): LiveData<List<Content>> {
        return contents
    }

    fun loadCotents(idSection: String = this.idSection) {
        Log.d("test", "loading Contents id = $idSection")
        if (!idSection.isEmpty()) {
            contents = contentRepository.searchAllContents(idSection)
        }
    }

    fun addContent(content: String, idSection: String, type: Int = 0) {
        contentRepository.createContent(content, idSection, type)
        loadCotents()
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
        contentRepository.deleteAllContents(idSection)
        loadCotents(this.idSection)
    }
}