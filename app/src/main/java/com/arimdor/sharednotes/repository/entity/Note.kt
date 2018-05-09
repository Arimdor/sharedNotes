package com.arimdor.sharednotes.repository.entity;

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Note(
        @PrimaryKey
        @Required
        var id: String = "",
        @Required
        var title: String = "Sin Titulo",
        @Required
        var createdBy: String = "Anónimo",
        var contents: RealmList<Content> = RealmList(),
        @Required
        var createdAt: Date? = null,
        var updatedAt: Date? = null

) : RealmObject(), RealmModel {
    override fun toString(): String {
        return "Note(id='$id', title='$title', createdBy='$createdBy', contents=$contents, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}


