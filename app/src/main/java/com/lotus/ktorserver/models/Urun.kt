package com.lotus.ktorserver.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "urunler")
data class Urun(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var urunIsmi: String,
    var fiyati: Double,
    var active: Boolean = true
)
