package com.example.dispositivosmoviles.data.entities.marvel.characters

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
fun Result.getMarvelCharsApiCustom() : MarvelChars{

    var comic: String = "No available"

    if (comics.items.isNotEmpty()) {
        comic = comics.items[0].name
    }

    var synopsys: String = "No available"

    if (description.isNotBlank()) {
        synopsys = description
    }

    return MarvelChars(
        id,
        name,
        comic,
        synopsys,
        thumbnail.path + "." + thumbnail.extension
    )
}