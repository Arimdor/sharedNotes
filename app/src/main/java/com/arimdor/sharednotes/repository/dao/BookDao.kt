package com.arimdor.sharednotes.repository.dao

import android.util.Log
import com.arimdor.sharednotes.app.MyApplication
import com.arimdor.sharednotes.repository.entity.Book
import io.realm.Case

class BookDao {

    private val realm = MyApplication.realm

    fun insertBook(title: String): Boolean {
        return try {
            realm.beginTransaction()
            realm.copyToRealm(Book(title))
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.e("test", e.message)
            false
        }
    }

    fun findAllBooks(): MutableList<Book> {
        return realm.where(Book::class.java).findAll()
    }

    fun findBookByTitle(title: String): MutableList<Book> {
        var books: MutableList<Book>;
        books = realm.where(Book::class.java).contains("title", title, Case.INSENSITIVE).findAll()
        return books
    }

    fun updateBook(idBook: String, title: String) {
        realm.beginTransaction()
        val book = realm.where(Book::class.java).equalTo("id", idBook).findFirst()
        book?.title = title
        realm.commitTransaction()
    }

    fun deleteBook(idBook: String) {
        realm.beginTransaction()
        val book = realm.where(Book::class.java).equalTo("id", idBook).findFirst()
        book?.deleteFromRealm()
        realm.commitTransaction()
    }
}