package com.example.dispositivosmoviles.logic.marvelLogic

import android.util.Log
import com.example.dispositivosmoviles.data.connections.ApiConnection
import com.example.dispositivosmoviles.data.endpoints.MarvelEndpoint
import com.example.dispositivosmoviles.data.entities.marvel.characters.getMarvelChars
import com.example.dispositivosmoviles.logic.data.MarvelChars

class MarvelLogic {

    suspend fun getMarvelChars(name: String, limit: Int): ArrayList<MarvelChars> {

        var itemList = arrayListOf<MarvelChars>()

        var response = ApiConnection.getService(
            ApiConnection.typeApi.Marvel,
            MarvelEndpoint::class.java
        ).getCharactersStartWith(name, limit)

        if (response.isSuccessful) {
            response.body()!!.data.results.forEach {
                val m = it.getMarvelChars()
                Log.d("UCE", response.toString())
                itemList.add(m)
            }
        } else {
            Log.d("UCE", response.toString())
        }

        return itemList
    }

    suspend fun getAllMarvelChars(offset: Int, limit: Int): ArrayList<MarvelChars> {

        var itemList = arrayListOf<MarvelChars>()

        var response = ApiConnection.getService(
            ApiConnection.typeApi.Marvel,
            MarvelEndpoint::class.java
        ).getAllMarvelChars(offset, limit)

        if (response.isSuccessful) {
            response.body()!!.data.results.forEach {
                val m = it.getMarvelChars()
                Log.d("UCE", response.toString())
                itemList.add(m)
            }
        } else {
            Log.d("UCE", response.toString())
        }

        return itemList
    }

}