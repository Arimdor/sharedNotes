package com.arimdor.sharednotes.repository.dao

import android.util.Log
import com.arimdor.sharednotes.app.MyApplication
import com.arimdor.sharednotes.repository.entity.Content
import com.arimdor.sharednotes.repository.entity.Note

class ContentDao {

    private val realm = MyApplication.realm

    fun insertNote(content: String, idSection: String, type: Int = 0): Boolean {
        return try {
            val section = realm.where(Note::class.java).equalTo("id", idSection).findFirst()
            val note = Content(content, type)
            realm.beginTransaction()
            realm.copyToRealm(section)
            section!!.contents.add(note)
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.e("test", e.message)
            false
        }
    }

    fun findAllNotes(idSection: String): MutableList<Content> {
        val section = realm.where(Note::class.java).equalTo("id", idSection).findFirst()
        return section!!.contents
    }

    fun updateContent(idContent: String, contentValue: String) {
        realm.beginTransaction()
        val content = realm.where(Content::class.java).equalTo("id", idContent).findFirst()
        content?.content = contentValue
        realm.commitTransaction()
    }

    fun removeContent(idContent: String) {
        realm.beginTransaction()
        realm.where(Content::class.java).equalTo("id", idContent).findFirst()?.deleteFromRealm()
        realm.commitTransaction()
    }

    fun removeAllContents(idSection: String) {
        realm.beginTransaction()
        val section = realm.where(Note::class.java).equalTo("id", idSection).findFirst()
        section!!.contents.deleteAllFromRealm()
        realm.commitTransaction()
    }
}