package com.arimdor.sharednotes.repository.entity

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Book(
        @PrimaryKey
        @Required
        var id: String = "",
        @Required
        var title: String = "",
        @Required
        var createdBy: String = "",
        var notes: RealmList<Note> = RealmList(),
        @Required
        var createdAt: Date? = null,
        var updatedAt: Date? = null

) : RealmObject(), RealmModel {
    override fun toString(): String {
        return "Book(id='$id', title='$title', createdBy='$createdBy', notes=$notes, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}

