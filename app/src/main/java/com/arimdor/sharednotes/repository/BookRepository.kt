package com.arimdor.sharednotes.repository

import com.arimdor.sharednotes.repository.dao.BookDao
import com.arimdor.sharednotes.repository.entity.Book


class BookRepository {

    private val bookDao = BookDao()

    fun createBook(title: String) {
        bookDao.insertBook(title)
    }

    fun searchAllBooks(): MutableList<Book> {
        return bookDao.findAllBooks()
    }

    fun updateBook(idBook: String, title: String) {
        bookDao.updateBook(idBook, title)
    }

    fun deleteBook(idBook: String) {
        bookDao.deleteBook(idBook)
    }

}