package com.arimdor.sharednotes.repository.dao

import android.util.Log
import com.arimdor.sharednotes.app.MyApplication
import com.arimdor.sharednotes.repository.entity.Book
import com.arimdor.sharednotes.repository.entity.Section

class SectionDao {

    private val realm = MyApplication.realm

    fun insertSection(title: String, idBook: String): Boolean {
        return try {
            val book = realm.where(Book::class.java).equalTo("id", idBook).findFirst()
            val section = Section(title)
            realm.beginTransaction()
            realm.copyToRealm(section)
            book!!.sections.add(section)
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.e("test", e.message)
            false
        }
    }

    fun findAllSections(idBook: String): MutableList<Section> {
        val book = realm.where(Book::class.java).equalTo("id",idBook).findFirst()
        return book!!.sections
    }

}