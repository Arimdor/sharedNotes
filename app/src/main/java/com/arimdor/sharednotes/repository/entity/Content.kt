package com.arimdor.sharednotes.repository.entity

import com.arimdor.sharednotes.util.Constants
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Content(
        @Required
        var content: String = "",
        var type: Int = Constants.TYPE_TEXT

) : RealmObject(), RealmModel {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    @Required
    var creationDate: Date = Date()

}

