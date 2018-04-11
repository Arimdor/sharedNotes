package com.arimdor.sharednotes.repository.dao

import android.util.Log
import com.arimdor.sharednotes.app.MyApplication
import com.arimdor.sharednotes.repository.entity.Book

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
}