package com.arimdor.sharednotes.repository.dao

import android.util.Log
import com.arimdor.sharednotes.app.MyApplication
import com.arimdor.sharednotes.repository.entity.Note
import com.arimdor.sharednotes.repository.entity.Section

class NoteDao {

    private val realm = MyApplication.realm

    fun insertNote(content: String, idSection: String): Boolean {
        return try {
            val section = realm.where(Section::class.java).equalTo("id", idSection).findFirst()
            val note = Note(content)
            realm.beginTransaction()
            realm.copyToRealm(section)
            section!!.notes.add(note)
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.e("test", e.message)
            false
        }
    }

    fun findAllNotes(idSection: String): MutableList<Note> {
        val section = realm.where(Section::class.java).equalTo("id", idSection).findFirst()
        return section!!.notes
    }

    fun removeAllNotes(idSection: String) {
        realm.beginTransaction()
        val section = realm.where(Section::class.java).equalTo("id", idSection).findFirst()
        section!!.notes.deleteAllFromRealm()
        realm.commitTransaction()
    }
}