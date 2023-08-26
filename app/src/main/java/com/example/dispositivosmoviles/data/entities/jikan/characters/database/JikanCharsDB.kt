package com.example.dispositivosmoviles.data.entities.jikan.characters.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dispositivosmoviles.logic.data.JikanChars
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class JikanCharsDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val comic: String,
    val synopsis: String,
    val image: String
) : Parcelable

fun JikanCharsDB.convertJikanDBtoNormal() : JikanChars {
    return JikanChars(
        id,
        name,
        comic,
        synopsis,
        image
    )
}