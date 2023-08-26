package com.example.dispositivosmoviles.logic.data

import android.os.Parcelable
import com.example.dispositivosmoviles.data.entities.jikan.characters.database.JikanCharsDB
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JikanChars(
    val id: Int,
    val name: String,
    val comic: String,
    val synopsis: String,
    val image: String
) : Parcelable



fun JikanChars.getJikanCharsDB() : JikanCharsDB {
    return JikanCharsDB(
        id,
        name,
        comic,
        synopsis,
        image
    )
}