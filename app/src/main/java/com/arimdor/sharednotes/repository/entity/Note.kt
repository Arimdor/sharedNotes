package com.arimdor.sharednotes.repository.entity

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Note(
        @Required
        var content: String = ""

) : RealmObject(), RealmModel {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    @Required
    var creationDate: Date = Date()

}

