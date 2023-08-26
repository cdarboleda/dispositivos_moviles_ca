package com.example.dispositivosmoviles.data.entities.marvel.characters.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dispositivosmoviles.logic.data.MarvelChars
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MarvelCharsFavoritesDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val comic: String,
    val synopsis: String,
    val image: String
) : Parcelable

fun MarvelCharsFavoritesDB.convertMarvelCharsFavDBtoNormal() : MarvelChars {
    return MarvelChars(
        id,
        name,
        comic,
        synopsis,
        image
    )
}
