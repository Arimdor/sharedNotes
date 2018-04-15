package com.arimdor.sharednotes.repository.entity;

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Note(
        @Required
        var title: String = "Sin Titulo"

) : RealmObject(), RealmModel {

    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    var contents: RealmList<Content> = RealmList()

    @Required
    var creationDate: Date = Date()
}