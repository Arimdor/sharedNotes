package com.arimdor.sharednotes.repository.entity

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Content(
        @PrimaryKey
        @Required
        var id: String = "",
        @Required
        var content: String = "",
        @Required
        var createdBy: String = "",
        var type: Int = 0,
        @Required
        var createdAt: Date? = null,
        var updatedAt: Date? = null

) : RealmObject(), RealmModel {
    override fun toString(): String {
        return "Content(id='$id', content='$content', createdBy='$createdBy', type=$type, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}

