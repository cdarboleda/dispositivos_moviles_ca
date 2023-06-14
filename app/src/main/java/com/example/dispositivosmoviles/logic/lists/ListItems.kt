package com.example.dispositivosmoviles.logic.lists

import com.example.dispositivosmoviles.data.entities.LoginUser
import com.example.dispositivosmoviles.data.entities.marvel.MarvelChars

class ListItems {

    fun returnItems(): List<LoginUser> {
        var items = listOf<LoginUser>(
            LoginUser("1", "1"),
            LoginUser("2", "1"),
            LoginUser("3", "1"),
            LoginUser("4", "1"),
            LoginUser("5", "1")

        )

        return items
    }

    fun returnMarvelChars(): List<MarvelChars> {
        val items = listOf(
            MarvelChars(
                1,
                "Spider-Man",
                "The Amazing Spider-Man Vol.7 (2014)",
                "https://comicvine.gamespot.com/a/uploads/scale_small/6/67663/3493707-700.3a.jpg"
            ),
            MarvelChars(
                2,
                "Black Panther",
                "Black Panther (2003)",
                "https://comicvine.gamespot.com/a/uploads/scale_small/2/26954/659356-5395_20060926192902_large.jpg"
            ),
            MarvelChars(
                3,
                "Black Cat",
                "The Amazing Spider-Man Vol.5 (2012)",
                "https://comicvine.gamespot.com/a/uploads/scale_small/11144/111442876/8759849-grr.jpg"
            ),
            MarvelChars(
                4,
                "Spider-Woman",
                "The Amazing Spider-Man Vol.3 (2005)",
                "https://comicvine.gamespot.com/a/uploads/scale_small/11116/111167641/7822219-spiderwoman_issue11-art.jpg"
            ),
            MarvelChars(
                5,
                "Blade",
                "Blade (2007)",
                "https://comicvine.gamespot.com/a/uploads/scale_small/12/124259/7949396-heroes_reborn_vol_2_1_devil_dog_comics_exclusive_virgin_variant.jpg"
            ),

            )

        return items
    }
}