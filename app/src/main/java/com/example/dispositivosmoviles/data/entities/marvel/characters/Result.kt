package com.example.dispositivosmoviles.data.entities.marvel.characters

import com.example.dispositivosmoviles.data.entities.marvel.characters.Result
import com.example.dispositivosmoviles.data.entities.marvel.characters.database.MarvelCharsDB
import com.example.dispositivosmoviles.logic.data.MarvelChars

data class Result(
    val comics: Comics,
    val description: String,
    val events: Events,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val stories: Stories,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)
//Funcion de extension para que me devuelva un MarvelChar custom
fun Result.getMarvelChars() : MarvelChars{

    var comic: String = "No available"

    if (comics.items.isNotEmpty()) {
        comic = comics.items[0].name
    }

    return MarvelChars(
        id,
        name,
        comic,
        description,
        thumbnail.path + "." + thumbnail.extension
    )
}