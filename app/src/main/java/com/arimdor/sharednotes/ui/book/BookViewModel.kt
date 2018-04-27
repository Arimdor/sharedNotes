package com.arimdor.sharednotes.ui.book

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.arimdor.sharednotes.repository.BookRepository
import com.arimdor.sharednotes.repository.entity.Book

class BookViewModel : ViewModel() {

    private val bookRepository = BookRepository()
    private var books = MutableLiveData<List<Book>>()

    init {
        if (books.value == null) {
            loadBooks()
        }
    }

    fun getBooks(): LiveData<List<Book>> {
        return books
    }

    fun loadBooks() {
        Log.d("test", "loading Books")
        books.value = bookRepository.searchAllBooks()
    }

    fun addBooks(title: String) {
        bookRepository.createBook(title)
        loadBooks()
    }

    fun findBookByTitle(title: String) {
        books.value = bookRepository.findBookByTitle(title)
    }

    fun updateBook(idBook: String, title: String) {
        bookRepository.updateBook(idBook, title)
        loadBooks()
    }

    fun deleteBook(idBook: String) {
        bookRepository.deleteBook(idBook)
        loadBooks()
    }

}

