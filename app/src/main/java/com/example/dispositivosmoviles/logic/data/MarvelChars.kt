package com.example.dispositivosmoviles.logic.data

import android.os.Parcelable
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsFavoritesDB
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarvelChars(
    val id: Int,
    val name: String,
    val comic: String,
    val synopsis: String,
    val image: String
) : Parcelable


fun MarvelChars.convertMarvelCharsNormaltoDB() : MarvelCharsDB {
    return MarvelCharsDB(
        id,
        name,
        comic,
        synopsis,
        image
    )
}

fun MarvelChars.convertMarvelCharsNormaltoFavDB() : MarvelCharsFavoritesDB {
    return MarvelCharsFavoritesDB(
        id,
        name,
        comic,
        synopsis,
        image
    )
}