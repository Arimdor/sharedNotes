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
        loadBooks()
    }

    fun getBooks(): LiveData<List<Book>> {
        return books
    }

    fun loadBooks() {
        Log.d("test","loading Books")
        books.value = bookRepository.searchAllBooks()
    }

    fun addBooks(title: String) {
        bookRepository.createBook(title)
        loadBooks()
    }

}
